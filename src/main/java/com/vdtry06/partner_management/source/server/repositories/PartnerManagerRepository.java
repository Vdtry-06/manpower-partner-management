package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerManagerRepository extends BaseRepository<PartnerManager, Integer> {
}
