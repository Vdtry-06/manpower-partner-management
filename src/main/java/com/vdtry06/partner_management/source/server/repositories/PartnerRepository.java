package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.source.server.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
}
