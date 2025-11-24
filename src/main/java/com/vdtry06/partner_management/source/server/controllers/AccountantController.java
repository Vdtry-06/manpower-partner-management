package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.enumerated.EmployeePosition;
import com.vdtry06.partner_management.lib.enumerated.PaymentMethod;
import com.vdtry06.partner_management.source.server.config.AppUrls;
import com.vdtry06.partner_management.source.server.dto.contract.ContractDetailResponse;
import com.vdtry06.partner_management.source.server.dto.contract.ContractResponse;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceConfirmResponse;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceCreateRequest;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceResponse;
import com.vdtry06.partner_management.source.server.dto.partner.PartnerResponse;
import com.vdtry06.partner_management.source.server.services.AuthService;
import com.vdtry06.partner_management.source.server.services.ContractService;
import com.vdtry06.partner_management.source.server.services.InvoiceService;
import com.vdtry06.partner_management.source.server.services.PartnerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/accountant")
@RequiredArgsConstructor
public class AccountantController {
    private final AuthService authService;
    private final PartnerService partnerService;
    private final ContractService contractService;
    private final InvoiceService invoiceService;
    private final AppUrls appUrls;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.ACCOUNTANT)) return "redirect:/auth/login";

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("username", session.getAttribute("username"));

        return "accountant/home";
    }

    @GetMapping("/search-partner")
    public String searchPartnerPage(@RequestParam(value = "namePartner", required = false) String namePartner, HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.ACCOUNTANT)) return "redirect:" + appUrls.getLogin();
        model.addAttribute("username", session.getAttribute("username"));

        if (namePartner == null || namePartner.trim().isEmpty()) {
            return appUrls.getSearch().get("accountant");
        }

        Integer employeeId = (Integer) session.getAttribute("employeeId");
        model.addAttribute("namePartner", namePartner);
        model.addAttribute("partners", partnerService.searchPartnersByNamePartner(namePartner, employeeId));

        return appUrls.getSearch().get("accountant");
    }

    @GetMapping("/list-contract/{partnerId}")
    public String listContract(@PathVariable Integer partnerId, HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.ACCOUNTANT)) return "redirect:" + appUrls.getLogin();

        try {
            PartnerResponse partner = partnerService.getPartnerResponseById(partnerId);
            List<ContractResponse> contracts = contractService.getAllContractsByPartnerId(partnerId);
            model.addAttribute("partner", partner);
            model.addAttribute("contracts", contracts);
            model.addAttribute("username", session.getAttribute("username"));

            return appUrls.getList().get("list-contract");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:" + appUrls.getSearch().get("accountant");
        }
    }

    @GetMapping("/detail-contract/{contractId}")
    public String detailContract(@PathVariable Integer contractId, HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.ACCOUNTANT))
            return "redirect:" + appUrls.getLogin();

        ContractDetailResponse contract = contractService.getContractDetailById(contractId);

        model.addAttribute("contract", contract);
        model.addAttribute("username", session.getAttribute("username"));

        return appUrls.getUpdate().get("update-invoice");
    }

    @PostMapping("/create-invoice-preview")
    public String createInvoice(@RequestParam Integer contractId,
                                @RequestParam List<Integer> shiftIds,
                                @RequestParam PaymentMethod paymentMethod,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        if (!authService.checkAuthentication(session, EmployeePosition.ACCOUNTANT))
            return "redirect:" + appUrls.getLogin();

        try {
            Integer accountantId = (Integer) session.getAttribute("employeeId");

            InvoiceCreateRequest request = InvoiceCreateRequest.builder()
                    .contractId(contractId)
                    .shiftIds(shiftIds)
                    .paymentMethod(paymentMethod)
                    .build();

            InvoiceConfirmResponse invoiceConfirmResponse = invoiceService.createInvoice(request, accountantId);

            session.setAttribute("invoicePreview", invoiceConfirmResponse);
            session.setAttribute("selectedShiftIds", shiftIds);

            return "redirect:/accountant/confirm-invoice";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/accountant/detail-contract/" + contractId;
        }
    }

    @GetMapping("/confirm-invoice")
    public String confirmInvoicePage(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.ACCOUNTANT))
            return "redirect:" + appUrls.getLogin();

        InvoiceConfirmResponse response = (InvoiceConfirmResponse) session.getAttribute("invoicePreview");

        if (response == null) {
            return "redirect:/accountant/home";
        }

        model.addAttribute("preview", response);
        model.addAttribute("username", session.getAttribute("username"));

        return "accountant/confirm-invoice";
    }

    @PostMapping("/save-invoice")
    public String saveInvoice(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.ACCOUNTANT))
            return "redirect:" + appUrls.getLogin();

        InvoiceConfirmResponse response = (InvoiceConfirmResponse) session.getAttribute("invoicePreview");
        List<Integer> shiftIds = (List<Integer>) session.getAttribute("selectedShiftIds");

        if (response == null || shiftIds == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không có dữ liệu hóa đơn.");
            return "redirect:/accountant/home";
        }

        try {
            Integer accountantId = (Integer) session.getAttribute("employeeId");

            InvoiceResponse invoice = invoiceService.saveInvoice(response, shiftIds, accountantId);

            session.removeAttribute("invoicePreview");
            session.removeAttribute("selectedShiftIds");

            redirectAttributes.addFlashAttribute("successMessage", "Lưu hóa đơn thành công.");
            return "redirect:/accountant/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/accountant/confirm-invoice";
        }
    }
}
