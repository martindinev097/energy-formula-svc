package com.buildingenergy.energy_formula_svc.formula.service;

import com.buildingenergy.energy_formula_svc.dto.CompanyFormulaRequest;
import com.buildingenergy.energy_formula_svc.dto.CompanyFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.model.CompanyReadingFormula;
import com.buildingenergy.energy_formula_svc.formula.repository.FormulaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CompanyFormulaService {

    private final FormulaRepository formulaRepository;

    public CompanyFormulaService(FormulaRepository formulaRepository) {
        this.formulaRepository = formulaRepository;
    }

    public CompanyFormulaResponse getCurrentFormula(UUID userId) {
        return formulaRepository.findTopByUserIdOrderByCreatedOnDesc(userId).map(this::toResponse).orElseGet(() -> defaultFormula(userId));
    }

    private CompanyFormulaResponse defaultFormula(UUID userId) {
        CompanyReadingFormula defaultFormula = CompanyReadingFormula.builder()
                .pricePerKwh(BigDecimal.valueOf(0.2))
                .multiplier(BigDecimal.valueOf(13.5))
                .divider(BigDecimal.valueOf(100))
                .createdOn(LocalDateTime.now())
                .userId(userId)
                .build();

        return toResponse(formulaRepository.save(defaultFormula));
    }

    public CompanyFormulaResponse updateFormula(UUID userId, CompanyFormulaRequest request) {
        CompanyReadingFormula formula = CompanyReadingFormula.builder()
                .pricePerKwh(request.getPricePerKwh())
                .multiplier(request.getMultiplier())
                .divider(request.getDivider())
                .createdOn(LocalDateTime.now())
                .userId(userId)
                .build();

        return toResponse(formulaRepository.save(formula));
    }

    public BigDecimal calculateCost(UUID userId, BigDecimal differenceReadings) {
        CompanyFormulaResponse currentFormula = getCurrentFormula(userId);

        BigDecimal cost = differenceReadings.multiply(currentFormula.getPricePerKwh()).multiply(currentFormula.getMultiplier());

        return cost.divide(currentFormula.getDivider(), 2, RoundingMode.HALF_UP);
    }

    private CompanyFormulaResponse toResponse(CompanyReadingFormula fs) {
        return new CompanyFormulaResponse(fs.getPricePerKwh(), fs.getMultiplier(), fs.getDivider());
    }
}
