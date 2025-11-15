package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.enumerated.EmployeePosition;
import com.vdtry06.partner_management.source.server.config.AppUrls;
import com.vdtry06.partner_management.source.server.dto.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.services.AuthService;
import com.vdtry06.partner_management.source.server.services.PartnerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/partner-manager")
@RequiredArgsConstructor
public class PartnerManagerController {
    private final AuthService authService;
    private final PartnerService partnerService;
    private final AppUrls appUrls;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("username", session.getAttribute("username"));

        return appUrls.getHome().get("partner-manager");
    }

    // Partner Management
    @GetMapping("/partner-management")
    public String showPartnerManagement(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        model.addAttribute("username", session.getAttribute("username"));

        return appUrls.getManagement().get("partner");
    }

    @GetMapping("/partner/add")
    public String showAddPartnerForm(HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("partnerRequest", new PartnerRequest());

        return appUrls.getCreate().get("create-partner");
    }

    @PostMapping("/add-partner")
    public String addPartner(@Valid @ModelAttribute PartnerRequest request, BindingResult result, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();

        if (result.hasErrors()) {
            model.addAttribute("username", session.getAttribute("username"));
            return appUrls.getCreate().get("partner-manager");
        }

        try {
            Integer employeeId = (Integer) session.getAttribute("employeeId");
            partnerService.createPartner(request, employeeId);

            redirectAttributes.addFlashAttribute("successMessage", "Add partner Successful");
            return "redirect:" + appUrls.getHome().get("partner-manager");
        } catch (Exception e) {
            model.addAttribute("username", session.getAttribute("username"));
            model.addAttribute("errorMessage", "Error message: " + e.getMessage());
            return appUrls.getCreate().get("create-partner");
        }
    }

    // Partner manager Sign contract with partner
    @GetMapping("/search-partner")
    public String searchPartnerPage(@RequestParam(value = "namePartner", required = false) String namePartner, HttpSession session, Model model) {
        if (!authService.checkAuthentication(session, EmployeePosition.PARTNER_MANAGER)) return "redirect:" + appUrls.getLogin();
        model.addAttribute("username", session.getAttribute("username"));

        if (namePartner == null || namePartner.trim().isEmpty()) {
            return appUrls.getSearch().get("search-partner");
        }

        Integer employeeId = (Integer) session.getAttribute("employeeId");
        model.addAttribute("namePartner", namePartner);
        model.addAttribute("partners", partnerService.searchPartnersByNamePartner(namePartner, employeeId));

        return appUrls.getSearch().get("search-partner");
    }

}