package com.vdtry06.partner_management.source.server.payload.partner;

import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import lombok.*;

import java.util.Date;

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
    private Date cooperationDate;
    private String description;
}
