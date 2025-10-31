package geostat.controller;

import geostat.model.PpiDataRequestDto;
import geostat.model.PpiDataResponseDto;
import geostat.service.PpiDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/ppi")
public class PpiDataController {

    private PpiDataService ppiDataService;

    @PostMapping("/getData")
    public List<PpiDataResponseDto> getData(@Valid @RequestBody PpiDataRequestDto ppiDataRequestDto) {

        Long surveyId = ppiDataRequestDto.getSurveyId();
        Integer year = ppiDataRequestDto.getYear();
        Integer month = ppiDataRequestDto.getMonth();
        Set<String> sidCodes = ppiDataRequestDto.getSidCodes();

        return ppiDataService.getPpiData(surveyId, year, month, sidCodes);
    }

    @Autowired
    public void setPpiDataService(PpiDataService ppiDataService) {
        this.ppiDataService = ppiDataService;
    }
}
