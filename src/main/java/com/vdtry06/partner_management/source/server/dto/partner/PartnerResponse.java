package com.vdtry06.partner_management.source.server.dto.partner;

import com.vdtry06.partner_management.source.server.entities.Partner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerResponse {
    private Integer id;
    private String namePartner;
    private String partnerRepresentative;
    private String phoneNumber;
    private String email;
    private String address;
    private String taxCode;
    private String connperationDate;
    private String description;

    public static PartnerResponse fromEntity(Partner partner) {
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
}