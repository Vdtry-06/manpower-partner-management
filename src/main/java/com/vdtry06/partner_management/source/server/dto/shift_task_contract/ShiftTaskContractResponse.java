package com.vdtry06.partner_management.source.server.dto.shift_task_contract;

import com.vdtry06.partner_management.source.server.entities.Shift;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftTaskContractResponse {
    private Integer id;
    private Integer workerCount;
    private Long shiftUnitPrice;
    private TaskContract taskContractId;
    private Shift shiftId;
}
