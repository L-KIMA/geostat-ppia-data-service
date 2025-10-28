package geostat.repository;

import geostat.domain.FormioCCIT2;
import geostat.domain.PpiId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FormioCCIT2Repository extends JpaRepository<FormioCCIT2, PpiId> {

    @Query("select t from FormioCCIT2 t where t.id.surveyId =:surveyId and t.id.year =:year and t.id.month =:month " +
            "and t.id.sid in (:sidCodes)")
    List<FormioCCIT2> getData(@Param("surveyId") Long surveyId, @Param("year") Integer year, @Param("month") Integer month,
                              @Param("sidCodes") Set<String> sidCodes);
}