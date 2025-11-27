package com.buildingenergy.energy_formula_svc.formula;

import com.buildingenergy.energy_formula_svc.formula.model.MeterReadingFormula;
import com.buildingenergy.energy_formula_svc.formula.repository.MeterFormulaRepository;
import com.buildingenergy.energy_formula_svc.formula.service.MeterFormulaService;
import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaRequest;
import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeterFormulaUTest {

    @Mock
    private MeterFormulaRepository meterFormulaRepository;

    @InjectMocks
    private MeterFormulaService meterFormulaService;

    @Test
    void givenUserWithoutFormula_whenGetCurrentFormula_thenDefaultFormulaIsCreatedAndReturned() {
        UUID userId = UUID.randomUUID();

        when(meterFormulaRepository.findTopByUserIdOrderByCreatedOnDesc(userId)).thenReturn(Optional.empty());
        when(meterFormulaRepository.save(any(MeterReadingFormula.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MeterFormulaResponse result = meterFormulaService.getCurrentFormula(userId);

        assertEquals(BigDecimal.valueOf(0.2), result.getPricePerKwh());
        assertEquals(BigDecimal.valueOf(2), result.getDivider());

        verify(meterFormulaRepository, times(1)).save(any(MeterReadingFormula.class));
    }

    @Test
    void givenMeterFormulaRequestForUser_whenUpdateMeterFormulaIsCalled_thenReturnTheUpdatedValues() {
        UUID userId = UUID.randomUUID();
        MeterFormulaRequest request = new MeterFormulaRequest(BigDecimal.valueOf(100), BigDecimal.valueOf(50));

        when(meterFormulaRepository.save(any(MeterReadingFormula.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MeterFormulaResponse response = meterFormulaService.updateMeterFormula(userId, request);

        assertEquals(BigDecimal.valueOf(100), response.getPricePerKwh());
        assertEquals(BigDecimal.valueOf(50), response.getDivider());
        assertNotNull(response);

        verify(meterFormulaRepository, times(1)).save(any(MeterReadingFormula.class));
    }

    @Test
    void givenFormulas_whenCleanupOldFormulasCalled_thenKeepTheMostRecentOne() {
        UUID user = UUID.randomUUID();
        UUID user1 = UUID.randomUUID();

        MeterReadingFormula f1 = MeterReadingFormula.builder()
                .id(UUID.randomUUID())
                .userId(user)
                .createdOn(LocalDateTime.now().minusDays(5))
                .build();

        MeterReadingFormula f2 = MeterReadingFormula.builder()
                .id(UUID.randomUUID())
                .userId(user)
                .createdOn(LocalDateTime.now().minusDays(2))
                .build();

        MeterReadingFormula f3 = MeterReadingFormula.builder()
                .id(UUID.randomUUID())
                .userId(user)
                .createdOn(LocalDateTime.now().minusDays(1))
                .build();

        MeterReadingFormula a1 = MeterReadingFormula.builder()
                .id(UUID.randomUUID())
                .userId(user1)
                .createdOn(LocalDateTime.now().minusDays(10))
                .build();

        MeterReadingFormula a2 = MeterReadingFormula.builder()
                .id(UUID.randomUUID())
                .userId(user1)
                .createdOn(LocalDateTime.now().minusDays(5))
                .build();

        when(meterFormulaRepository.findAll()).thenReturn(List.of(f1, f2, f3, a1, a2));

        meterFormulaService.cleanupOldFormulas();

        verify(meterFormulaRepository, times(1)).delete(f1);
        verify(meterFormulaRepository, times(1)).delete(f2);
        verify(meterFormulaRepository, times(1)).delete(a1);

        verify(meterFormulaRepository, never()).delete(f3);
        verify(meterFormulaRepository, never()).delete(a2);
    }

    @Test
    void givenOnlyOneFormula_whenCleanupOldFormulasCalled_thenDeleteDoesNotHappen() {
        UUID user = UUID.randomUUID();

        MeterReadingFormula f1 = MeterReadingFormula.builder()
                .id(UUID.randomUUID())
                .userId(user)
                .createdOn(LocalDateTime.now().minusDays(5))
                .build();

        when(meterFormulaRepository.findAll()).thenReturn(List.of(f1));

        meterFormulaService.cleanupOldFormulas();

        verify(meterFormulaRepository, never()).delete(f1);
    }

}
