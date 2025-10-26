package com.vdtry06.partner_management.source.server.dto.contract;

import com.vdtry06.partner_management.source.server.dto.taskcontract.TaskContractDetailResponse;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.services.TaskContractService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDetailResponse {
    private Integer id;
    private String contractName;
    private String description;
    private String startDate;
    private String endDate;
    private Integer totalContractValue;
    private String status;

    // Thông tin đối tác
    private String partnerName;
    private String partnerRepresentative;
    private String partnerPhone;
    private String partnerEmail;

    // Thông tin nhân viên quản lý
    private String managerName;
    private String managerPosition;

    // Danh sách task contracts với shifts
    private List<TaskContractDetailResponse> taskContracts;

    public static ContractDetailResponse fromEntity(Contract contract, TaskContractService taskContractService) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return ContractDetailResponse.builder()
                .id(contract.getId())
                .contractName(contract.getContractName())
                .description(contract.getDescription())
                .startDate(contract.getStartDate() != null ? sdf.format(contract.getStartDate()) : "")
                .endDate(contract.getEndDate() != null ? sdf.format(contract.getEndDate()) : "")
                .totalContractValue(contract.getTotalContractValue())
                .status(contract.getStatus() != null ? contract.getStatus().name() : "")
                .partnerName(contract.getPartnerId().getNamePartner())
                .partnerRepresentative(contract.getPartnerId().getPartnerRepresentative())
                .partnerPhone(contract.getPartnerId().getPhoneNumber())
                .partnerEmail(contract.getPartnerId().getEmail())
                .managerName(contract.getPartnerManagerId().getFullname())
                .managerPosition("Quản lý đối tác")
                .taskContracts(taskContractService.getTaskContractsByContractId(contract.getId())
                        .stream()
                        .map(tc -> TaskContractDetailResponse.fromTaskContractResponse(tc))
                        .collect(Collectors.toList()))
                .build();
    }
}