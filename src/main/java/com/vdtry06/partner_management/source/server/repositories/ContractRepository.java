package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.Partner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractRepository extends BaseRepository<Contract, Integer> {
    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM public.tbl_contract c " +
                    "WHERE c.partner_id = :partnerId",
            nativeQuery = true
    )
    long countAllContractByPartnerId(Integer partnerId);

    @Query(
            value = "SELECT * " +
                    "FROM public.tbl_contract c " +
                    "WHERE c.partner_id = :partnerId " +
                    "LIMIT :limit OFFSET :offset ",
            nativeQuery = true
    )
    List<Contract> findAllContractByPartnerId(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("partnerId") Integer partnerId);

    @Query(
            value = "SELECT * " +
                    "FROM public.tbl_contract c " +
                    "WHERE c.partner_id = :partnerId " +
                    "AND c.contract_id = :contractId",
            nativeQuery = true
    )
    Contract findContractByPartnerIdAndContactId(
            @Param("partnerId") Integer partnerId,
            @Param("contractId") Integer contractId);
}
