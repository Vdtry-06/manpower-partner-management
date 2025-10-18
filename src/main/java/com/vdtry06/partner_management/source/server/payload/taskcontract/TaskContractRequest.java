package com.vdtry06.partner_management.source.server.payload.taskcontract;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskContractRequest {
    private Integer taskUnitPrice;
}
