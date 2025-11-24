package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.source.server.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
    List<Partner> findByPartnerManagerId_Id(Integer managerId);

    @Query(value = "SELECT * FROM tbl_partner " +
            "WHERE partner_manager_id = :managerId " +
            "AND name_partner ILIKE CONCAT('%', :namePartner, '%')",
            nativeQuery = true)
    List<Partner> searchByNamePartnerAndManagerId(@Param("namePartner") String namePartner,
                                                  @Param("managerId") Integer managerId);

    @Query(value = "SELECT * FROM tbl_partner " +
            "WHERE name_partner ILIKE CONCAT('%', :namePartner, '%')",
            nativeQuery = true)
    List<Partner> searchByNamePartnerAndAccountantId(@Param("namePartner") String namePartner);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM tbl_partner " +
            "WHERE name_partner = :namePartner",
            nativeQuery = true)
    boolean existsByNamePartner(@Param("namePartner") String namePartner);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM tbl_partner " +
            "WHERE tax_code = :taxCode",
            nativeQuery = true)
    boolean existsByTaxCode(@Param("taxCode") String taxCode);
}
