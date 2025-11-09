package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Table(name = "tblTask", schema = "public")
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name_task")
    private String nameTask;

    @Column(name = "description")
    private String description;
}