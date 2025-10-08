package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;
import lombok.Builder;

import java.io.Serializable;

@Table(name = "tblTaskContract", schema = "public")
@Builder
@Entity
public class TaskContract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task_unit_price")
    private Integer taskUnitPrice;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contractId;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task taskId;

    public TaskContract() {}

    public TaskContract(Integer id, Integer taskUnitPrice, Contract contractId, Task taskId) {
        this.id = id;
        this.taskUnitPrice = taskUnitPrice;
        this.contractId = contractId;
        this.taskId = taskId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskUnitPrice() {
        return taskUnitPrice;
    }

    public void setTaskUnitPrice(Integer taskUnitPrice) {
        this.taskUnitPrice = taskUnitPrice;
    }

    public Contract getContractId() {
        return contractId;
    }

    public void setContractId(Contract contractId) {
        this.contractId = contractId;
    }

    public Task getTaskId() {
        return taskId;
    }

    public void setTaskId(Task taskId) {
        this.taskId = taskId;
    }
}
