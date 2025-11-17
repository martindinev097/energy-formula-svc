package com.buildingenergy.energy_formula_svc.formula.repository;

import com.buildingenergy.energy_formula_svc.formula.model.MeterReadingFormula;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeterFormulaRepository extends MongoRepository<MeterReadingFormula, UUID> {

    Optional<MeterReadingFormula> findTopByUserIdOrderByCreatedOnDesc(UUID userId);

}
