package com.vdtry06.partner_management.source.server.payload.task;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    private String nameTask;
    private String description;
}
