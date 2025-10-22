package br.rocha.estocai.exceptions;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler{

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        String jsonResponse = String.format(
            "{\"status\": 403, \"error\": \"Forbidden\", \"message\": \"You do not have permission ('%s') to access this resource.\", \"path\": \"%s\"}",
            accessDeniedException.getMessage(),
            request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
    }

    
    
}
