package com.buildingenergy.energy_formula_svc.formula.service;

import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaRequest;
import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.model.MeterReadingFormula;
import com.buildingenergy.energy_formula_svc.formula.repository.MeterFormulaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
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

        log.info("Meter formula updated by user with id: [%s] at: [%s]".formatted(userId, LocalDateTime.now()));

        return toResponse(meterFormulaRepository.save(formula));
    }

    public MeterFormulaResponse getCurrentFormula(UUID userId) {
        return meterFormulaRepository.findTopByUserIdOrderByCreatedOnDesc(userId).map(this::toResponse).orElseGet(() -> defaultMeterFormula(userId));
    }

    @Scheduled(cron = "0 0 0 1 * *")
    public void cleanupOldFormulas() {
        List<MeterReadingFormula> allFormulas = meterFormulaRepository.findAll();

        Map<UUID, List<MeterReadingFormula>> grouped = allFormulas.stream()
                .collect(Collectors.groupingBy(MeterReadingFormula::getUserId));

        grouped.values().forEach(this::cleanup);
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

    private MeterFormulaResponse toResponse(MeterReadingFormula mrf) {
        return new MeterFormulaResponse(mrf.getPricePerKwh(), mrf.getDivider());
    }

    private void cleanup(List<MeterReadingFormula> formulas) {
        if (formulas.size() <= 1) {
            return;
        }

        formulas.stream()
                .sorted(Comparator.comparing(MeterReadingFormula::getCreatedOn).reversed())
                .skip(1)
                .forEach(meterFormulaRepository::delete);

        log.info("Running scheduled meter formula deletion");
    }
}
