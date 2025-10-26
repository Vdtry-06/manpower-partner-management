package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.source.server.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    List<Invoice> findByShiftId_Id(Integer shiftId);
    List<Invoice> findByAccountantId_Id(Integer accountantId);

    @Query("SELECT i FROM Invoice i WHERE i.shiftId.taskContractId.contractId.id = :contractId")
    List<Invoice> findByContractId(@Param("contractId") Integer contractId);
}