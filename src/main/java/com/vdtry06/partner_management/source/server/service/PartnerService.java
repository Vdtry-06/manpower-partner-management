package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.exceptions.BadRequestException;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.config.language.MessageSourceHelper;
import com.vdtry06.partner_management.source.server.entities.Partner;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.payload.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.payload.partner.PartnerResponse;
import com.vdtry06.partner_management.source.server.repositories.PartnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerManagerService partnerManagerService;
    private final MessageSourceHelper messageSourceHelper;

    protected PartnerService(PartnerRepository partnerRepository, PartnerManagerService partnerManagerService, MessageSourceHelper messageSourceHelper) {
        this.partnerRepository = partnerRepository;
        this.partnerManagerService = partnerManagerService;
        this.messageSourceHelper = messageSourceHelper;
    }

    protected Partner findById(Integer partnerId) {
        return partnerRepository.findById(partnerId)
                .orElseThrow(() -> new BadRequestException(messageSourceHelper.getMessage("error.not_found.partner")));
    }

    protected boolean existsById(Integer partnerId) {
        return partnerRepository.existsById(partnerId);
    }

    @Transactional(rollbackFor = BadRequestException.class)
    public PartnerResponse createPartner(PartnerRequest partnerRequest) {
        PartnerManager partnerManager = partnerManagerService.getCurrentPartnerManager();

        if (partnerRepository.existsByNamePartner(partnerRequest.getNamePartner())) {
            throw new BadRequestException(messageSourceHelper.getMessage("error.partner.name_exists"));
        }

        Partner partner = toPartner(partnerRequest);
        partner.setPartnerManagerId(partnerManager);
        partner = partnerRepository.save(partner);

        return toPartnerResponse(partner);
    }

    @Transactional(readOnly = true)
    public PartnerResponse getPartnerById(Integer partnerId) {
        Partner partner =  partnerRepository.findById(partnerId)
                .orElseThrow(() -> new BadRequestException(messageSourceHelper.getMessage("error.not_found.partner")));
        return toPartnerResponse(partner);
    }

//    @Transactional(readOnly = true)
//    public PaginationResponse<PartnerResponse> getAllPartnersWithConditions(int page, int perPage, String search) {
//        long totalRecord = partnerRepository.countAllPartnerWithConditions(search);
//        int offset = PagingUtil.getOffSet(page, perPage);
//        int totalPage = PagingUtil.getTotalPage(totalRecord, perPage);
//        List<Partner> partners = partnerRepository.findAllPartnerWithCondition(offset, perPage, search);
//        List<PartnerResponse> partnerResponses = new ArrayList<>();
//        if (partners != null) {
//            partnerResponses = partners
//                    .stream()
//                    .map(partner -> toPartnerResponse(partner))
//                    .toList()
//            ;
//        }
//
//        return PaginationResponse.<PartnerResponse>builder()
//                .page(page)
//                .perPage(perPage)
//                .data(partnerResponses)
//                .totalPage(totalPage)
//                .totalRecord(totalRecord)
//                .build();
//
//    }

    @Transactional(readOnly = true)
    public PaginationResponse<PartnerResponse> getAllPartnersWithConditions(int page, int perPage, String search) {
        long totalRecord = partnerRepository.countAllPartnerWithConditions(search);
        int offset = PagingUtil.getOffSet(page, perPage);
        int totalPage = PagingUtil.getTotalPage(totalRecord, perPage);

        List<Object[]> rows = partnerRepository.findAllPartnerWithConditions(offset, perPage, search);
        List<PartnerResponse> partnerResponses = rows.stream()
                .map(r -> PartnerResponse.builder()
                        .id((Integer) r[0])
                        .namePartner((String) r[1])
                        .partnerRepresentative((String) r[2])
                        .phoneNumber((String) r[3])
                        .email((String) r[4])
                        .build())
                .toList();

        return new PaginationResponse<>(page, perPage, partnerResponses, totalPage, totalRecord);
    }

    private Partner toPartner(PartnerRequest partnerRequest) {
        new Partner();
        return Partner.builder()
                .namePartner(partnerRequest.getNamePartner())
                .partnerRepresentative(partnerRequest.getPartnerRepresentative())
                .phoneNumber(partnerRequest.getPhoneNumber())
                .email(partnerRequest.getEmail())
                .address(partnerRequest.getAddress())
                .taxCode(partnerRequest.getTaxCode())
                .connperationDate(partnerRequest.getCooperationDate())
                .description(partnerRequest.getDescription())
                .build();
    }

    private PartnerResponse toPartnerResponse(Partner partner) {
        new PartnerResponse();
        return PartnerResponse.builder()
                .id(partner.getId())
                .namePartner(partner.getNamePartner())
                .partnerRepresentative(partner.getPartnerRepresentative())
                .phoneNumber(partner.getPhoneNumber())
                .email(partner.getEmail())
                .build();
    }
}
