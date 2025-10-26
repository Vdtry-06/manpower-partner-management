package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.source.server.config.language.DetectLanguage;
import com.vdtry06.partner_management.source.server.payload.accountant.AccountantResponse;
import com.vdtry06.partner_management.source.server.service.AccountantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accountant")
@RequiredArgsConstructor
public class AccountantController {
    private final AccountantService accountantService;

    @GetMapping
    @DetectLanguage
    public String getAccountantInfo(Model model) {
        AccountantResponse response = accountantService.getAccountantInfor();
        model.addAttribute("fullname", response.getFullname());
        model.addAttribute("username", response.getUsername());
        return "AccountantHomeView";
    }

    @GetMapping("/receive-payment")
    @DetectLanguage
    public String showReceivePaymentPage() {
        return "SearchPartnerFromPartner";
    }
}