package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.source.server.dto.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.dto.partner.PartnerResponse;
import com.vdtry06.partner_management.source.server.entities.Partner;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.repositories.PartnerRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;
    private final PartnerManagerRepository partnerManagerRepository;

    public List<PartnerResponse> searchPartnersByKeyword(String keyword, Integer managerId) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getPartnersByManagerIdResponse(managerId);
        }

        return partnerRepository.searchByKeywordAndManagerId(keyword.trim(), managerId).stream()
                .map(PartnerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PartnerResponse> getAllPartnersResponse() {
        return partnerRepository.findAll().stream()
                .map(PartnerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PartnerResponse> getPartnersByManagerIdResponse(Integer managerId) {
        return partnerRepository.findByPartnerManagerId_Id(managerId).stream()
                .map(PartnerResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<Partner> getAllPartners() {
        return partnerRepository.findAll();
    }

    public List<Partner> getPartnersByManagerId(Integer managerId) {
        return partnerRepository.findByPartnerManagerId_Id(managerId);
    }

    public Partner getPartnerById(Integer id) {
        return partnerRepository.findById(id).orElse(null);
    }

    @Transactional
    public Partner createPartner(PartnerRequest request, Integer partnerManagerId) {
        PartnerManager partnerManager = partnerManagerRepository.findById(partnerManagerId)
                .orElseThrow(() -> new RuntimeException("Partner Manager not found"));

        Partner partner = Partner.builder()
                .namePartner(request.getNamePartner())
                .partnerRepresentative(request.getPartnerRepresentative())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .taxCode(request.getTaxCode())
                .connperationDate(request.getConnperationDate())
                .description(request.getDescription())
                .partnerManagerId(partnerManager)
                .build();

        return partnerRepository.save(partner);
    }

    @Transactional
    public Partner updatePartner(Integer id, PartnerRequest request) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        partner.setNamePartner(request.getNamePartner());
        partner.setPartnerRepresentative(request.getPartnerRepresentative());
        partner.setPhoneNumber(request.getPhoneNumber());
        partner.setEmail(request.getEmail());
        partner.setAddress(request.getAddress());
        partner.setTaxCode(request.getTaxCode());
        partner.setConnperationDate(request.getConnperationDate());
        partner.setDescription(request.getDescription());

        return partnerRepository.save(partner);
    }

    @Transactional
    public void deletePartner(Integer id) {
        partnerRepository.deleteById(id);
    }
}