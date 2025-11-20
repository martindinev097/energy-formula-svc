package com.buildingenergy.energy_formula_svc.formula.service;

import com.buildingenergy.energy_formula_svc.web.dto.CompanyFormulaRequest;
import com.buildingenergy.energy_formula_svc.web.dto.CompanyFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.model.CompanyReadingFormula;
import com.buildingenergy.energy_formula_svc.formula.repository.CompanyFormulaRepository;
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
                .id(UUID.randomUUID())
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
                .id(UUID.randomUUID())
                .pricePerKwh(request.getPricePerKwh())
                .multiplier(request.getMultiplier())
                .divider(request.getDivider())
                .createdOn(LocalDateTime.now())
                .userId(userId)
                .build();

        log.info("Company formula updated by user with id: [%s] at: [%s]".formatted(userId, LocalDateTime.now()));

        return toResponse(companyFormulaRepository.save(formula));
    }

    private CompanyFormulaResponse toResponse(CompanyReadingFormula crf) {
        return new CompanyFormulaResponse(crf.getPricePerKwh(), crf.getMultiplier(), crf.getDivider());
    }

    @Scheduled(cron = "0 0 0 1 * *")
    private void cleanupOldFormulas() {
        List<CompanyReadingFormula> allFormulas = companyFormulaRepository.findAll();

        Map<UUID, List<CompanyReadingFormula>> grouped = allFormulas.stream()
                .collect(Collectors.groupingBy(CompanyReadingFormula::getUserId));

        grouped.forEach((userId, formulas) -> formulas.stream()
                .sorted(Comparator.comparing(CompanyReadingFormula::getCreatedOn).reversed())
                .skip(1)
                .forEach(companyFormulaRepository::delete));

        log.info("Running scheduled company formula deletion");
    }
}
