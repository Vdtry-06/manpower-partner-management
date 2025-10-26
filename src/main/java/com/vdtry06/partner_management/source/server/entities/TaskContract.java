package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Table(name = "tblTaskContract", schema = "public")
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskContract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task_unit_price")
    private Long taskUnitPrice;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contractId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task taskId;
}
