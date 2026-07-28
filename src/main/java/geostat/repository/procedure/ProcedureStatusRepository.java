package geostat.repository.procedure;

import geostat.domain.procedure.Procedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcedureStatusRepository extends JpaRepository<Procedure, String> {

    Optional<Procedure> getById(String uuid);
}
