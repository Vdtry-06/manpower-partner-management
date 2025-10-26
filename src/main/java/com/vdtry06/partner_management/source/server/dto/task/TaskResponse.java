package com.vdtry06.partner_management.source.server.dto.task;

import com.vdtry06.partner_management.source.server.entities.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Integer id;
    private String nameTask;
    private String description;

    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .nameTask(task.getNameTask())
                .description(task.getDescription())
                .build();
    }
}