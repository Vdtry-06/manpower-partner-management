package com.vdtry06.partner_management.source.server.dto.shift_task_contract;

import com.vdtry06.partner_management.source.server.entities.Shift;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftTaskContractRequest {
    private Shift shiftId;

    private Integer workerCount;

    private Long shiftUnitPrice;
}
