package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.entities.Partner;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.payload.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.payload.partner.PartnerResponse;
import com.vdtry06.partner_management.source.server.repositories.EmployeeRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerManagerRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final PartnerManagerService partnerManagerService;
    private final PartnerManagerRepository partnerManagerRepository;
    private final EmployeeRepository employeeRepository;

    public PartnerService(PartnerRepository partnerRepository, PartnerManagerService partnerManagerService, PartnerManagerRepository partnerManagerRepository, EmployeeRepository employeeRepository) {
        this.partnerRepository = partnerRepository;
        this.partnerManagerService = partnerManagerService;
        this.partnerManagerRepository = partnerManagerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(rollbackFor = BadRequestException.class)
    public PartnerResponse createPartner(Integer id, PartnerRequest partnerRequest) {
        if (partnerRepository.existsByNamePartner(partnerRequest.getNamePartner())) {
            throw new RuntimeException("Tên đối tác đã có trong hệ thống");
        }

        PartnerManager partnerManager = partnerManagerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("id này không có quyền vào"));

        Partner partner = toPartner(partnerRequest);
        partner.setPartnerManagerId(partnerManager);
        partner = partnerRepository.save(partner);
        return toPartnerResponse(partner);
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
                .build();
    }
}
