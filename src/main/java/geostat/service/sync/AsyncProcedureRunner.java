package geostat.service.sync;

import geostat.domain.sync.SyncJob;
import geostat.repository.sync.SyncJobRepository;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class AsyncProcedureRunner {

    private final SyncJobRepository syncJobRepository;
    private final JdbcTemplate jdbcTemplate;

    public AsyncProcedureRunner(SyncJobRepository syncJobRepository, JdbcTemplate jdbcTemplate) {
        this.syncJobRepository = syncJobRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async("procedureExecutor")
    public void runProcedureAsync(UUID jobId, Integer surveyId, Integer year, Integer quarter) {

        System.out.println("runProcedureAsync thread: {}  " + Thread.currentThread().getName());

        Optional<SyncJob> byId = syncJobRepository.getById(jobId);

        if (!byId.isPresent()) {
            throw new RuntimeException();
        }

        SyncJob job = byId.get();
        job.setStatus("RUNNING");
        job.setStartedAt(new Date());
        syncJobRepository.save(job);

        try {
            String sql = "{call [KD].[dbo].[Sync_Labour_formio](?, ?, ?, ?)}";

            Integer count = jdbcTemplate.execute(conn -> {

                        CallableStatement cs = conn.prepareCall(sql);
                        cs.setQueryTimeout(2400); // 40 წუთი, მარჟით
                        cs.setInt(1, surveyId);
                        cs.setInt(2, year);
                        cs.setInt(3, quarter);
                        cs.registerOutParameter(4, java.sql.Types.INTEGER);
                        return cs;
                    },
                    (CallableStatementCallback<Integer>) cs -> {
                        cs.execute();
                        return cs.getInt(4);
                    });

            job.setStatus("DONE");
            job.setResult(count);

        } catch (Exception e) {

            job.setStatus("FAILED");
            job.setErrorMessage(e.getMessage());

        } finally {

            job.setFinishedAt(new Date());
            syncJobRepository.save(job);
        }
    }
}
