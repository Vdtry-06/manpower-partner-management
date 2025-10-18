package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.enumerated.ContractStatus;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.Partner;
import com.vdtry06.partner_management.source.server.entities.PartnerManager;
import com.vdtry06.partner_management.source.server.payload.contract.ContractResponse;
import com.vdtry06.partner_management.source.server.repositories.ContractRepository;
import com.vdtry06.partner_management.source.server.repositories.PartnerRepository;
import com.vdtry06.partner_management.source.server.repositories.spec.ContractSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService extends BaseService<Contract, Integer> {
    private final ContractRepository contractRepository;
    private final EmployeeService employeeService;
    private final PartnerManagerService partnerManagerService;
    private final PartnerService partnerService;
    private final PartnerRepository partnerRepository;
    private final TaskContractService taskContractService;

    public ContractService(BaseRepository<Contract, Integer> repository, ContractRepository contractRepository, EmployeeService employeeService, PartnerManagerService partnerManagerService, PartnerService partnerService, PartnerRepository partnerRepository, TaskContractService taskContractService) {
        super(repository);
        this.contractRepository = contractRepository;
        this.employeeService = employeeService;
        this.partnerManagerService = partnerManagerService;
        this.partnerService = partnerService;
        this.partnerRepository = partnerRepository;
        this.taskContractService = taskContractService;
    }

    public ContractResponse getContractByPartnerIdAndContractId(Integer partnerId, Integer contractId) {
        if (!partnerRepository.existsById(partnerId)) throw new RuntimeException("Không tìm thấy id đối tác");
        Contract contract = contractRepository.findContractByPartnerIdAndContactId(partnerId, contractId);
//        Specification<Contract> spec = ContractSpecification.hasPartnerId(partnerId)
//                .and(ContractSpecification.hasPartnerId(contractId));
//        Contract contract = (Contract) contractRepository.findAll(spec);

        return toContractResponse(contract);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<ContractResponse> getAllContractsByPartnerId(int page, int perPage, Integer partnerId) {
        if (!partnerRepository.existsById(partnerId)) throw new RuntimeException("Không tìm thấy id đối tác");
        long totalRecord = contractRepository.countAllContractByPartnerId(partnerId);
        int offset = PagingUtil.getOffSet(page, perPage);
        int totalPage = PagingUtil.getTotalPage(totalRecord, perPage);
        List<Contract> contracts = contractRepository.findAllContractByPartnerId(offset, perPage, partnerId);

//        Specification<Contract> spec = ContractSpecification.hasPartnerId(partnerId);
//        List<Contract> contracts = contractRepository.findAll(spec);

        List<ContractResponse> contractResponses = new ArrayList<>();
        if (contracts != null) {
            contractResponses = contracts
                    .stream()
                    .map(contract -> toContractResponse(contract))
                    .toList();
        }

        return PaginationResponse.<ContractResponse>builder()
                .page(page)
                .perPage(perPage)
                .data(contractResponses)
                .totalPage(totalPage)
                .totalRecord(totalRecord)
                .build();
    }

    @Transactional(readOnly = true)
    public ContractResponse getContractById(Integer contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id của hợp đồng"));
        return toContractResponse(contract);
    }

    @Transactional(rollbackFor = BadRequestException.class)
    public ContractResponse createContract(Integer partnerId) {
        PartnerManager partnerManager = partnerManagerService.getCurrentPartnerManager();
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id đối tác"));

        Contract contract = Contract.builder()
                .partnerManagerId(partnerManager)
                .partnerId(partner)
                .contractName("Cung cấp nhân công cho " + partner.getNamePartner())
                .totalContractValue(0)
                .startDate(LocalDate.now())
                .status(ContractStatus.PENDING)
                .build();
        contract = contractRepository.save(contract);

        return toContractResponse(contract);
    }

    @Transactional(rollbackFor = BadRequestException.class)
    public ContractResponse updateContract(Integer contractId) {
        Contract contract = contractRepository.findById(contractId).orElse(null);
        contract.setEndDate(taskContractService.endDateContract(contractId));
        contract.setTotalContractValue(taskContractService.totalTasksUnitPrice(contractId));
        contract.setStatus(ContractStatus.ACTIVE);
        contract.setDescription(taskContractService.getListNameTasks(contractId));
        contract = contractRepository.save(contract);
        return toContractResponse(contract);
    }

//    private Contract toContract(ContractRequest contractRequest) {
//        new Contract();
//        return Contract.builder()
//                .contractName(contractRequest.getContractName())
//                .startDate(contractRequest.getStartDate())
//                .endDate(contractRequest.getEndDate())
//                .totalContractValue(contractRequest.getTotalContractValue())
//                .status(ContractStatus.ACTIVE)
//                .description(contractRequest.getDescription())
//                .build();
//    }

    private ContractResponse toContractResponse(Contract contract) {
        new ContractResponse();
        return ContractResponse.builder()
                .id(contract.getId())
                .contractName(contract.getContractName())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .totalContractValue(contract.getTotalContractValue())
                .status(contract.getStatus())
                .description(contract.getDescription())
                .partnerManagerId(contract.getPartnerManagerId())
                .partnerId(contract.getPartnerId())
                .build();
    }
}
