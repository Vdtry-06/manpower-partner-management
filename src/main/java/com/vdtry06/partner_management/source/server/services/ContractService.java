package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.lib.enumerated.ContractStatus;
import com.vdtry06.partner_management.source.server.dto.contract.ContractDetailResponse;
import com.vdtry06.partner_management.source.server.dto.contract.ContractRequest;
import com.vdtry06.partner_management.source.server.dto.contract.ContractResponse;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.Partner;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.repositories.ContractRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final PartnerRepository partnerRepository;
    private final PartnerManagerRepository partnerManagerRepository;
    private final TaskContractService taskContractService;

    public List<ContractResponse> getAllContracts() {
        return contractRepository.findAll().stream()
                .map(ContractResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ContractResponse> getContractsByManagerId(Integer managerId) {
        return contractRepository.findByPartnerManagerId_Id(managerId).stream()
                .map(ContractResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Contract getContractById(Integer id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));
    }

    public ContractDetailResponse getContractDetail(Integer contractId) {
        Contract contract = getContractById(contractId);
        return ContractDetailResponse.fromEntity(contract, taskContractService);
    }

    @Transactional
    public Integer createDraftContract(Integer partnerId, Integer partnerManagerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found with id: " + partnerId));

        PartnerManager partnerManager = partnerManagerRepository.findById(partnerManagerId)
                .orElseThrow(() -> new RuntimeException("Partner Manager not found with id: " + partnerManagerId));

        // Tạo tên hợp đồng tự động
        String contractName = "Cung cấp nhân công cho " + partner.getNamePartner();

        Contract contract = Contract.builder()
                .contractName(contractName)
                .startDate(new Date())
                .totalContractValue(0)
                .status(ContractStatus.DRAFT) // Trạng thái nháp
                .partnerId(partner)
                .partnerManagerId(partnerManager)
                .build();

        Contract savedContract = contractRepository.save(contract);
        return savedContract.getId();
    }

    @Transactional
    public Contract updateContract(Integer id, ContractRequest request) {
        Contract contract = getContractById(id);

        contract.setContractName(request.getContractName());
        contract.setStartDate(request.getStartDate());
        contract.setEndDate(request.getEndDate());
        contract.setDescription(request.getDescription());

        return contractRepository.save(contract);
    }

    @Transactional
    public void finalizeContract(Integer contractId) {
        Contract contract = getContractById(contractId);

        // Tính tổng giá trị hợp đồng
        Integer totalValue = taskContractService.calculateContractTotalValue(contractId);

        contract.setTotalContractValue(totalValue);
        contract.setStatus(ContractStatus.ACTIVE);
        contract.setEndDate(new Date()); // Hoặc tính toán từ shifts

        contractRepository.save(contract);
    }

    @Transactional
    public void deleteContract(Integer id) {
        if (!contractRepository.existsById(id)) {
            throw new RuntimeException("Contract not found with id: " + id);
        }
        contractRepository.deleteById(id);
    }

    @Transactional
    public void cancelDraftContract(Integer contractId) {
        Contract contract = getContractById(contractId);

        if (contract.getStatus() != ContractStatus.DRAFT) {
            throw new RuntimeException("Chỉ có thể hủy hợp đồng ở trạng thái nháp!");
        }

        // Xóa tất cả task contracts và shifts liên quan
        taskContractService.deleteByContractId(contractId);

        // Xóa contract
        contractRepository.deleteById(contractId);
    }
}
