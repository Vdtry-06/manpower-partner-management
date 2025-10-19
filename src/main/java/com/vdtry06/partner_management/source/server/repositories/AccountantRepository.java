package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.Accountant;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountantRepository extends BaseRepository<Accountant, Integer> {
}
