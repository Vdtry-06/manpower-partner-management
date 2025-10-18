package com.vdtry06.partner_management.source.server.payload.contract;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRequest {
    private String contractName;
    private String description;
}
