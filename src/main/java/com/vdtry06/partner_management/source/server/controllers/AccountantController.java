package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.source.server.payload.accountant.AccountantResponse;
import com.vdtry06.partner_management.source.server.service.AccountantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accountant")
public class AccountantController {
    private final AccountantService accoutantService;

    public AccountantController(AccountantService accoutantService) {
        this.accoutantService = accoutantService;
    }

    @GetMapping
    public ApiResponse<AccountantResponse> getPartnerManagerInfor() {
        return new ApiResponse<>(true, accoutantService.getAccountantInfor());
    }
}
