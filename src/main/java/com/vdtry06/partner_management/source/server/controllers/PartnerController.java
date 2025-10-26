package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.source.server.payload.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.payload.partner.PartnerResponse;
import com.vdtry06.partner_management.source.server.service.PartnerService;
import com.vdtry06.partner_management.source.server.config.language.DetectLanguage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    @GetMapping("/manage")
    @DetectLanguage
    public String showPartnerManagementView() {
        return "PartnerManagementView";
    }

    @GetMapping("/add")
    @DetectLanguage
    public String showCreatePartner(Model model) {
        model.addAttribute("partnerRequest", new PartnerRequest());
        return "AddPartnerView";
    }

    @PostMapping("/add")
    @DetectLanguage
    public String createPartner(@ModelAttribute PartnerRequest partnerRequest, Model model) {
        try {
            PartnerResponse response = partnerService.createPartner(partnerRequest);
            model.addAttribute("partner", response);
            return "redirect:/partner/manage"; // Quay lại trang quản lý sau khi thêm
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi thêm đối tác: " + e.getMessage());
            return "AddPartnerView";
        }
    }
}