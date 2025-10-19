package com.vdtry06.partner_management.source.server.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class AuthExceptionsHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private final MessageSourceHelper messageSourceHelper;

    private String getUnauthorizedMessage() {
        return messageSourceHelper.getMessage("warning.unauthorized");
    }

    private String getForbiddenMessage() {
        return messageSourceHelper.getMessage("error.access.forbidden_general");
    }

    /* 401 unauthorized */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = getUnauthorizedMessage();
        String detailMessage = messageSourceHelper.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials");
        String moreInformation = "http://localhost:8080/exception/401";

        ApiResponse<String> errorResponse = new ApiResponse<>(false, message, detailMessage, HttpStatus.UNAUTHORIZED, moreInformation);
        log.error(detailMessage, authException);

        // convert object to json
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(errorResponse);

        // return json
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

    /* 403 Forbidden */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String message = getForbiddenMessage();
        String detailMessage = messageSourceHelper.getMessage("error.access.forbidden_detail");
        String moreInformation = "http://localhost:8080/exception/403";

        ApiResponse<String> errorResponse = new ApiResponse<>(false, message, detailMessage, HttpStatus.FORBIDDEN, moreInformation);
        log.error(detailMessage, accessDeniedException);

        // conver object to json
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(errorResponse);

        // return json
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}
