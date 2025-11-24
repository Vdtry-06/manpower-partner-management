package com.vdtry06.partner_management.source.server.dto.task_contract;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class TaskContractSelectionResponse {
    List<TaskContractResponse> selectedTasks;
    Map<Integer, Integer> taskContractMap;
    Map<Integer, Integer> shiftTaskContractMap;
}
