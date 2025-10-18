package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.payload.task.TaskRequest;
import com.vdtry06.partner_management.source.server.payload.task.TaskResponse;
import com.vdtry06.partner_management.source.server.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> getTaskById(@PathVariable int id) {
        return new ApiResponse<TaskResponse>(true, taskService.getTaskById(id));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public ApiResponse<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        return new ApiResponse<TaskResponse>(true, taskService.createTask(taskRequest));
    }

    @GetMapping
    public PaginationResponse<TaskResponse> getAllTasks(
            @RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_PAGE) int page,
            @RequestParam(required = false, defaultValue = PagingUtil.DEFAULT_SIZE) int perPage
    ) {
        return taskService.getAllTasks(page, perPage);
    }
}
