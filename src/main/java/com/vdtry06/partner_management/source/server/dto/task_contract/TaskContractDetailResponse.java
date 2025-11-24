package com.vdtry06.partner_management.source.server.dto.task_contract;

import com.vdtry06.partner_management.source.server.dto.shift.ShiftDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskContractDetailResponse {
    private Integer taskContractId;
    private Integer taskId;
    private String taskName;
    private String taskDescription;
    private List<ShiftDetailResponse> shifts;
    private Long totalValue;
}
