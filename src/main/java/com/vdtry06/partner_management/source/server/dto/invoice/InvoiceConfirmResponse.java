package com.vdtry06.partner_management.source.server.dto.invoice;

import com.vdtry06.partner_management.lib.enumerated.InvoiceStatus;
import com.vdtry06.partner_management.lib.enumerated.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceConfirmResponse {
    private Integer invoiceId;
    private Integer contractId;
    private String contractName;
    private String partnerName;
    private String partnerRepresentative;
    private String partnerPhone;
    private String accountantName;
    private List<ShiftInvoiceDetail> shifts;
    private Long totalAmount;
    private Long paymentAmount;
    private Long remainingAmount;
    private InvoiceStatus invoiceStatus;
    private PaymentMethod paymentMethod;
    private String invoiceDate;
}
