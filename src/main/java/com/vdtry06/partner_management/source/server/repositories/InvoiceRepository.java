package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.source.server.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query(value = """
        SELECT COALESCE(SUM(DISTINCT i.payment_amount), 0)
        FROM tbl_invoice i
        JOIN tbl_shift s ON s.invoice_id = i.invoice_id
        JOIN tbl_shift_task_contract stc ON stc.shift_id = s.shift_id
        JOIN tbl_task_contract tc ON tc.id = stc.task_contract_id
        WHERE tc.contract_id = :contractId
    """, nativeQuery = true)
    long sumPaymentAmountByContract(@Param("contractId") Integer contractId);
}
