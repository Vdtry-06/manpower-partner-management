package com.vdtry06.partner_management.source.server.repositories;

import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.source.server.entities.Partner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends BaseRepository<Partner, Integer> {

    boolean existsByTaxCode(String taxCode);

    boolean existsByNamePartner(String namePartner);

    @Query(
            value = "SELECT COUNT(*) " +
                    "FROM public.tbl_partner p " +
                    "WHERE ( :search IS NULL OR :search = '' OR p.name_partner ILIKE CONCAT('%', :search, '%')) ",
            nativeQuery = true)
    long countAllPartnerWithConditions(@Param("search") String search);

//    @Query(
//            value = "SELECT * FROM public.tbl_partner p " +
//                    "WHERE ( :search IS NULL OR :search = '' OR p.name_partner ILIKE CONCAT('%', :search, '%')) " +
//                    "ORDER BY p.partner_id ASC " +
//                    "LIMIT :limit OFFSET :offset ",
//            nativeQuery = true)
//    List<Partner> findAllPartnerWithCondition(
//            @Param("offset") int offset,
//            @Param("limit") int limit,
//            @Param("search") String search
//    );

    /*
        dung projection :
        - khong can select toan bang
        - toi uu hon khi nhieu bang lon
     */
    @Query(
            value = "SELECT p.partner_id AS id, p.name_partner AS namePartner, p.partner_representative AS partnerRepresentative, p.phone_number AS phoneNumber, p.email AS email, p.name_partner AS namePartner " +
                    "FROM public.tbl_partner p " +
                    "WHERE ( :search IS NULL OR :search = '' OR p.name_partner ILIKE CONCAT('%', :search, '%')) " +
                    "ORDER BY p.partner_id ASC " +
                    "LIMIT :limit OFFSET :offset ",
            nativeQuery = true)
    List<Object[]> findAllPartnerWithConditions(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("search") String search
    );
}
