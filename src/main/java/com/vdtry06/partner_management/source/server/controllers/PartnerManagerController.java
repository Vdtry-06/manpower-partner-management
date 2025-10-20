package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.source.server.payload.partnermanager.PartnerManagerResponse;
import com.vdtry06.partner_management.source.server.service.PartnerManagerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/partner-manager")
public class PartnerManagerController {
    private final PartnerManagerService partnerManagerService;

    public PartnerManagerController(PartnerManagerService partnerManagerService) {
        this.partnerManagerService = partnerManagerService;
    }

    @GetMapping
    public ApiResponse<PartnerManagerResponse> getPartnerManagerInfor() {
        return new ApiResponse<>(true, partnerManagerService.getPartnerManagerInfor());
    }
}
