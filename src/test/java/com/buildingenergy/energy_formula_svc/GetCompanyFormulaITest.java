package com.buildingenergy.energy_formula_svc;

import com.buildingenergy.energy_formula_svc.formula.repository.CompanyFormulaRepository;
import com.buildingenergy.energy_formula_svc.formula.service.CompanyFormulaService;
import com.buildingenergy.energy_formula_svc.web.dto.CompanyFormulaResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class GetCompanyFormulaITest {

    @Autowired
    private CompanyFormulaService companyFormulaService;
    @Autowired
    private CompanyFormulaRepository companyFormulaRepository;

    @Test
    void givenNoFormulaYet_whenInvokeGetCurrentFormula_thenCreateDefaultFormula() {
        UUID userId = UUID.randomUUID();

        CompanyFormulaResponse response = companyFormulaService.getCurrentFormula(userId);

        assertEquals(BigDecimal.valueOf(0.2), response.getPricePerKwh());
        assertEquals(BigDecimal.valueOf(13.5), response.getMultiplier());
        assertEquals(BigDecimal.valueOf(100), response.getDivider());
        assertEquals(1, companyFormulaRepository.countByUserId(userId));
    }

}
