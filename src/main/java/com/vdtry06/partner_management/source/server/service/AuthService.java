package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.source.server.config.JwtProvider;
import com.vdtry06.partner_management.source.server.entities.BlacklistedToken;
import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.payload.auth.LoginRequest;
import com.vdtry06.partner_management.source.server.payload.auth.LoginResponse;
import com.vdtry06.partner_management.source.server.payload.auth.LogoutRequest;
import com.vdtry06.partner_management.source.server.payload.auth.TokenResponse;
import com.vdtry06.partner_management.source.server.repositories.BlacklistedTokenRepository;
import com.vdtry06.partner_management.source.server.repositories.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.*;

import java.util.Date;
import java.util.UUID;

@Service
@Log4j2
public class AuthService {
    private final EmployeeRepository employeeRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public AuthService(EmployeeRepository employeeRepository, AuthenticationManager authenticationManager, JwtProvider jwtProvider, BlacklistedTokenRepository blacklistedTokenRepository) {
        this.employeeRepository = employeeRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
    }


    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Employee employee = employeeRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Tên đăng nhập không tìm thấy"));

        final String accessToken = jwtProvider.generateToken(employee.getUsername());
        final String refreshToken = UUID.randomUUID().toString();

        return LoginResponse.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .fullname(employee.getFullname())
                .token(TokenResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build())
                .build();
    }

    public void logout(LogoutRequest request) {
        log.info("Logout request received");

        try {
            // Lấy access token từ SecurityContext (được set trong JwtAuthenticationFilter)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String accessToken = null;

            if (authentication != null && authentication.getCredentials() != null) {
                accessToken = authentication.getCredentials().toString();
            }

            // Blacklist access token
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

            // Nếu có refresh token trong request (dù là UUID string, vẫn lưu để tracking)
            if (request != null && request.getToken() != null) {
                log.info("Refresh token invalidated: {}", request.getToken());
            }

            SecurityContextHolder.clearContext();
            log.info("Logout completed successfully");

        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            throw new RuntimeException("Logout failed", e);
        }
    }
}