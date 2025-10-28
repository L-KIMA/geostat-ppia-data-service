package geostat.repository;

import geostat.domain.FormioExportT2;
import geostat.domain.PpiId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FormioExportT2Repository extends JpaRepository<FormioExportT2, PpiId> {

    @Query("select t from FormioExportT2 t where t.id.surveyId =:surveyId and t.id.year =:year and t.id.month =:month " +
            "and t.id.sid in (:sidCodes)")
    List<FormioExportT2> getData(@Param("surveyId") Long surveyId, @Param("year") Integer year, @Param("month") Integer month,
                                 @Param("sidCodes") Set<String> sidCodes);
}