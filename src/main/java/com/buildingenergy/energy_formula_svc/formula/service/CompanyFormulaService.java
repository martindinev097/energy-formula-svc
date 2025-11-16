package com.buildingenergy.energy_formula_svc.formula.service;

import com.buildingenergy.energy_formula_svc.dto.CompanyFormulaRequest;
import com.buildingenergy.energy_formula_svc.dto.CompanyFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.model.CompanyReadingFormula;
import com.buildingenergy.energy_formula_svc.formula.repository.CompanyFormulaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CompanyFormulaService {

    private final CompanyFormulaRepository companyFormulaRepository;

    public CompanyFormulaService(CompanyFormulaRepository companyFormulaRepository) {
        this.companyFormulaRepository = companyFormulaRepository;
    }

    public CompanyFormulaResponse getCurrentFormula(UUID userId) {
        return companyFormulaRepository.findTopByUserIdOrderByCreatedOnDesc(userId).map(this::toResponse).orElseGet(() -> defaultFormula(userId));
    }

    private CompanyFormulaResponse defaultFormula(UUID userId) {
        CompanyReadingFormula defaultFormula = CompanyReadingFormula.builder()
                .pricePerKwh(BigDecimal.valueOf(0.2))
                .multiplier(BigDecimal.valueOf(13.5))
                .divider(BigDecimal.valueOf(100))
                .createdOn(LocalDateTime.now())
                .userId(userId)
                .build();

        return toResponse(companyFormulaRepository.save(defaultFormula));
    }

    public CompanyFormulaResponse updateFormula(UUID userId, CompanyFormulaRequest request) {
        CompanyReadingFormula formula = CompanyReadingFormula.builder()
                .pricePerKwh(request.getPricePerKwh())
                .multiplier(request.getMultiplier())
                .divider(request.getDivider())
                .createdOn(LocalDateTime.now())
                .userId(userId)
                .build();

        return toResponse(companyFormulaRepository.save(formula));
    }

    private CompanyFormulaResponse toResponse(CompanyReadingFormula crf) {
        return new CompanyFormulaResponse(crf.getPricePerKwh(), crf.getMultiplier(), crf.getDivider());
    }
}
