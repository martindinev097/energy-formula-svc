package com.buildingenergy.energy_formula_svc.web;

import com.buildingenergy.energy_formula_svc.dto.CompanyFormulaRequest;
import com.buildingenergy.energy_formula_svc.dto.CompanyFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.service.CompanyFormulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/company/formula")
public class CompanyFormulaController {

    private final CompanyFormulaService companyFormulaService;

    public CompanyFormulaController(CompanyFormulaService companyFormulaService) {
        this.companyFormulaService = companyFormulaService;
    }

    @GetMapping
    public ResponseEntity<CompanyFormulaResponse> getFormula(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(companyFormulaService.getCurrentFormula(userId));
    }

    @PostMapping
    public ResponseEntity<CompanyFormulaResponse> updateFormula(@RequestHeader(value = "SM-API-KEY", required = false) String apiKey, @RequestParam("userId") UUID userId, @RequestBody CompanyFormulaRequest request) {
        if (!"secretKey123".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        CompanyFormulaResponse response = companyFormulaService.updateFormula(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
