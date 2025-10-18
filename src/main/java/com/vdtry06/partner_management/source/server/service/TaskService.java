package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.lib.api.PaginationResponse;
import com.vdtry06.partner_management.lib.repository.BaseRepository;
import com.vdtry06.partner_management.lib.service.BaseService;
import com.vdtry06.partner_management.lib.utils.PagingUtil;
import com.vdtry06.partner_management.source.server.entities.Task;
import com.vdtry06.partner_management.source.server.payload.task.TaskRequest;
import com.vdtry06.partner_management.source.server.payload.task.TaskResponse;
import com.vdtry06.partner_management.source.server.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService extends BaseService<Task, Integer> {
    private final TaskRepository taskRepository;

    public TaskService(BaseRepository<Task, Integer> repository, TaskRepository taskRepository) {
        super(repository);
        this.taskRepository = taskRepository;
    }


    public TaskResponse getTaskById(Integer taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        return toTaskResponse(task);
    }

    protected String getNameTask(Integer taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        return task.getNameTask();
    }

    public TaskResponse createTask(TaskRequest taskRequest) {
        if (taskRepository.existsByNameTask(taskRequest.getNameTask())) {
            throw new RuntimeException("Tên đầu việc đã có trong hệ thống");
        }

        Task task = toTask(taskRequest);
        task.setNameTask(taskRequest.getNameTask());
        task.setDescription(taskRequest.getDescription());

        task = taskRepository.save(task);
        return toTaskResponse(task);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<TaskResponse> getAllTasks(int page, int perPage) {
        long totalRecord = taskRepository.countAllTask();
        int offset = PagingUtil.getOffSet(page, perPage);
        int totalPage = PagingUtil.getTotalPage(totalRecord, perPage);
        List<Task> tasks = taskRepository.findAllTask(offset, perPage);
        List<TaskResponse> taskResponses = new ArrayList<>();
        if (tasks != null) {
            taskResponses = tasks
                    .stream()
                    .map(task -> toTaskResponse(task))
                    .toList()
            ;
        }
        return PaginationResponse.<TaskResponse>builder()
                .page(page)
                .perPage(perPage)
                .data(taskResponses)
                .totalPage(totalPage)
                .totalRecord(totalRecord)
                .build();
    }

    private Task toTask(TaskRequest taskRequest) {
        new Task();
        return Task.builder()
                .nameTask(taskRequest.getNameTask())
                .description(taskRequest.getDescription())
                .build();
    }

    private TaskResponse toTaskResponse(Task task) {
        new TaskResponse();
        return TaskResponse.builder()
                .id(task.getId())
                .nameTask(task.getNameTask())
                .description(task.getDescription())
                .build();
    }
}
