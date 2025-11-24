package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.lib.enumerated.ContractStatus;
import com.vdtry06.partner_management.source.server.dto.contract.ContractDetailResponse;
import com.vdtry06.partner_management.source.server.dto.contract.ContractResponse;
import com.vdtry06.partner_management.source.server.dto.shift.ShiftDetailResponse;
import com.vdtry06.partner_management.source.server.dto.task_contract.TaskContractDetailResponse;
import com.vdtry06.partner_management.source.server.entities.*;
import com.vdtry06.partner_management.source.server.repositories.ContractRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerManagerRepository;
import com.vdtry06.partner_management.source.server.repositories.ShiftTaskContractRepository;
import com.vdtry06.partner_management.source.server.repositories.TaskContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final TaskContractService taskContractService;
    private final PartnerService partnerService;
    private final ContractRepository contractRepository;
    private final PartnerManagerRepository partnerManagerRepository;
    private final TaskContractRepository taskContractRepository;
    private final ShiftTaskContractRepository shiftTaskContractRepository;

    public List<ContractResponse> getAllContractsByPartnerId(Integer partnerId) {
        return contractRepository.findByPartnerId_Id(partnerId).stream()
                .map(contract -> toContractResponse(contract))
                .collect(Collectors.toList());
    }

    public Integer createDraftContract(Integer partnerId, Integer partnerManagerId) {
        Partner partner = partnerService.getPartnerById(partnerId);

        PartnerManager partnerManager = partnerManagerRepository.findById(partnerManagerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id nhân viên quản lý đối tác: " + partnerManagerId));

        Contract contract = Contract.builder()
                .contractName("Cung cấp nhân công cho " + partner.getNamePartner())
                .startDate(LocalDate.now())
                .totalContractValue(0L)
                .status(ContractStatus.DRAFT)
                .partnerId(partner)
                .partnerManagerId(partnerManager)
                .build();

        contract = contractRepository.save(contract);
        return contract.getId();
    }

    public void cancelDraftContract(Integer contractId) {
        Contract contract = getContractById(contractId);

        if (contract.getStatus() != ContractStatus.DRAFT) {
            throw new RuntimeException("Chỉ có thể hủy hợp đồng ở trạng thái nháp");
        }

        taskContractService.deleteByContractId(contractId);
        contractRepository.deleteById(contractId);
    }

    public Contract getContractById(Integer id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id hợp đồng"));
    }

    public void updateContract(Integer contractId) {
        Contract contract = getContractById(contractId);

        List<TaskContract> taskContracts = taskContractRepository.findByContractId_Id(contractId);
        long totalContractValue = 0;
        LocalDate endDate = LocalDate.now();

        for (TaskContract taskContract : taskContracts) {
            List<ShiftTaskContract> shiftTaskContracts =
                    shiftTaskContractRepository.findByTaskContractId_Id(taskContract.getId());

            for (ShiftTaskContract shiftTaskContract : shiftTaskContracts) {
                if (shiftTaskContract.getShiftId() != null) {
                    Shift shift = shiftTaskContract.getShiftId();
                    if (endDate.isBefore(shift.getWorkDate())) endDate = shift.getWorkDate();
                }
            }
            totalContractValue += taskContract.getTaskUnitPrice();
        }

        String description = taskContracts.stream()
                .map(tc -> tc.getTaskId().getNameTask())
                .collect(Collectors.joining(", "));
        contract.setDescription(description);
        contract.setEndDate(LocalDate.parse(endDate.toString()));
        contract.setTotalContractValue(totalContractValue);
        contractRepository.save(contract);
    }

    public ContractDetailResponse getContractDetailById(Integer contractId) {
        Contract contract = getContractById(contractId);

        List<TaskContract> taskContracts = taskContractRepository.findByContractId_Id(contractId);
        List<TaskContractDetailResponse> taskContractDetails = new ArrayList<>();

        for (TaskContract taskContract : taskContracts) {
            List<ShiftTaskContract> shiftTaskContracts =
                    shiftTaskContractRepository.findByTaskContractId_Id(taskContract.getId());

            List<ShiftDetailResponse> shiftDetails = new ArrayList<>();

            for (ShiftTaskContract shiftTaskContract : shiftTaskContracts) {
                if (shiftTaskContract.getShiftId() != null) {
                    Shift shift = shiftTaskContract.getShiftId();

                    shiftDetails.add(ShiftDetailResponse.builder()
                            .id(shift.getId())
                            .workDate(shift.getWorkDate().toString())
                            .shiftType(shift.getShiftType().name())
                            .startTime(shift.getShiftType().timeRange().split("-")[0])
                            .endTime(shift.getShiftType().timeRange().split("-")[1])
                            .workerCount(shiftTaskContract.getWorkerCount())
                            .shiftUnitPrice(shiftTaskContract.getShiftUnitPrice())
                            .totalValue(shiftTaskContract.getShiftUnitPrice() * shiftTaskContract.getWorkerCount())
                            .invoiceId(shift.getInvoice() != null ? shift.getInvoice().getId() : null)
                            .build()
                    );
                }
            }

            if (!shiftDetails.isEmpty()) {
                taskContractDetails.add(
                        TaskContractDetailResponse.builder()
                                .taskContractId(taskContract.getId())
                                .taskId(taskContract.getTaskId().getId())
                                .taskName(taskContract.getTaskId().getNameTask())
                                .taskDescription(taskContract.getTaskId().getDescription())
                                .shifts(shiftDetails)
                                .totalValue(taskContract.getTaskUnitPrice())
                                .build()
                );
            }
        }

        return ContractDetailResponse.builder()
                .contractId(contract.getId())
                .contractName(contract.getContractName())
                .description(contract.getDescription())
                .startDate(contract.getStartDate().toString())
                .endDate(contract.getEndDate().toString())
                .partnerId(contract.getPartnerId().getId())
                .partnerName(contract.getPartnerId().getNamePartner())
                .partnerRepresentative(contract.getPartnerId().getPartnerRepresentative())
                .partnerPhone(contract.getPartnerId().getPhoneNumber())
                .partnerEmail(contract.getPartnerId().getEmail())
                .managerName(contract.getPartnerManagerId().getFullname())
                .managerPosition(contract.getPartnerManagerId().getPosition().name())
                .taskContracts(taskContractDetails)
                .totalContractValue(contract.getTotalContractValue())
                .build();
    }

    @Transactional
    public void finalizeContract(Integer contractId) {
        Contract contract = getContractById(contractId);

        if (contract.getStatus() != ContractStatus.DRAFT) {
            throw new RuntimeException("Hợp đồng đã được xác nhận trước đó");
        }

        contract.setStatus(ContractStatus.ACTIVE);

        contractRepository.save(contract);
    }

    private ContractResponse toContractResponse(Contract contract) {

        return ContractResponse.builder()
                .id(contract.getId())
                .contractName(contract.getContractName())
                .startDate(contract.getStartDate().toString())
                .endDate(contract.getEndDate().toString())
                .totalContractValue(contract.getTotalContractValue())
                .status(contract.getStatus().name())
                .description(contract.getDescription())
                .build();
    }
}
