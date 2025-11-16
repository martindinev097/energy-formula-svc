package com.buildingenergy.energy_formula_svc.formula.repository;

import com.buildingenergy.energy_formula_svc.dto.MeterFormulaResponse;
import com.buildingenergy.energy_formula_svc.formula.model.MeterReadingFormula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeterFormulaRepository extends JpaRepository<MeterReadingFormula, UUID> {

    Optional<MeterFormulaResponse> findTopByUserIdOrderByCreatedOnDesc(UUID userId);

}
