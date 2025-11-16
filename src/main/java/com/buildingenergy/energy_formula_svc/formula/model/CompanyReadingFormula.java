package com.buildingenergy.energy_formula_svc.formula.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "company_formula")
public class CompanyReadingFormula {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal pricePerKwh;

    @Column(nullable = false)
    private BigDecimal multiplier;

    @Column(nullable = false)
    private BigDecimal divider;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private UUID userId;
}
