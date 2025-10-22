package br.rocha.estocai.exceptions;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String jsonResponse = String.format(
            "{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"You must be logged in or provide a valid JWT token to access '%s'.\", \"path\": \"%s\"}",
            request.getRequestURI(),
            request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
    }
    
}
