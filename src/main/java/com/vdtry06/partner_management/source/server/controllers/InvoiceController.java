package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.source.server.payload.invoice.ConfirmInvoiceResponse;
import com.vdtry06.partner_management.source.server.payload.invoice.InvoiceRequest;
import com.vdtry06.partner_management.source.server.payload.invoice.InvoiceResponse;
import com.vdtry06.partner_management.source.server.service.InvoiceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/get/{id}")
    public String getInvoice(@PathVariable int id, Model model) {
        ConfirmInvoiceResponse response = invoiceService.getInvoice(id);
        model.addAttribute("invoice", response);
        return "ConfirmInvoiceView";
    }

    @GetMapping("/update/{partnerId}")
    public String showUpdateInvoice(@PathVariable int partnerId, Model model) {
        // Load data if needed
        model.addAttribute("partnerId", partnerId);
        model.addAttribute("invoiceRequest", new InvoiceRequest());
        return "UpdateInvoiceView";
    }

    @PostMapping("/update/{partnerId}")
    public String updateInvoice(@PathVariable int partnerId, @ModelAttribute InvoiceRequest invoiceRequest, Model model) {
        InvoiceResponse response = invoiceService.updateInvoice(partnerId, invoiceRequest);
        model.addAttribute("invoice", response);
        return "redirect:/invoice/get/" + response.getId(); // Assuming id in response
    }

    @GetMapping("/add/{shiftId}")
    @PreAuthorize("hasAuthority('ACCOUNTANT')")
    public String showCreateInvoice(@PathVariable int shiftId, Model model) {
        model.addAttribute("shiftId", shiftId);
        return "UpdateInvoiceView"; // Reuse or specific view
    }

    @PostMapping("/add/{shiftId}")
    @PreAuthorize("hasAuthority('ACCOUNTANT')")
    public String createInvoice(@PathVariable int shiftId, Model model) {
        InvoiceResponse response = invoiceService.createInvoice(shiftId);
        model.addAttribute("invoice", response);
        return "ConfirmInvoiceView";
    }
}