package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftRequest;
import com.vdtry06.partner_management.source.server.payload.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.service.ShiftService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/shift")
public class ShiftController {
    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @GetMapping("/{taskContractId}")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public String showCreateShift(@PathVariable int taskContractId, Model model) {
        model.addAttribute("taskContractId", taskContractId);
        model.addAttribute("shiftRequest", new ShiftRequest());
        return "AddShiftView";
    }

    @PostMapping("/{taskContractId}")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public String createShift(@PathVariable int taskContractId, @ModelAttribute ShiftRequest shiftRequest, Model model) {
        ShiftResponse response = shiftService.createShift(taskContractId, shiftRequest);
        model.addAttribute("shift", response);
        // Business flow: after shift, can back to add more taskcontract or confirm
        return "redirect:/confirm-contract"; // Or list
    }

    @GetMapping("/{id}")
    public String getAllShiftByTaskContractId(
            @RequestParam(defaultValue = PagingUtil.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PagingUtil.DEFAULT_SIZE) int perPage,
            @PathVariable int id,
            Model model
    ) {
        PaginationResponse<ShiftResponse> pagination = shiftService.getAllShiftByTaskContractId(page, perPage, id);
        model.addAttribute("shifts", pagination.getData());
        return "AddShiftView"; // Or list view
    }
}