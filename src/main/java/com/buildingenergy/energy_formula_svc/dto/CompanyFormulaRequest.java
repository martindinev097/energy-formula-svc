package com.buildingenergy.energy_formula_svc.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompanyFormulaRequest {

    private BigDecimal pricePerKwh;
    private BigDecimal multiplier;
    private BigDecimal divider;

}
