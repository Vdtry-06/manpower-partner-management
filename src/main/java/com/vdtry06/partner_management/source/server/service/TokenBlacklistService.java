package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.source.server.repositories.BlacklistedTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = new HashSet<>();
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    public void blacklistToken(String token) {
        if (token != null && !blacklistedTokens.contains(token)) {
            blacklistedTokens.add(token);
            log.info("Token {} đã được thêm vào blacklist", token.substring(0, 10) + "...");
        }
    }

    public boolean isTokenBlacklisted(String token) {
        return token != null && blacklistedTokens.contains(token);
    }

    @Scheduled(fixedRate = 360000)
    @Transactional
    public void cleanupExpiredTokens() {
        Date now = new Date();
        try {
            int deleted = blacklistedTokenRepository.deleteByExpiryTimeBefore(now);
            log.info("Cleaned up {} expired blacklisted tokens", deleted);
        } catch (Exception e) {
            log.error("Error cleaning up expired tokens: {}", e.getMessage());
        }
    }
}