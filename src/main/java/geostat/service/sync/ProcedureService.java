package geostat.service.sync;

import geostat.domain.sync.SyncJob;
import geostat.repository.sync.SyncJobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class ProcedureService {

    private final SyncJobRepository syncJobRepository;
    private final AsyncProcedureRunner asyncProcedureRunner;

    public ProcedureService(SyncJobRepository syncJobRepository, AsyncProcedureRunner asyncProcedureRunner) {
        this.syncJobRepository = syncJobRepository;
        this.asyncProcedureRunner = asyncProcedureRunner;
    }

    @Transactional
    public UUID startSync(Integer surveyId, Integer year, Integer quarter) {

        System.out.println("startSync thread: {}  " + Thread.currentThread().getName());

        UUID jobId = UUID.randomUUID();

        SyncJob job = new SyncJob();
        job.setId(jobId);
        job.setStatus("PENDING");
        job.setSurveyId(surveyId);
        job.setYear(year);
        job.setQuarter(quarter);
        job.setCreatedAt(new Date());

        syncJobRepository.save(job);

        asyncProcedureRunner.runProcedureAsync(jobId, surveyId, year, quarter);


        System.out.println(job + "  =============================================================");

        return jobId;
    }
}