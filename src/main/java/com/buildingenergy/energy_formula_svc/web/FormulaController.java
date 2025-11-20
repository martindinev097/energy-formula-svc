package com.buildingenergy.energy_formula_svc.web;

import com.buildingenergy.energy_formula_svc.formula.service.CompanyFormulaService;
import com.buildingenergy.energy_formula_svc.web.dto.CompanyFormulaRequest;
import com.buildingenergy.energy_formula_svc.web.dto.CompanyFormulaResponse;
import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaRequest;
import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.service.MeterFormulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class FormulaController {

    private final CompanyFormulaService companyFormulaService;
    private final MeterFormulaService meterFormulaService;

    public FormulaController(CompanyFormulaService companyFormulaService, MeterFormulaService meterFormulaService) {
        this.companyFormulaService = companyFormulaService;
        this.meterFormulaService = meterFormulaService;
    }

    @GetMapping("/company/formula")
    public ResponseEntity<CompanyFormulaResponse> getCompanyFormula(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(companyFormulaService.getCurrentFormula(userId));
    }

    @PutMapping("/company/formula")
    public ResponseEntity<CompanyFormulaResponse> updateCompanyFormula(@RequestParam("userId") UUID userId, @RequestBody CompanyFormulaRequest request) {
        CompanyFormulaResponse response = companyFormulaService.updateFormula(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/meter/formula")
    public ResponseEntity<MeterFormulaResponse> getMeterFormula(@RequestParam("userId") UUID userId) {
        return ResponseEntity.ok(meterFormulaService.getCurrentFormula(userId));
    }

    @PutMapping("/meter/formula")
    public ResponseEntity<MeterFormulaResponse> updateMeterFormula(@RequestParam("userId") UUID userId, @RequestBody MeterFormulaRequest request) {
        MeterFormulaResponse response = meterFormulaService.updateMeterFormula(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

}
