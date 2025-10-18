package com.vdtry06.partner_management.source.server.payload.contract;

import com.vdtry06.partner_management.lib.enumerated.ContractStatus;
import com.vdtry06.partner_management.source.server.entities.Partner;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {
    private Integer id;
    private String contractName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalContractValue;
    @Enumerated(EnumType.STRING)
    private ContractStatus status;
    private String description;
    private PartnerManager partnerManagerId;
    private Partner partnerId;
}
