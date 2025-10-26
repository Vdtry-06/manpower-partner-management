package com.vdtry06.partner_management.source.server.dto.taskcontract;

import com.vdtry06.partner_management.source.server.dto.shift.ShiftResponse;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.repositories.ShiftRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private Integer totalValue;
    private Integer shiftCount;
    private List<ShiftResponse> shifts;

    public static TaskContractResponse fromEntity(TaskContract taskContract, ShiftRepository shiftRepository) {
        Integer totalValue = shiftRepository.calculateTotalValueByTaskContract(taskContract.getId());
        Integer shiftCount = shiftRepository.countByTaskContractId_Id(taskContract.getId());

        List<ShiftResponse> shifts = shiftRepository.findByTaskContractId_Id(taskContract.getId())
                .stream()
                .map(ShiftResponse::fromEntity)
                .collect(Collectors.toList());

        return TaskContractResponse.builder()
                .id(taskContract.getId())
                .taskId(taskContract.getTaskId().getId())
                .taskName(taskContract.getTaskId().getNameTask())
                .taskDescription(taskContract.getTaskId().getDescription())
                .taskUnitPrice(taskContract.getTaskUnitPrice())
                .totalValue(totalValue != null ? totalValue : 0)
                .shiftCount(shiftCount)
                .shifts(shifts)
                .build();
    }
}