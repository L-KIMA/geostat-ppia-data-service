package geostat.repository.sync;

import geostat.domain.sync.SyncJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SyncJobRepository extends JpaRepository<SyncJob, UUID> {

//    Optional<SyncJob> findBySurveyIdAndYearAndQuarterAndStatusIn(Integer year, Integer quarter, List<String> statuses);

    Optional<SyncJob> getById(UUID uuid);
}
