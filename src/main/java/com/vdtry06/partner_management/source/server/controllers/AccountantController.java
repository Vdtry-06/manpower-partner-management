package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import com.vdtry06.partner_management.source.server.dto.contract.ContractDetailResponse;
import com.vdtry06.partner_management.source.server.dto.contract.ContractResponse;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceDetailResponse;
import com.vdtry06.partner_management.source.server.dto.invoice.InvoiceRequest;
import com.vdtry06.partner_management.source.server.dto.partner.PartnerResponse;
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

    private final PartnerService partnerService;
    private final ContractService contractService;
    private final InvoiceService invoiceService;

    // Kiểm tra quyền truy cập
    private boolean checkAccess(HttpSession session) {
        if (session.getAttribute("employeeId") == null) {
            return false;
        }
        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        return position == EmployeePostion.ACCOUNTANT;
    }

    @GetMapping("/home")
    public String dashboard(HttpSession session, Model model) {
        if (!checkAccess(session)) {
            return "redirect:/auth/login";
        }

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("username", session.getAttribute("username"));

        return "accountant/home";
    }

    // 1. Trang tìm kiếm đối tác
    @GetMapping("/search-partner")
    public String searchPartner(
            @RequestParam(required = false) String keyword,
            HttpSession session,
            Model model) {

        if (!checkAccess(session)) {
            return "redirect:/auth/login";
        }

        List<PartnerResponse> partners = null;
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Tìm kiếm theo keyword
            partners = partnerService.getAllPartnersResponse().stream()
                    .filter(p -> p.getNamePartner().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
            model.addAttribute("keyword", keyword);
            model.addAttribute("searched", true);
        }

        model.addAttribute("partners", partners);
        model.addAttribute("fullname", session.getAttribute("fullname"));

        return "accountant/search-partner";
    }

    // 2. Danh sách hợp đồng của đối tác
    @GetMapping("/list-contract/{partnerId}")
    public String listContract(
            @PathVariable Integer partnerId,
            HttpSession session,
            Model model) {

        if (!checkAccess(session)) {
            return "redirect:/auth/login";
        }

        try {
            // Lấy thông tin đối tác
            PartnerResponse partner = partnerService.getAllPartnersResponse().stream()
                    .filter(p -> p.getId().equals(partnerId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đối tác"));

            // Lấy danh sách hợp đồng của đối tác (chỉ lấy hợp đồng ACTIVE)
            List<ContractResponse> contracts = contractService.getAllContracts().stream()
                    .filter(c -> c.getPartnerName().equals(partner.getNamePartner()))
                    .filter(c -> c.getStatus().equals("ACTIVE"))
                    .toList();

            model.addAttribute("partner", partner);
            model.addAttribute("contracts", contracts);
            model.addAttribute("fullname", session.getAttribute("fullname"));

            return "accountant/list-contract";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/accountant/search-partner";
        }
    }

    // 3. Chi tiết hợp đồng với danh sách ca làm việc
    @GetMapping("/detail-contract/{contractId}")
    public String detailContract(
            @PathVariable Integer contractId,
            HttpSession session,
            Model model) {

        if (!checkAccess(session)) {
            return "redirect:/auth/login";
        }

        ContractDetailResponse contract = contractService.getContractDetail(contractId);

        model.addAttribute("contract", contract);
        model.addAttribute("fullname", session.getAttribute("fullname"));

        return "accountant/detail-contract";
    }

    // 4. Tạo và cập nhật hóa đơn cho ca làm việc
    @GetMapping("/update-invoice/{shiftId}")
    public String updateInvoice(
            @PathVariable Integer shiftId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!checkAccess(session)) {
            return "redirect:/auth/login";
        }

        try {
            Integer accountantId = (Integer) session.getAttribute("employeeId");

            // Tạo invoice nháp
            Integer invoiceId = invoiceService.createDraftInvoice(shiftId, accountantId);

            // Lấy thông tin invoice
            InvoiceDetailResponse invoice = invoiceService.getInvoiceDetail(invoiceId);

            model.addAttribute("invoice", invoice);
            model.addAttribute("fullname", session.getAttribute("fullname"));

            return "accountant/update-invoice";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/accountant/search-partner";
        }
    }

    // 5. Lưu thông tin hóa đơn
    @PostMapping("/save-invoice/{invoiceId}")
    public String saveInvoice(
            @PathVariable Integer invoiceId,
            @ModelAttribute InvoiceRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!checkAccess(session)) {
            return "redirect:/auth/login";
        }

        try {
            invoiceService.updateInvoice(invoiceId, request);
            return "redirect:/accountant/confirm-invoice/" + invoiceId;

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/accountant/update-invoice/" +
                    invoiceService.getInvoiceById(invoiceId).getShiftId().getId();
        }
    }

    // 6. Xác nhận hóa đơn
    @GetMapping("/confirm-invoice/{invoiceId}")
    public String confirmInvoice(
            @PathVariable Integer invoiceId,
            HttpSession session,
            Model model) {

        if (!checkAccess(session)) {
            return "redirect:/auth/login";
        }

        InvoiceDetailResponse invoice = invoiceService.getInvoiceDetail(invoiceId);

        model.addAttribute("invoice", invoice);
        model.addAttribute("fullname", session.getAttribute("fullname"));

        return "accountant/confirm-invoice";
    }

    // 7. Xác nhận cuối cùng và xuất hóa đơn
    @PostMapping("/finalize-invoice/{invoiceId}")
    public String finalizeInvoice(
            @PathVariable Integer invoiceId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!checkAccess(session)) {
            return "redirect:/auth/login";
        }

        try {
            invoiceService.confirmInvoice(invoiceId);
            redirectAttributes.addFlashAttribute("success", "Hóa đơn đã được xác nhận và xuất thành công! Nhân viên có thể in hóa đơn và nhận tiền từ đối tác.");
            return "redirect:/accountant/home";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/accountant/confirm-invoice/" + invoiceId;
        }
    }

    // Hủy hóa đơn nháp
    @PostMapping("/cancel-invoice/{invoiceId}")
    public String cancelInvoice(
            @PathVariable Integer invoiceId,
            @RequestParam Integer contractId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!checkAccess(session)) {
            return "redirect:/auth/login";
        }

        try {
            invoiceService.deleteInvoice(invoiceId);
            redirectAttributes.addFlashAttribute("success", "Đã hủy hóa đơn!");
            return "redirect:/accountant/detail-contract/" + contractId;

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/accountant/detail-contract/" + contractId;
        }
    }
}