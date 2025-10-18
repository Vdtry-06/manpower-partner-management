package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.Invoice;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends BaseRepository<Invoice, Integer> {
}
