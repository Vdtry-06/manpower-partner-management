package com.vdtry06.partner_management.source.server.payload.partner;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerRequest {
    private String namePartner;
    private String partnerRepresentative;
    private String phoneNumber;
    private String email;
    private String address;
    private String taxCode;
    private LocalDate cooperationDate;
    private String description;
}
