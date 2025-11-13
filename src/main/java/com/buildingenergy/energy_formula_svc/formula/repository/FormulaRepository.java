package com.buildingenergy.energy_formula_svc.formula.repository;

import com.buildingenergy.energy_formula_svc.formula.model.CompanyReadingFormula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormulaRepository extends JpaRepository<CompanyReadingFormula, UUID> {

    Optional<CompanyReadingFormula> findTopByUserIdOrderByCreatedOnDesc(UUID userId);

}
