package com.vdtry06.partner_management.source.server.payload.taskcontract;

import com.vdtry06.partner_management.source.server.entities.Contract;
import com.vdtry06.partner_management.source.server.entities.Task;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskContractResponse {
    private Integer id;
    private Integer taskUnitPrice;
    private Contract contractId;
    private Task taskId;
}
