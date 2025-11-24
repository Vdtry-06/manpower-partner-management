package com.vdtry06.partner_management.source.server.dto.task_contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskContractResponse {
    private Integer id;
    private Integer taskId;
    private String taskName;
    private String taskDescription;
    private Long taskUnitPrice;
}
