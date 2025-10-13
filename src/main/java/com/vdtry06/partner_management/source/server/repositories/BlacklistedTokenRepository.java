package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.BlacklistedToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface BlacklistedTokenRepository extends BaseRepository<BlacklistedToken, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM BlacklistedToken b WHERE b.expiryTime < :expiryTime")
    int deleteByExpiryTimeBefore(@org.springframework.data.repository.query.Param("expiryTime") Date expiryTime);
}
