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
import java.util.stream.Stream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDetailResponse {
    private Integer id;
    private Integer taskId;
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
    private boolean hasShifts;

    // Danh sách task contracts với shifts
    private List<TaskContractDetailResponse> taskContracts;

    public static ContractDetailResponse fromEntity(Contract contract, TaskContractService taskContractService) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        boolean hasShifts = taskContractService.getTaskContractsByContractId(contract.getId())
                .stream()
                .flatMap(tc -> tc.getShifts() == null ? Stream.empty() : tc.getShifts().stream())
                .findAny()
                .isPresent();

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
                .hasShifts(hasShifts)
                .build();
    }
}