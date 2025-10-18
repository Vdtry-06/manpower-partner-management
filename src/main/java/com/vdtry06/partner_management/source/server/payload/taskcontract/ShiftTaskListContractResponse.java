package com.vdtry06.partner_management.source.server.payload.taskcontract;

import com.vdtry06.partner_management.source.server.payload.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.payload.task.TaskResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftTaskListContractResponse {
    private Integer taskContractId;
    private TaskResponse taskResponse;
    private ShiftResponse shiftResponse;
}
