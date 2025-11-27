package com.buildingenergy.energy_formula_svc.config;

import com.buildingenergy.energy_formula_svc.exception.ApiKeyMissingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApiKeyInterceptorUTest {

    @Mock
    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    @Mock
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    @InjectMocks
    private ApiKeyInterceptor interceptor;

    @Test
    void givenValidApiKey_whenInterceptorInAction_thenReturnTrue() throws Exception {
        when(request.getHeader("SM-API-KEY")).thenReturn("secretKey123");

        boolean result = interceptor.preHandle(request, response, new Object());

        assertTrue(result);
    }

    @Test
    void givenInvalidApiKey_whenInterceptorInAction_thenThrowApiKeyMissingException() {
        when(request.getHeader("SM-API-KEY")).thenReturn("wrongKey");

        assertThrows(ApiKeyMissingException.class, () -> interceptor.preHandle(request, response, new Object()));
    }
}
