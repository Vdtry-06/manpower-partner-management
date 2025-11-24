package com.vdtry06.partner_management.source.server.dto.shift;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftInvoiceResponse {
    private Integer id;
    private String workDate;
    private String shiftType;
    private String startTime;
    private String endTime;
    private boolean paid;
    private Integer invoiceId;
}
