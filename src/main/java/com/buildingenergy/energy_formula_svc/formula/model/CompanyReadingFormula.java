package com.buildingenergy.energy_formula_svc.formula.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "formula_settings")
public class CompanyReadingFormula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal pricePerKwh;

    private BigDecimal multiplier;

    private BigDecimal divider;

    private LocalDateTime createdOn;

    private UUID userId;

}
