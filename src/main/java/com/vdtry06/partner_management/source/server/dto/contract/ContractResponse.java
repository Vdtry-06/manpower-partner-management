package com.vdtry06.partner_management.source.server.dto.contract;

import com.vdtry06.partner_management.lib.enumerated.ContractStatus;
import com.vdtry06.partner_management.source.server.entities.Contract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractResponse {
    private Integer id;
    private String contractName;
    private String startDate;
    private String endDate;
    private Integer totalContractValue;
    private String status;
    private String description;
    private String partnerName;
    private String partnerManagerName;

    public static ContractResponse fromEntity(Contract contract) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return ContractResponse.builder()
                .id(contract.getId())
                .contractName(contract.getContractName())
                .startDate(contract.getStartDate() != null ? sdf.format(contract.getStartDate()) : "")
                .endDate(contract.getEndDate() != null ? sdf.format(contract.getEndDate()) : "")
                .totalContractValue(contract.getTotalContractValue())
                .status(contract.getStatus() != null ? contract.getStatus().name() : "")
                .description(contract.getDescription())
                .partnerName(contract.getPartnerId() != null ? contract.getPartnerId().getNamePartner() : "")
                .partnerManagerName(contract.getPartnerManagerId() != null ? contract.getPartnerManagerId().getFullname() : "")
                .build();
    }
}