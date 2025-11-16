package com.buildingenergy.energy_formula_svc.web;

import com.buildingenergy.energy_formula_svc.dto.MeterFormulaRequest;
import com.buildingenergy.energy_formula_svc.dto.MeterFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.service.MeterFormulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/meter/formula")
public class MeterFormulaController {

    private final MeterFormulaService meterFormulaService;

    public MeterFormulaController(MeterFormulaService meterFormulaService) {
        this.meterFormulaService = meterFormulaService;
    }

    @GetMapping
    public ResponseEntity<MeterFormulaResponse> getMeterFormula(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(meterFormulaService.getCurrentFormula(userId));
    }

    @PostMapping
    public ResponseEntity<MeterFormulaResponse> updateMeterFormula(@RequestHeader(value = "SM-API-KEY", required = false) String apiKey, @RequestParam("userId") UUID userId, @RequestBody MeterFormulaRequest request) {
        if (!"secretKey123".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        MeterFormulaResponse response = meterFormulaService.updateMeterFormula(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
