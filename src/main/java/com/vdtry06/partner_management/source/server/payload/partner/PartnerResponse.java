package com.vdtry06.partner_management.source.server.payload.partner;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerResponse {
    private Integer id;
    private String namePartner;
    private String partnerRepresentative;
    private String phoneNumber;
    private String email;
}
