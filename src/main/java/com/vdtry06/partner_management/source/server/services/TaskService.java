package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.source.server.dto.task.TaskRequest;
import com.vdtry06.partner_management.source.server.dto.task.TaskResponse;
import com.vdtry06.partner_management.source.server.entities.Task;
import com.vdtry06.partner_management.source.server.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Task getTaskById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Transactional
    public Task createTask(TaskRequest request) {
        // Kiểm tra trùng tên
        if (taskRepository.existsByNameTask(request.getNameTask())) {
            throw new RuntimeException("Đầu việc với tên này đã tồn tại!");
        }

        Task task = Task.builder()
                .nameTask(request.getNameTask())
                .description(request.getDescription())
                .build();

        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Integer id, TaskRequest request) {
        Task task = getTaskById(id);

        // Kiểm tra trùng tên (nếu đổi tên)
        if (!task.getNameTask().equals(request.getNameTask())
                && taskRepository.existsByNameTask(request.getNameTask())) {
            throw new RuntimeException("Đầu việc với tên này đã tồn tại!");
        }

        task.setNameTask(request.getNameTask());
        task.setDescription(request.getDescription());

        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Integer id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public List<TaskResponse> searchTasksByKeyword(String keyword) {
        return taskRepository.findByNameTaskContainingIgnoreCase(keyword).stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }
}