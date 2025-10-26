package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.source.server.config.language.DetectLanguage;
import com.vdtry06.partner_management.source.server.payload.partnermanager.PartnerManagerResponse;
import com.vdtry06.partner_management.source.server.service.PartnerManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/partner-manager")
@RequiredArgsConstructor
public class PartnerManagerController {
    private final PartnerManagerService partnerManagerService;

    @GetMapping
    @DetectLanguage
    public String getPartnerManagerInfo(Model model) {
        PartnerManagerResponse response = partnerManagerService.getPartnerManagerInfor();
        model.addAttribute("fullname", response.getFullname());
        model.addAttribute("username", response.getUsername());
        return "PartnerManagerHomeView"; // Options: Manage Partner, Sign Contract
    }

    @GetMapping("/sign-contract")
    @DetectLanguage
    public String showSignContract() {
        return "SearchPartnerView"; // Bắt đầu flow ký hợp đồng
    }
}