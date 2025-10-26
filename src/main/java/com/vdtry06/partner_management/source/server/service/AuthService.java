package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.enumerated.Language;
import com.vdtry06.partner_management.lib.exceptions.BadRequestException;
import com.vdtry06.partner_management.source.server.config.language.LanguageContext;
import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import com.vdtry06.partner_management.source.server.config.security.JwtProvider;
import com.vdtry06.partner_management.source.server.entities.BlacklistedToken;
import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.payload.auth.LoginRequest;
import com.vdtry06.partner_management.source.server.payload.auth.LoginResponse;
import com.vdtry06.partner_management.source.server.payload.auth.LogoutRequest;
import com.vdtry06.partner_management.source.server.payload.auth.TokenResponse;
import com.vdtry06.partner_management.source.server.repositories.BlacklistedTokenRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Service
@Log4j2
public class AuthService {
    private final EmployeeService employeeService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final MessageSourceHelper messageSourceHelper;

    public AuthService(EmployeeService employeeService, AuthenticationManager authenticationManager, JwtProvider jwtProvider, BlacklistedTokenRepository blacklistedTokenRepository, MessageSourceHelper messageSourceHelper) {
        this.employeeService = employeeService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
        this.messageSourceHelper = messageSourceHelper;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Attempting login for user: {}, Locale: {}", loginRequest.getUsername(), LanguageContext.getLocale());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Employee employee = employeeService.findByUsername(loginRequest.getUsername());
        if (employee == null) {
            throw new BadRequestException(messageSourceHelper.getMessage("error.user_not_found"));
        }

        final String accessToken = jwtProvider.generateToken(employee.getUsername());
        final String refreshToken = UUID.randomUUID().toString();
        log.info("Generated accessToken: {}, refreshToken: {}", accessToken, refreshToken);

        return LoginResponse.builder()
                .id(employee.getId())
                .check(true)
                .position(employee.getPosition())
                .token(TokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build())
                .build();
    }

    public void logout(LogoutRequest request) {
        log.info("Logout request received");

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String accessToken = null;

            if (authentication != null && authentication.getCredentials() instanceof String) {
                accessToken = (String) authentication.getCredentials();
            }

            if (accessToken != null && jwtProvider.validateToken(accessToken)) {
                String jwtId = jwtProvider.getJwtIdFromToken(accessToken);
                Date expiryTime = jwtProvider.parseClaims(accessToken).getExpirationTime();

                if (jwtId != null && !blacklistedTokenRepository.existsById(jwtId)) {
                    BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                            .id(jwtId)
                            .expiryTime(expiryTime)
                            .build();
                    blacklistedTokenRepository.save(blacklistedToken);
                    log.info("Access token blacklisted: {}", jwtId);
                }
            }

            if (request != null && request.getToken() != null) {
                log.info("Refresh token invalidated: {}", request.getToken());
            }

            SecurityContextHolder.clearContext();
            log.info("Logout completed successfully");
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            throw new BadRequestException(messageSourceHelper.getMessage("error.logout_failed"));
        }
    }

    @Deprecated
    private Language getLegion() {
        final Locale locale = LocaleContextHolder.getLocale();
        switch (locale.getLanguage()) {
            case "en":
                return Language.EN;
            case "vi":
                return Language.VI;
            default:
                return Language.DEFAULT;
        }
    }
}