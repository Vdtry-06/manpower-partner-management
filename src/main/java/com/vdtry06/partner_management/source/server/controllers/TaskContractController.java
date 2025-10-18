package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.source.server.payload.taskcontract.TaskContractResponse;
import com.vdtry06.partner_management.source.server.service.TaskContractService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task-contract")
public class TaskContractController {
    private final TaskContractService taskContractService;

    public TaskContractController(TaskContractService taskContractService) {
        this.taskContractService = taskContractService;
    }

    @PostMapping("/{contractId}/{taskId}")
    public ApiResponse<TaskContractResponse> createTaskContract(@PathVariable Integer contractId, @PathVariable Integer taskId) {
        return new ApiResponse<TaskContractResponse>(true, taskContractService.createTaskContract(contractId, taskId));
    }
}
