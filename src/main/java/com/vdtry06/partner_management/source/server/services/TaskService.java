package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.source.server.dto.task.TaskRequest;
import com.vdtry06.partner_management.source.server.dto.task.TaskResponse;
import com.vdtry06.partner_management.source.server.entities.Task;
import com.vdtry06.partner_management.source.server.entities.TaskContract;
import com.vdtry06.partner_management.source.server.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public Integer createTask(TaskRequest taskRequest) {
        if (taskRepository.existsByNameTask(taskRequest.getNameTask())) {
            throw new RuntimeException("Tên đầu việc đã tồn tại!");
        }
        Task task = Task.builder()
                .nameTask(taskRequest.getNameTask())
                .description(taskRequest.getDescription())
                .build();
        taskRepository.save(task);
        return task.getId();
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(task -> toTaskResponse(task))
                .collect(Collectors.toList());
    }

    public Task getTaskById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id đầu việc"));
    }

    private TaskResponse toTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .nameTask(task.getNameTask())
                .description(task.getDescription())
                .build();
    }
}
