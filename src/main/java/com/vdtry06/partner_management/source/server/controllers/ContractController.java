package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.payload.contract.ContractResponse;
import com.vdtry06.partner_management.source.server.service.ContractService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contract")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public String showSearchPartnerView() {
        return "PartnerManagementView";
    }

    @GetMapping("/get-contract-id-of-partner/{contractId}/{partnerId}")
    public String getContractByPartnerIdAndContractId(
            @PathVariable int partnerId,
            @PathVariable int contractId,
            Model model
    ) {
        ContractResponse response = contractService.getContractByPartnerIdAndContractId(partnerId, contractId);
        model.addAttribute("contract", response);
        return "DetailContractView"; // Adjust view as needed
    }

    @GetMapping("/get-all-contracts-of-partner/{partnerId}")
    public String getAllContractsByPartnerId(
            @RequestParam(defaultValue = PagingUtil.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PagingUtil.DEFAULT_SIZE) int perPage,
            @PathVariable int partnerId,
            Model model
    ) {
        PaginationResponse<ContractResponse> pagination = contractService.getAllContractsByPartnerId(page, perPage, partnerId);
        model.addAttribute("contracts", pagination.getData());
        return "ListContractView";
    }

    @GetMapping("/{id}")
    public String getContractById(@PathVariable int id, Model model) {
        ContractResponse response = contractService.getContractById(id);
        model.addAttribute("contract", response);
        return "DetailContractView";
    }

    @GetMapping("/add/{partnerId}")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public String showCreateContract(@PathVariable int partnerId, Model model) {
        model.addAttribute("partnerId", partnerId);
        return "ConfirmContractView"; // Or a form view if needed
    }

    @PostMapping("/add/{partnerId}")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public String createContract(@PathVariable int partnerId, Model model) {
        ContractResponse response = contractService.createContract(partnerId);
        model.addAttribute("contract", response);
        return "redirect:/list-task"; // Redirect to next step
    }

    @GetMapping("/update/{contractId}")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public String showUpdateContract(@PathVariable int contractId, Model model) {
        ContractResponse response = contractService.getContractById(contractId);
        model.addAttribute("contract", response);
        return "ConfirmContractView";
    }

    @PostMapping("/update/{contractId}")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public String updateContract(@PathVariable int contractId, Model model) {
        ContractResponse response = contractService.updateContract(contractId);
        model.addAttribute("contract", response);
        return "ConfirmContractView";
    }
}