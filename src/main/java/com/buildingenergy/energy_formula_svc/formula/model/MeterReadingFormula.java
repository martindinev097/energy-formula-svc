package com.buildingenergy.energy_formula_svc.formula.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document("meter_formula")
public class MeterReadingFormula {

    @MongoId(FieldType.STRING)
    private UUID id;

    private BigDecimal pricePerKwh;

    private BigDecimal divider;

    private LocalDateTime createdOn;

    private UUID userId;
}
