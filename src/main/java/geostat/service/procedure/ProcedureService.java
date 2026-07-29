package geostat.service.procedure;

import geostat.domain.procedure.Procedure;
import geostat.model.procedure.ProcedureRequestDto;
import geostat.model.procedure.ProcedureResponseDto;
import geostat.model.procedure.ProcedureStatus;
import geostat.repository.procedure.ProcedureStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProcedureService {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final ProcedureStatusRepository procedureStatusRepository;
    private final AsyncProcedureRunner asyncProcedureRunner;

    public ProcedureService(ProcedureStatusRepository procedureStatusRepository, AsyncProcedureRunner asyncProcedureRunner) {
        this.procedureStatusRepository = procedureStatusRepository;
        this.asyncProcedureRunner = asyncProcedureRunner;
    }

    @Transactional
    public ProcedureResponseDto startSync(ProcedureRequestDto procedureRequestDto) {

        Long surveyId = procedureRequestDto.getSurveyId();
        Integer year = procedureRequestDto.getYear();
        Integer quarter = procedureRequestDto.getQuarter();
        Integer month = procedureRequestDto.getMonth();

        String jobId = UUID.randomUUID().toString();

        Date crateTime = new Date();

        Procedure job = new Procedure();
        job.setId(jobId);
        job.setStatus(ProcedureStatus.PENDING);
        job.setSurveyId(surveyId);
        job.setYear(year);
        job.setQuarter(quarter);
        job.setMonth(month);
        job.setCreatedAt(crateTime);
        job.setStartedAt(crateTime);

        procedureStatusRepository.save(job);
        procedureStatusRepository.flush();

        asyncProcedureRunner.runProcedureAsync(procedureRequestDto, jobId);

        return mapToProcedureResponseDto(job);
    }

    public ProcedureResponseDto getProcedureInfo(String id) {

        Optional<Procedure> byId = procedureStatusRepository.getById(id);

        if (byId.isPresent()) {

            Procedure procedure = byId.get();

            return mapToProcedureResponseDto(procedure);
        }

        return null;
    }

    private ProcedureResponseDto mapToProcedureResponseDto(Procedure procedure) {

        ProcedureResponseDto responseDto = new ProcedureResponseDto();

        String startedAtStr = procedure.getStartedAt() != null ? sdf.format(procedure.getStartedAt()) : null;
        String finishedAtStr = procedure.getFinishedAt() != null ? sdf.format(procedure.getFinishedAt()) : null;

        responseDto.setJobId(procedure.getId());
        responseDto.setStatus(procedure.getStatus());
        responseDto.setSurveyId(procedure.getSurveyId());
        responseDto.setStartedAt(startedAtStr);
        responseDto.setFinishedAt(finishedAtStr);
        responseDto.setResult(procedure.getResult());
        responseDto.setYear(procedure.getYear());
        responseDto.setQuarter(procedure.getQuarter());
        responseDto.setMonth(procedure.getMonth());
        responseDto.setErrorMessage(procedure.getErrorMessage());

        return responseDto;
    }
}