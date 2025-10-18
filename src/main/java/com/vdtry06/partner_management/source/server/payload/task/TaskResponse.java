package com.vdtry06.partner_management.source.server.payload.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {
    private Integer id;
    private String nameTask;
    private String description;
}
