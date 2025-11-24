package com.vdtry06.partner_management.source.server.dto.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftInvoiceDetail {
    private Integer shiftId;
    private String workDate;
    private String startTime;
    private String endTime;
    private Integer workerCount;
    private Long shiftUnitPrice;
    private Integer remainingAmountBefore;
    private Integer remainingAmountAfter;
    private Long totalValue;
}
