package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftRequest;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.service.ShiftService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/shift")
@RestController
public class ShiftController {
    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @PostMapping("/{taskContractId}")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public ApiResponse<ShiftResponse> createShift(@PathVariable int taskContractId, @RequestBody ShiftRequest shiftRequest) {
        return new ApiResponse<ShiftResponse>(true, shiftService.createShift(taskContractId, shiftRequest));
    }

    @GetMapping("/{id}")
    public PaginationResponse<ShiftResponse> getAllShiftByTaskContractId(
            @PathVariable int id
    ) {
        return shiftService.getAllShiftByTaskContractId(id);
    }
}
