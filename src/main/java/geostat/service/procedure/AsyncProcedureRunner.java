package geostat.service.procedure;

import geostat.domain.procedure.Procedure;
import geostat.model.procedure.ProcCall;
import geostat.model.procedure.ProcContext;
import geostat.model.procedure.ProcedureRequestDto;
import geostat.model.procedure.ProcedureStatus;
import geostat.repository.procedure.ProcedureStatusRepository;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.util.*;
import java.util.function.Function;

@Component
public class AsyncProcedureRunner {

    private final ProcedureStatusRepository procedureStatusRepository;
    private final JdbcTemplate jdbcTemplate;

    public AsyncProcedureRunner(ProcedureStatusRepository procedureStatusRepository, JdbcTemplate jdbcTemplate) {
        this.procedureStatusRepository = procedureStatusRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final Map<Integer, Function<ProcContext, ProcCall>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put(14, c -> new ProcCall("{call [KD].[dbo].[Sync_Bes_Fin_formio](?)}",
                new Integer[]{c.getYear()}, true));

        REGISTRY.put(2, c -> new ProcCall("{call [KD].[dbo].[Sync_BES_formio](?, ?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear(), c.getQuarter()}, true));

        REGISTRY.put(3, c -> new ProcCall("{call [KD].[dbo].[Sync_BES_formio_annual_ie](?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear()}, true));

        REGISTRY.put(4, c -> new ProcCall("{call [KD].[dbo].[Sync_BES_formio_annual_lp_batching](?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear()}, true));

        REGISTRY.put(48, c -> new ProcCall("{call [KD].[dbo].[Sync_CATI_formio](?, ?)}",
                new Integer[]{c.getYear(), c.getMonth()}, false));

        REGISTRY.put(8, c -> new ProcCall("{call [KD].[dbo].[Sync_Courier_formio](?)}",
                new Integer[]{c.getYear()}, true));

        REGISTRY.put(9, c -> new ProcCall("{call [KD].[dbo].[Sync_Elevator_formio](?, ?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear(), c.getQuarter()}, true));

        REGISTRY.put(25, c -> new ProcCall("{call [KD].[dbo].[Sync_Elevator_formio_annual](?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear()}, true));

        REGISTRY.put(15, c -> new ProcCall("{call [KD].[dbo].[Sync_EXT_formio](?, ?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear(), c.getQuarter()}, true));

        REGISTRY.put(40, c -> new ProcCall("{call [KD].[dbo].[Sync_EXT_formio_annual](?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear()}, true));

        REGISTRY.put(13, c -> new ProcCall("{call [KD].[dbo].[Sync_EXTNORES_formio](?, ?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear(), c.getQuarter()}, true));

        REGISTRY.put(24, c -> new ProcCall("{call [KD].[dbo].[Sync_EXTNORES_formio_annual](?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear()}, true));

        REGISTRY.put(20, c -> new ProcCall("{call [KD].[dbo].[Sync_FIN_formio](?)}",
                new Integer[]{c.getYear()}, true));

        REGISTRY.put(21, c -> new ProcCall("{call [KD].[dbo].[Sync_HOT_New](?)}",
                new Integer[]{c.getYear()}, true));

        REGISTRY.put(50, c -> new ProcCall("{call [KD].[dbo].[Sync_ICT_new](?, ?)}",
                new Integer[]{c.getYear(), c.getSurveyId()}, true));

        REGISTRY.put(49, c -> new ProcCall("{call [KD].[dbo].[Sync_INOVA_new](?, ?)}",
                new Integer[]{c.getYear(), c.getSurveyId()}, true));

        REGISTRY.put(17, c -> new ProcCall("{call [KD].[dbo].[Sync_Labour_formio](?, ?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear(), c.getQuarter()}, true));

        REGISTRY.put(19, c -> new ProcCall("{call [KD].[dbo].[Sync_NEC_formio](?)}",
                new Integer[]{c.getYear()}, true));

        REGISTRY.put(5, c -> new ProcCall("{call [KD].[dbo].[Sync_Refrigerator_formio](?, ?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear(), c.getQuarter()}, true));

        REGISTRY.put(26, c -> new ProcCall("{call [KD].[dbo].[Sync_Refrigerator_formio_annual](?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear()}, true));

        REGISTRY.put(10, c -> new ProcCall("{call [KD].[dbo].[Sync_Slaughterhouses_formio](?, ?, ?)}",
                new Integer[]{c.getSurveyId(), c.getYear(), c.getQuarter()}, true));

        REGISTRY.put(27, c -> new ProcCall("{call [KD].[dbo].[Sync_Slaughterhouses_formio_annual](?)}",
                new Integer[]{c.getYear()}, true));

        REGISTRY.put(18, c -> new ProcCall("{call [KD].[dbo].[Sync_TRD_formio](?)}",
                new Integer[]{c.getYear()}, true));
    }

    @Async("procedureExecutor")
    public void runProcedureAsync(ProcedureRequestDto procedureRequestDto, UUID jobId) {

        int surveyId = procedureRequestDto.getSurveyId().intValue();
        Integer year = procedureRequestDto.getYear();
        Integer quarter = procedureRequestDto.getQuarter();
        Integer month = procedureRequestDto.getMonth();

        Optional<Procedure> byId = procedureStatusRepository.getById(jobId);

        if (!byId.isPresent()) {
            throw new RuntimeException("SyncJob not found: " + jobId);
        }

        Procedure job = byId.get();
        job.setStatus(ProcedureStatus.RUNNING);
        job.setStartedAt(new Date());
        procedureStatusRepository.save(job);

        try {

            ProcCall call = resolveProcCall(surveyId, year, quarter, month);

            Integer[] params = call.getParams();
            for (int i = 0; i < params.length; i++) {
                if (params[i] == null) {
                    throw new IllegalArgumentException(
                            "surveyId=" + surveyId + ": parameter at position " + (i + 1) + " is null");
                }
            }

            Integer result = executeProc(call);

            job.setStatus(ProcedureStatus.DONE);
            job.setResult(result);

        } catch (Exception e) {

            job.setStatus(ProcedureStatus.FAILED);
            job.setErrorMessage(e.getMessage());

        } finally {

            job.setFinishedAt(new Date());
            procedureStatusRepository.save(job);
        }
    }

    private ProcCall resolveProcCall(int surveyId, int year, int quarter, int month) {

        Function<ProcContext, ProcCall> factory = REGISTRY.get(surveyId);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown surveyId: " + surveyId);
        }

        return factory.apply(new ProcContext(surveyId, year, quarter, month));
    }

    private Integer executeProc(ProcCall call) {

        final int inCount = call.getParams().length;

        return jdbcTemplate.execute(

                conn -> {
                    CallableStatement cs = conn.prepareCall(call.getSql());
                    cs.setQueryTimeout(2400); // 40 წუთი, მარჟით
                    for (int i = 0; i < inCount; i++) {
                        cs.setInt(i + 1, call.getParams()[i]);
                    }
                    if (call.isHasOutParam()) {
                        cs.registerOutParameter(inCount + 1, java.sql.Types.INTEGER);
                    }
                    return cs;
                },
                (CallableStatementCallback<Integer>) cs -> {
                    cs.execute();
                    return call.isHasOutParam() ? cs.getInt(inCount + 1) : null;
                });
    }
}
