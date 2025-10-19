package com.vdtry06.partner_management.source.server.payload.invoice;

import com.vdtry06.partner_management.source.server.payload.shift.ShiftResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmInvoiceResponse {
    private InvoiceResponse invoiceResponse;
    private ShiftResponse shiftResponse;
    private String namePatner;
    private String nameContract;
    private String nameAccountant;
}
