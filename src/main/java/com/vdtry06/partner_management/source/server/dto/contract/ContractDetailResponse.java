package com.vdtry06.partner_management.source.server.dto.contract;

import com.vdtry06.partner_management.source.server.dto.task_contract.TaskContractDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDetailResponse {
    private Integer contractId;
    private String contractName;
    private String description;
    private String startDate;
    private String endDate;

    // Partner info
    private Integer partnerId;
    private String partnerName;
    private String partnerRepresentative;
    private String partnerPhone;
    private String partnerEmail;

    // partner manager info
    private String managerName;
    private String managerPosition;

    private List<TaskContractDetailResponse> taskContracts;

    private Long totalContractValue;
}
