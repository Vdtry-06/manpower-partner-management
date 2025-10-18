package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.source.server.payload.invoice.ConfirmInvoiceResponse;
import com.vdtry06.partner_management.source.server.payload.invoice.InvoiceRequest;
import com.vdtry06.partner_management.source.server.payload.invoice.InvoiceResponse;
import com.vdtry06.partner_management.source.server.service.InvoiceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/get/{id}")
    public ApiResponse<ConfirmInvoiceResponse> getInvoice(@PathVariable int id) {
        return new ApiResponse<ConfirmInvoiceResponse>(true, invoiceService.getInvoice(id));
    }

    @PostMapping("/update/{partnerId}")
    public ApiResponse<InvoiceResponse> updateInvoice(@PathVariable int partnerId, @RequestBody InvoiceRequest invoiceRequest) {
        return new ApiResponse<InvoiceResponse>(true, invoiceService.updateInvoice(partnerId, invoiceRequest));
    }

    @PostMapping("/add/{shiftId}")
    @PreAuthorize("hasAuthority('ACCOUNTANT')")
    public ApiResponse<InvoiceResponse> createInvoice(@PathVariable int shiftId) {
        return new ApiResponse<InvoiceResponse>(true, invoiceService.createInvoice(shiftId));
    }
}
