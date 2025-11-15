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
}
