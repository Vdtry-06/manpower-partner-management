package com.vdtry06.partner_management.source.server.dto.invoice;

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
public class InvoiceCreateRequest {
    private Integer contractId;
    private List<Integer> shiftIds;
    private PaymentMethod paymentMethod;
    private Long paymentAmount;
}
