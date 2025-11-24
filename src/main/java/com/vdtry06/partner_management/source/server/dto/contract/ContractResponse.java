package com.vdtry06.partner_management.source.server.dto.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {
    private Integer id;
    private String contractName;
    private String startDate;
    private String endDate;
    private Long totalContractValue;
    private String status;
    private String description;
}
