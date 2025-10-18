package com.vdtry06.partner_management.source.server.payload.invoice;

import com.vdtry06.partner_management.lib.enumerated.PaymentMethod;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    private PaymentMethod paymentMethod;
    private Integer paymentAmount;
}
