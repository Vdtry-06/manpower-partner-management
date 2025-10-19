package com.vdtry06.partner_management.source.server.config.security;

import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import com.vdtry06.partner_management.source.server.repositories.BlacklistedTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import com.vdtry06.partner_management.source.server.service.TokenBlacklistService;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final MessageSourceHelper messageSourceHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String path = request.getRequestURI();
            String[] publicUrls = {"/auth/login"};

            for (String publicUrl : publicUrls) {
                if (path.equals(publicUrl)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            String jwt = jwtProvider.getJwtFromRequest(request);

            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // Kiểm tra blacklist bằng JWT ID
            String jwtId = jwtProvider.getJwtIdFromToken(jwt);
            if (jwtId != null && blacklistedTokenRepository.existsById(jwtId)) {
                log.warn("Token đã bị blacklist: {}", jwtId);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, messageSourceHelper.getMessage("warning.access_denied"));
                response.getWriter().write("{\"error\":\"Token đã bị vô hiệu hóa\"}");
                return;
            }

            if (jwtProvider.validateToken(jwt)) {
                String username = jwtProvider.getUsernameFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                log.info("Username: {}", username);
                log.info("Authorities: {}", userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, jwt, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());

            filterChain.doFilter(request, response);
        }
    }
}