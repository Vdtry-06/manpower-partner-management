package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import com.vdtry06.partner_management.source.server.dto.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.entities.Partner;
import com.vdtry06.partner_management.source.server.services.PartnerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/partner-manager")
@RequiredArgsConstructor
public class PartnerManagerController {

    private final PartnerService partnerService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // Kiểm tra đăng nhập
        if (session.getAttribute("employeeId") == null) {
            return "redirect:/auth/login";
        }

        // Kiểm tra quyền
        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        if (position != EmployeePostion.PARTNER_MANAGER) {
            return "redirect:/auth/login";
        }

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("username", session.getAttribute("username"));

        return "partner-manager/home";
    }

    @GetMapping("/partners")
    public String listPartners(HttpSession session, Model model) {
        // Kiểm tra đăng nhập và quyền
        if (session.getAttribute("employeeId") == null) {
            return "redirect:/auth/login";
        }

        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        if (position != EmployeePostion.PARTNER_MANAGER) {
            return "redirect:/auth/login";
        }

        Integer employeeId = (Integer) session.getAttribute("employeeId");

        // Sử dụng Response DTO để tránh lỗi LocalDate
        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("partners", partnerService.getPartnersByManagerIdResponse(employeeId));

        return "partner-manager/partner-list";
    }

    @GetMapping("/partners/add")
    public String showAddPartnerForm(HttpSession session, Model model) {
        // Kiểm tra đăng nhập và quyền
        if (session.getAttribute("employeeId") == null) {
            return "redirect:/auth/login";
        }

        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        if (position != EmployeePostion.PARTNER_MANAGER) {
            return "redirect:/auth/login";
        }

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("partnerRequest", new PartnerRequest());

        return "partner-manager/add-partner";
    }

    @PostMapping("/partners/add")
    public String addPartner(@Valid @ModelAttribute PartnerRequest partnerRequest,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        // Kiểm tra đăng nhập và quyền
        if (session.getAttribute("employeeId") == null) {
            return "redirect:/auth/login";
        }

        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        if (position != EmployeePostion.PARTNER_MANAGER) {
            return "redirect:/auth/login";
        }

        // Kiểm tra validation errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("fullname", session.getAttribute("fullname"));
            return "partner-manager/add-partner";
        }

        try {
            Integer employeeId = (Integer) session.getAttribute("employeeId");
            partnerService.createPartner(partnerRequest, employeeId);

            redirectAttributes.addFlashAttribute("successMessage", "Thêm đối tác thành công!");
            return "redirect:/partner-manager/partners";
        } catch (Exception e) {
            model.addAttribute("fullname", session.getAttribute("fullname"));
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            return "partner-manager/add-partner";
        }
    }
}
