package com.buildingenergy.energy_formula_svc.web;

import com.buildingenergy.energy_formula_svc.dto.CompanyFormulaRequest;
import com.buildingenergy.energy_formula_svc.dto.CompanyFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.service.CompanyFormulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/formula")
public class FormulaController {

    private final CompanyFormulaService formulaService;

    public FormulaController(CompanyFormulaService formulaService) {
        this.formulaService = formulaService;
    }

    @GetMapping
    public ResponseEntity<CompanyFormulaResponse> getFormula(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(formulaService.getCurrentFormula(userId));
    }

    @PostMapping
    public ResponseEntity<CompanyFormulaResponse> updateFormula(@RequestHeader(value = "SM-API-KEY", required = false) String apiKey, @RequestParam("userId") UUID userId, @RequestBody CompanyFormulaRequest request) {
        if (!"secretKey123".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CompanyFormulaResponse response = formulaService.updateFormula(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/cost")
    public BigDecimal calculate(@RequestParam("userId") UUID userId, @RequestParam BigDecimal differenceReadings) {
        return formulaService.calculateCost(userId, differenceReadings);
    }

}
