package com.buildingenergy.energy_formula_svc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeterFormulaResponse {

    private BigDecimal energyPercentage;
    private BigDecimal pricePerKwh;
    private BigDecimal divider;

}
