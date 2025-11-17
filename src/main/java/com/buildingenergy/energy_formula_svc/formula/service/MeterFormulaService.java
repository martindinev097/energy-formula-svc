package com.buildingenergy.energy_formula_svc.formula.service;

import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaRequest;
import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.model.MeterReadingFormula;
import com.buildingenergy.energy_formula_svc.formula.repository.MeterFormulaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MeterFormulaService {

    private final MeterFormulaRepository meterFormulaRepository;

    public MeterFormulaService(MeterFormulaRepository meterFormulaRepository) {
        this.meterFormulaRepository = meterFormulaRepository;
    }

    public MeterFormulaResponse updateMeterFormula(UUID userId, MeterFormulaRequest request) {
        MeterReadingFormula formula = MeterReadingFormula.builder()
                .id(UUID.randomUUID())
                .pricePerKwh(request.getPricePerKwh())
                .divider(request.getDivider())
                .createdOn(LocalDateTime.now())
                .userId(userId)
                .build();

        return toResponse(meterFormulaRepository.save(formula));
    }

    private MeterFormulaResponse toResponse(MeterReadingFormula mrf) {
        return new MeterFormulaResponse(mrf.getPricePerKwh(), mrf.getDivider());
    }

    public MeterFormulaResponse getCurrentFormula(UUID userId) {
        return meterFormulaRepository.findTopByUserIdOrderByCreatedOnDesc(userId).map(this::toResponse).orElseGet(() -> defaultMeterFormula(userId));
    }

    private MeterFormulaResponse defaultMeterFormula(UUID userId) {
        MeterReadingFormula defaultMeterFormula = MeterReadingFormula.builder()
                .id(UUID.randomUUID())
                .pricePerKwh(BigDecimal.valueOf(0.2))
                .divider(BigDecimal.valueOf(2))
                .createdOn(LocalDateTime.now())
                .userId(userId)
                .build();

        return toResponse(meterFormulaRepository.save(defaultMeterFormula));
    }
}
