package com.vdtry06.partner_management.source.server.payload.invoice;

import com.vdtry06.partner_management.lib.enumerated.InvoiceStatus;
import com.vdtry06.partner_management.lib.enumerated.PaymentMethod;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    private Integer id;
    private LocalDateTime invoiceDate;
    private Integer paymentAmount;
    private PaymentMethod paymentMethod;
    private InvoiceStatus invoiceStatus;
    private Integer accountantId;
    private Integer shiftId;
    private Integer remainingAmount;
}
