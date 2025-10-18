package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.payload.taskcontract.ShiftTaskListContractResponse;
import com.vdtry06.partner_management.source.server.payload.taskcontract.TaskContractResponse;
import com.vdtry06.partner_management.source.server.service.TaskContractService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task-contract")
public class TaskContractController {
    private final TaskContractService taskContractService;

    public TaskContractController(TaskContractService taskContractService) {
        this.taskContractService = taskContractService;
    }

    @GetMapping("/get-shift-list/{id}")
    public PaginationResponse<ShiftTaskListContractResponse> getShiftTaskListOfContract(
            @RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_SIZE) int perPage,
            @PathVariable int id
    ) {
        return taskContractService.getShiftTaskListOfContract(page, perPage, id);
    }

    @PostMapping("/contract/{contractId}/task/{taskId}")
    public ApiResponse<TaskContractResponse> createTaskContract(@PathVariable Integer contractId, @PathVariable Integer taskId) {
        return new ApiResponse<TaskContractResponse>(true, taskContractService.createTaskContract(contractId, taskId));
    }
}
