package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerManagerRepository extends JpaRepository<PartnerManager, Integer> {
}
