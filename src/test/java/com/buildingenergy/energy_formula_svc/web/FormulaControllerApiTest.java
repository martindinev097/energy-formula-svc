package com.buildingenergy.energy_formula_svc.web;

import com.buildingenergy.energy_formula_svc.formula.service.CompanyFormulaService;
import com.buildingenergy.energy_formula_svc.formula.service.MeterFormulaService;
import com.buildingenergy.energy_formula_svc.web.dto.CompanyFormulaRequest;
import com.buildingenergy.energy_formula_svc.web.dto.CompanyFormulaResponse;
import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaRequest;
import com.buildingenergy.energy_formula_svc.web.dto.MeterFormulaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FormulaController.class)
public class FormulaControllerApiTest {

    @MockitoBean
    private CompanyFormulaService companyFormulaService;
    @MockitoBean
    private MeterFormulaService meterFormulaService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void putUpdateCompanyFormula_shouldInvokeUpdateFormulaAndReturnStatusCreatedWithCompanyFormulaResponse() throws Exception {
        UUID userId = UUID.randomUUID();

        CompanyFormulaRequest dto = new CompanyFormulaRequest(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
        CompanyFormulaResponse response = new CompanyFormulaResponse(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);

        when(companyFormulaService.updateFormula(any(), any())).thenReturn(response);

        MockHttpServletRequestBuilder httpRequest = put("/api/v1/company/formula")
                .header("SM-API-KEY", "secretKey123")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(dto));

        mockMvc.perform(httpRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("pricePerKwh").isNotEmpty())
                .andExpect(jsonPath("multiplier").isNotEmpty())
                .andExpect(jsonPath("divider").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(companyFormulaService).updateFormula(any(), any());
    }

    @Test
    void putUpdateMeterFormula_shouldInvokeUpdateMeterFormulaAndReturnStatusCreatedWithMeterFormulaResponse() throws Exception {
        UUID userId = UUID.randomUUID();

        MeterFormulaRequest dto = new MeterFormulaRequest(BigDecimal.ONE, BigDecimal.ONE);
        MeterFormulaResponse response = new MeterFormulaResponse(BigDecimal.ONE, BigDecimal.ONE);

        when(meterFormulaService.updateMeterFormula(any(), any())).thenReturn(response);

        MockHttpServletRequestBuilder httpRequest = put("/api/v1/meter/formula")
                .header("SM-API-KEY", "secretKey123")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(dto));

        mockMvc.perform(httpRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("pricePerKwh").isNotEmpty())
                .andExpect(jsonPath("divider").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(meterFormulaService).updateMeterFormula(any(), any());
    }

    @Test
    void putUpdateMeterFormulaWithMissingApiKeyHeader_shouldThrowApiKeyMissingExceptionAndReturnStatusForbidden() throws Exception {
        UUID userId = UUID.randomUUID();

        MeterFormulaRequest dto = new MeterFormulaRequest(BigDecimal.ONE, BigDecimal.ONE);
        MeterFormulaResponse response = new MeterFormulaResponse(BigDecimal.ONE, BigDecimal.ONE);

        when(meterFormulaService.updateMeterFormula(any(), any())).thenReturn(response);

        MockHttpServletRequestBuilder httpRequest = put("/api/v1/meter/formula")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(dto));

        mockMvc.perform(httpRequest)
                .andExpect(status().isForbidden());

        verify(meterFormulaService, never()).updateMeterFormula(any(), any());
    }

    @Test
    void putUpdateMeterFormulaWithEmptyDto_shouldThrowExceptionAndReturnStatusInternalServerError() throws Exception {
        UUID userId = UUID.randomUUID();

        MeterFormulaRequest dto = new MeterFormulaRequest();

        when(meterFormulaService.updateMeterFormula(any(), any())).thenThrow(new RuntimeException("Unexpected failure."));

        MockHttpServletRequestBuilder httpRequest = put("/api/v1/meter/formula")
                .header("SM-API-KEY", "secretKey123")
                .param("userId", userId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(dto));

        mockMvc.perform(httpRequest)
                .andExpect(status().isInternalServerError());

        verify(meterFormulaService).updateMeterFormula(any(), any());
    }

}
