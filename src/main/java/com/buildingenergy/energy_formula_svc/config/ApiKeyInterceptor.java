package com.buildingenergy.energy_formula_svc.config;

import com.buildingenergy.energy_formula_svc.exception.ApiKeyMissingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final String API_KEY = "secretKey123";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String apiKey = request.getHeader("SM-API-KEY");

        if (!API_KEY.equals(apiKey)) {
            throw new ApiKeyMissingException("Invalid or missing API key");
        }

        return true;
    }
}
