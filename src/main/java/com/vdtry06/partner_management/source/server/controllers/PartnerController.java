package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.source.server.payload.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.payload.partner.PartnerResponse;
import com.vdtry06.partner_management.source.server.service.PartnerService;
import org.apache.coyote.BadRequestException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partner")
public class PartnerController {
    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping("/add/{id}")
    public ApiResponse<PartnerResponse> createPartner(@PathVariable Integer id, @RequestBody PartnerRequest partnerRequest) {
        return new ApiResponse<PartnerResponse>(true, partnerService.createPartner(id, partnerRequest));
    }
}
