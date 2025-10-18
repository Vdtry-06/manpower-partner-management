package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;
import lombok.Builder;

import java.io.Serializable;

@Table(name = "tblTask", schema = "public")
@Builder
@Entity
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name_task", unique = false, length = 255)
    private String nameTask;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    public Task() {}

    public Task(Integer id, String nameTask, String description) {
        this.id = id;
        this.nameTask = nameTask;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
