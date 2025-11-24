package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.source.server.dto.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.dto.partner.PartnerResponse;
import com.vdtry06.partner_management.source.server.entities.Partner;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.repositories.AccountantRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerManagerRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerManagerRepository partnerManagerRepository;
    private final AccountantRepository accountantRepository;

    public List<PartnerResponse> searchPartnersByNamePartner(String namePartner, Integer employeeId) {
        boolean isPartnerManager = partnerManagerRepository.existsById(employeeId);
        boolean isAccountant = accountantRepository.existsById(employeeId);
        if (isPartnerManager) {
            if (namePartner == null || namePartner.trim().isEmpty()) return getPartnersByManagerIdResponse(employeeId);
            return partnerRepository.searchByNamePartnerAndManagerId(namePartner.trim(), employeeId).stream()
                    .map(partner -> toPartnerResponse(partner))
                    .collect(Collectors.toList());
        } else if (isAccountant) {
            return partnerRepository.searchByNamePartnerAndAccountantId(namePartner.trim()).stream()
                    .map(partner -> toPartnerResponse(partner))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<PartnerResponse> getPartnersByManagerIdResponse(Integer managerId) {
        return partnerRepository.findByPartnerManagerId_Id(managerId).stream()
                .map(partner -> toPartnerResponse(partner))
                .collect(Collectors.toList());
    }

    public PartnerResponse createPartner(PartnerRequest request, Integer partnerManagerId) {
        PartnerManager partnerManager = partnerManagerRepository.findById(partnerManagerId)
                .orElseThrow(() -> new RuntimeException("Partner Manager not found"));

        Partner partner = toPartner(request);
        if (partnerRepository.existsByNamePartner(partner.getNamePartner()))
            throw new RuntimeException("Tên đối tác đã có trong hệ thống");

        if (partnerRepository.existsByTaxCode(partner.getTaxCode()))
            throw new RuntimeException("Mã số thuế đã có trong hệ thống");

        partner.setPartnerManagerId(partnerManager);
        partner = partnerRepository.save(partner);
        return PartnerResponse.builder()
                .id(partner.getId())
                .build();
    }

    public PartnerResponse getPartnerResponseById(Integer id) {
        Partner partner = getPartnerById(id);
        return toPartnerResponse(partner);
    }

    public Partner getPartnerById(Integer id) {
        return partnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id đối tác"));
    }

    private PartnerResponse toPartnerResponse(Partner partner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return PartnerResponse.builder()
                .id(partner.getId())
                .namePartner(partner.getNamePartner())
                .partnerRepresentative(partner.getPartnerRepresentative())
                .phoneNumber(partner.getPhoneNumber())
                .email(partner.getEmail())
                .address(partner.getAddress())
                .taxCode(partner.getTaxCode())
                .connperationDate(partner.getConnperationDate() != null
                        ? partner.getConnperationDate().format(formatter)
                        : "")
                .description(partner.getDescription())
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
