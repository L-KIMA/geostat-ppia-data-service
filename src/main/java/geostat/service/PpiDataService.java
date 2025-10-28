package geostat.service;

import geostat.domain.FormioBaseEntity;
import geostat.model.PpiDataResponseDto;
import geostat.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PpiDataService {

    private FormioCaT2Repository formioCaT2Repository;
    private FormioCCIT2Repository formioCCIT2Repository;
    private FormioExportT2Repository formioExportT2Repository;
    private FormioImportT2Repository formioImportT2Repository;
    private FormioMshT2Repository formioMshT2Repository;
    private FormioPpiT2Repository formioPpiT2Repository;
    private FormioTeTt2Repository formioTeTt2Repository;
    private FormioTRNT2Repository formioTRNT2Repository;

    public List<PpiDataResponseDto> getPpiData(Long surveyId, Integer year, Integer month, Set<String> sidCodes) {

        List<? extends Object> entities = Collections.emptyList();
        if (surveyId.equals(12L)) {

            entities = formioMshT2Repository.getData(surveyId, year, month, sidCodes);

        } else if (surveyId.equals(22L)) {

            entities = formioExportT2Repository.getData(surveyId, year, month, sidCodes);

        } else if (surveyId.equals(23L)) {

            entities = formioPpiT2Repository.getData(surveyId, year, month, sidCodes);

        } else if (surveyId.equals(32L)) {

            entities = formioCaT2Repository.getData(surveyId, year, month, sidCodes);

        } else if (surveyId.equals(35L)) {

            entities = formioTeTt2Repository.getData(surveyId, year, month, sidCodes);

        } else if (surveyId.equals(39L)) {

            entities = formioCCIT2Repository.getData(surveyId, year, month, sidCodes);

        } else if (surveyId.equals(44L)) {

            entities = formioImportT2Repository.getData(surveyId, year, month, sidCodes);

        } else if (surveyId.equals(47L)) {

            entities = formioTRNT2Repository.getData(surveyId, year, month, sidCodes);
        }

        return entities.stream().map(e -> mapToDto((FormioBaseEntity) e)).collect(Collectors.toList());
    }

    private PpiDataResponseDto mapToDto(FormioBaseEntity entity) {

        PpiDataResponseDto dto = new PpiDataResponseDto();
        dto.setSidCode(entity.getId().getSid());
        dto.setData(entity.getData());

        return dto;
    }

    @Autowired
    public void setFormioCaT2Repository(FormioCaT2Repository formioCaT2Repository) {
        this.formioCaT2Repository = formioCaT2Repository;
    }

    @Autowired
    public void setFormioCCIT2Repository(FormioCCIT2Repository formioCCIT2Repository) {
        this.formioCCIT2Repository = formioCCIT2Repository;
    }

    @Autowired
    public void setFormioExportT2Repository(FormioExportT2Repository formioExportT2Repository) {
        this.formioExportT2Repository = formioExportT2Repository;
    }

    @Autowired
    public void setFormioImportT2Repository(FormioImportT2Repository formioImportT2Repository) {
        this.formioImportT2Repository = formioImportT2Repository;
    }

    @Autowired
    public void setFormioMshT2Repository(FormioMshT2Repository formioMshT2Repository) {
        this.formioMshT2Repository = formioMshT2Repository;
    }

    @Autowired
    public void setFormioPpiT2Repository(FormioPpiT2Repository formioPpiT2Repository) {
        this.formioPpiT2Repository = formioPpiT2Repository;
    }

    @Autowired
    public void setFormioTeTt2Repository(FormioTeTt2Repository formioTeTt2Repository) {
        this.formioTeTt2Repository = formioTeTt2Repository;
    }

    @Autowired
    public void setFormioTRNT2Repository(FormioTRNT2Repository formioTRNT2Repository) {
        this.formioTRNT2Repository = formioTRNT2Repository;
    }
}
