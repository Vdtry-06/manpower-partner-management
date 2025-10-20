package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.payload.contract.ContractResponse;
import com.vdtry06.partner_management.source.server.service.ContractService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("get-contract-id-of-partner/{contractId}/{partnerId}")
    public ApiResponse<ContractResponse> getContractByPartnerIdAndContractId(
            @PathVariable int partnerId,
            @PathVariable int contractId
    ) {
        return new ApiResponse<>(true, contractService.getContractByPartnerIdAndContractId(partnerId, contractId));
    }

    @GetMapping("/get-all-contracts-of-partner/{partnerId}")
    public PaginationResponse<ContractResponse> getAllContractsByPartnerId(
            @RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_SIZE) int perPage,
            @PathVariable int partnerId
    ) {
        return contractService.getAllContractsByPartnerId(page, perPage, partnerId);
    }

    @GetMapping("/{id}")
    public ApiResponse<ContractResponse> getContractById(@PathVariable int id) {
        return new ApiResponse<>(true, contractService.getContractById(id));
    }

    @PostMapping("/add/{partnerId}")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public ApiResponse<ContractResponse> createContract(@PathVariable int partnerId) {
        return new ApiResponse<>(true, contractService.createContract(partnerId));
    }

    @PostMapping("/update/{contractId}")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public ApiResponse<ContractResponse> updateContract(@PathVariable int contractId) {
        return new ApiResponse<>(true, contractService.updateContract(contractId));
    }

}
