package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.payload.taskcontract.ShiftTaskListContractResponse;
import com.vdtry06.partner_management.source.server.payload.taskcontract.TaskContractResponse;
import com.vdtry06.partner_management.source.server.service.TaskContractService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/task-contract")
public class TaskContractController {
    private final TaskContractService taskContractService;

    public TaskContractController(TaskContractService taskContractService) {
        this.taskContractService = taskContractService;
    }

    @GetMapping("/get-shift-list/{id}")
    public String getShiftTaskListOfContract(
            @RequestParam(defaultValue = PagingUtil.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PagingUtil.DEFAULT_SIZE) int perPage,
            @PathVariable int id,
            Model model
    ) {
        PaginationResponse<ShiftTaskListContractResponse> pagination = taskContractService.getShiftTaskListOfContract(page, perPage, id);
        model.addAttribute("shifts", pagination.getData());
        return "AddShiftView"; // List shifts
    }

    @PostMapping("/contract/{contractId}/task/{taskId}")
    public String createTaskContract(@PathVariable Integer contractId, @PathVariable Integer taskId, Model model) {
        TaskContractResponse response = taskContractService.createTaskContract(contractId, taskId);
        model.addAttribute("taskContract", response);
        return "redirect:/shift/" + response.getId(); // Next to add shift
    }
}