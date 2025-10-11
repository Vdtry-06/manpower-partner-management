package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.Partner;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends BaseRepository<Partner, Integer> {
    boolean existsByNamePartner(String namePartner);
}
