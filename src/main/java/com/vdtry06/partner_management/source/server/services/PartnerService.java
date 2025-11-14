package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.source.server.dto.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.dto.partner.PartnerResponse;
import com.vdtry06.partner_management.source.server.entities.Partner;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.repositories.PartnerManagerRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerManagerRepository partnerManagerRepository;

    public PartnerResponse createPartner(PartnerRequest request, Integer partnerManagerId) {
        PartnerManager partnerManager = partnerManagerRepository.findById(partnerManagerId)
                .orElseThrow(() -> new RuntimeException("Partner Manager not found"));

        Partner partner = toPartner(request);
        partner.setPartnerManagerId(partnerManager);
        partner = partnerRepository.save(partner);
        return PartnerResponse.builder()
                .id(partner.getId())
                .build();
    }

    private Partner toPartner(PartnerRequest request) {
        return Partner.builder()
                .namePartner(request.getNamePartner())
                .partnerRepresentative(request.getPartnerRepresentative())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .taxCode(request.getTaxCode())
                .connperationDate(request.getConnperationDate())
                .description(request.getDescription())
                .build();
    }

}
