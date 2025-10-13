package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.lib.utils.StringUtil;
import com.vdtry06.partner_management.source.server.payload.partner.PartnerRequest;
import com.vdtry06.partner_management.source.server.payload.partner.PartnerResponse;
import com.vdtry06.partner_management.source.server.service.PartnerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partner")
public class PartnerController {
    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public ApiResponse<PartnerResponse> createPartner(@RequestBody PartnerRequest partnerRequest) {
        return new ApiResponse<PartnerResponse>(true, partnerService.createPartner(partnerRequest));
    }

    @GetMapping
    public PaginationResponse<PartnerResponse> getAllPartnersWithConditions(
            @RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_SIZE) int perPage,
            @RequestParam(required = false, defaultValue = StringUtil.EMPTY) String search ) {
        return partnerService.getAllPartnersWithConditions(page, perPage, search);
    }
}
