package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.source.server.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
    List<Partner> findByPartnerManagerId_Id(Integer partnerManagerId);
    boolean existsByEmail(String email);
    boolean existsByTaxCode(String taxCode);

    @Query("SELECT p FROM Partner p WHERE p.partnerManagerId.id = :managerId " +
            "AND (LOWER(p.namePartner) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.partnerRepresentative) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Partner> searchByKeywordAndManagerId(@Param("keyword") String keyword,
                                              @Param("managerId") Integer managerId);
}