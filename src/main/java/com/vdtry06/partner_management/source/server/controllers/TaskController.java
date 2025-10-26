package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.payload.task.TaskRequest;
import com.vdtry06.partner_management.source.server.payload.task.TaskResponse;
import com.vdtry06.partner_management.source.server.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable int id, Model model) {
        TaskResponse response = taskService.getTaskById(id);
        model.addAttribute("task", response);
        return "AddTaskView"; // Or detail
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public String showCreateTask(Model model) {
        model.addAttribute("taskRequest", new TaskRequest());
        return "AddTaskView";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('PARTNER_MANAGER')")
    public String createTask(@ModelAttribute TaskRequest taskRequest, Model model) {
        TaskResponse response = taskService.createTask(taskRequest);
        model.addAttribute("task", response);
        return "redirect:/list-task"; // Back to list
    }

    @GetMapping
    public String getAllTasks(
            @RequestParam(defaultValue = PagingUtil.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PagingUtil.DEFAULT_SIZE) int perPage,
            Model model
    ) {
        PaginationResponse<TaskResponse> pagination = taskService.getAllTasks(page, perPage);
        model.addAttribute("tasks", pagination.getData());
        return "ListTaskView";
    }
}