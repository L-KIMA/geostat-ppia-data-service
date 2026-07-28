package geostat.controller;

import geostat.model.procedure.ProcedureRequestDto;
import geostat.model.procedure.ProcedureResponseDto;
import geostat.service.procedure.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/procedure")
public class ProcedureController {

    private ProcedureService procedureService;

    @PostMapping("/call")
    public ResponseEntity<ProcedureResponseDto> callProcedure(@RequestBody @Validated ProcedureRequestDto procedureRequestDto) {

        ProcedureResponseDto procedureResponseDto = procedureService.startSync(procedureRequestDto);

        return new ResponseEntity<>(procedureResponseDto, HttpStatus.OK);
    }

    @GetMapping("/info")
    public ResponseEntity<ProcedureResponseDto> getProcedureInfo(@RequestParam String id) {

        ProcedureResponseDto procedureResponseDto = procedureService.getProcedureInfo(id);

        return new ResponseEntity<>(procedureResponseDto, HttpStatus.OK);
    }

    @Autowired
    public void setProcedureService(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }
}
