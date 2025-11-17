package com.buildingenergy.energy_formula_svc.formula.repository;

import com.buildingenergy.energy_formula_svc.formula.model.CompanyReadingFormula;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyFormulaRepository extends MongoRepository<CompanyReadingFormula, UUID> {

    Optional<CompanyReadingFormula> findTopByUserIdOrderByCreatedOnDesc(UUID userId);

}
