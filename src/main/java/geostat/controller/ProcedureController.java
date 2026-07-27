package geostat.controller;

import geostat.service.sync.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/procedure")
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;

    @GetMapping("/call")
    public ResponseEntity<Map<String, Object>> callTestProcedure(@RequestParam Integer surveyId,
                                                                 @RequestParam Integer year,
                                                                 @RequestParam Integer quarter) {

        UUID jobId = procedureService.startSync(surveyId, year, quarter);

        Map<String, Object> body = new HashMap<>();
        body.put("jobId", jobId);
        body.put("status", "PENDING");

        return ResponseEntity.accepted().body(body);
    }
}
