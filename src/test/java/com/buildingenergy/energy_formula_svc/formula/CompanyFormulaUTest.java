package com.buildingenergy.energy_formula_svc.formula;

import com.buildingenergy.energy_formula_svc.formula.model.CompanyReadingFormula;
import com.buildingenergy.energy_formula_svc.formula.repository.CompanyFormulaRepository;
import com.buildingenergy.energy_formula_svc.formula.service.CompanyFormulaService;
import com.buildingenergy.energy_formula_svc.web.dto.CompanyFormulaRequest;
import com.buildingenergy.energy_formula_svc.web.dto.CompanyFormulaResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyFormulaUTest {

    @Mock
    private CompanyFormulaRepository companyFormulaRepository;

    @InjectMocks
    private CompanyFormulaService companyFormulaService;

    @Test
    void givenUserWithoutFormula_whenGetCurrentFormula_thenDefaultFormulaIsCreatedAndReturned() {
        UUID userId = UUID.randomUUID();

        when(companyFormulaRepository.findTopByUserIdOrderByCreatedOnDesc(userId)).thenReturn(Optional.empty());
        when(companyFormulaRepository.save(any(CompanyReadingFormula.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompanyFormulaResponse formula = companyFormulaService.getCurrentFormula(userId);

        assertEquals(BigDecimal.valueOf(0.2), formula.getPricePerKwh());
        assertEquals(BigDecimal.valueOf(13.5), formula.getMultiplier());
        assertEquals(BigDecimal.valueOf(100), formula.getDivider());

        verify(companyFormulaRepository, times(1)).save(any(CompanyReadingFormula.class));
    }

    @Test
    void givenCompanyFormulaRequestForUser_whenUpdateFormulaIsCalled_thenReturnTheNewFormulaValues() {
        UUID userId = UUID.randomUUID();

        CompanyFormulaRequest request = new CompanyFormulaRequest(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.ONE);

        when(companyFormulaRepository.save(any(CompanyReadingFormula.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompanyFormulaResponse result = companyFormulaService.updateFormula(userId, request);

        assertNotNull(result);
        assertEquals(BigDecimal.TEN, result.getPricePerKwh());
        assertEquals(BigDecimal.TEN, result.getMultiplier());
        assertEquals(BigDecimal.ONE, result.getDivider());

        verify(companyFormulaRepository, times(1)).save(any(CompanyReadingFormula.class));
    }

    @Test
    void givenFormulasForUser_whenCleanupOldFormulasIsCalled_thenKeepOnlyTheLastOne() {
        UUID user = UUID.randomUUID();
        UUID user1 = UUID.randomUUID();

        CompanyReadingFormula f1 = CompanyReadingFormula.builder()
                .id(UUID.randomUUID())
                .pricePerKwh(BigDecimal.ONE)
                .multiplier(BigDecimal.valueOf(15))
                .divider(BigDecimal.valueOf(25))
                .createdOn(LocalDateTime.now().minusDays(10))
                .userId(user)
                .build();

        CompanyReadingFormula f2 = CompanyReadingFormula.builder()
                .id(UUID.randomUUID())
                .pricePerKwh(BigDecimal.TEN)
                .multiplier(BigDecimal.valueOf(123))
                .divider(BigDecimal.valueOf(321))
                .createdOn(LocalDateTime.now().minusDays(5))
                .userId(user)
                .build();

        CompanyReadingFormula a1 = CompanyReadingFormula.builder()
                .id(UUID.randomUUID())
                .pricePerKwh(BigDecimal.valueOf(21))
                .multiplier(BigDecimal.valueOf(15))
                .divider(BigDecimal.valueOf(25))
                .createdOn(LocalDateTime.now().minusDays(4))
                .userId(user1)
                .build();

        CompanyReadingFormula a2 = CompanyReadingFormula.builder()
                .id(UUID.randomUUID())
                .pricePerKwh(BigDecimal.valueOf(33))
                .multiplier(BigDecimal.valueOf(32))
                .divider(BigDecimal.valueOf(50))
                .createdOn(LocalDateTime.now().minusDays(3))
                .userId(user1)
                .build();

        when(companyFormulaRepository.findAll()).thenReturn(List.of(f1, f2, a1, a2));

        companyFormulaService.cleanupOldFormulas();

        verify(companyFormulaRepository, times(1)).delete(f1);
        verify(companyFormulaRepository, times(1)).delete(a1);

        verify(companyFormulaRepository, never()).delete(f2);
        verify(companyFormulaRepository, never()).delete(a2);
    }

    @Test
    void givenOnlyOneFormulaForUser_whenCleanupOldFormulasIsCalled_thenDoNotDelete() {
        UUID user = UUID.randomUUID();

        CompanyReadingFormula f1 = CompanyReadingFormula.builder()
                .id(UUID.randomUUID())
                .pricePerKwh(BigDecimal.ONE)
                .multiplier(BigDecimal.valueOf(15))
                .divider(BigDecimal.valueOf(25))
                .createdOn(LocalDateTime.now().minusDays(10))
                .userId(user)
                .build();

        when(companyFormulaRepository.findAll()).thenReturn(List.of(f1));

        companyFormulaService.cleanupOldFormulas();

        verify(companyFormulaRepository, never()).delete(f1);
    }

}
