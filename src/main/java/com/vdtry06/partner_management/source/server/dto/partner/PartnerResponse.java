package com.vdtry06.partner_management.source.server.dto.partner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
