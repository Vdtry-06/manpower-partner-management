package com.vdtry06.partner_management.source.server.dto.taskcontract;

import com.vdtry06.partner_management.source.server.dto.shift.ShiftResponse;
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
    private Integer id;
    private Integer taskId;
    private String taskName;
    private String taskDescription;
    private Long taskUnitPrice;
    private Integer totalValue;
    private Integer shiftCount;
    private List<ShiftResponse> shifts;

    public static TaskContractDetailResponse fromTaskContractResponse(TaskContractResponse tcr) {
        return TaskContractDetailResponse.builder()
                .id(tcr.getId())
                .taskId(tcr.getTaskId())
                .taskName(tcr.getTaskName())
                .taskDescription(tcr.getTaskDescription())
                .taskUnitPrice(tcr.getTaskUnitPrice())
                .totalValue(tcr.getTotalValue())
                .shiftCount(tcr.getShiftCount())
                .shifts(tcr.getShifts())
                .build();
    }
}