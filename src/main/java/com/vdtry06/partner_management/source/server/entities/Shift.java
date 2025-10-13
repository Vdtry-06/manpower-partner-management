package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Date;

@Table(name = "tblShift", schema = "public")
@Builder
@Entity
public class Shift implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "work_date")
    private Date workDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "worker_count")
    private Integer workerCount;

    @Column(name = "shift_unit_price")
    private Integer shiftUnitPrice;

    @Column(name = "remaining_amount")
    private Integer remainingAmount;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "task_contract_id")
    private TaskContract taskContractId;

    public Shift() {}

    public Shift(Integer id, Date workDate, LocalTime startTime, LocalTime endTime, Integer workerCount, Integer shiftUnitPrice, Integer remainingAmount, String description, TaskContract taskContractId) {
        this.id = id;
        this.workDate = workDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.workerCount = workerCount;
        this.shiftUnitPrice = shiftUnitPrice;
        this.remainingAmount = remainingAmount;
        this.description = description;
        this.taskContractId = taskContractId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(Integer workerCount) {
        this.workerCount = workerCount;
    }

    public Integer getShiftUnitPrice() {
        return shiftUnitPrice;
    }

    public void setShiftUnitPrice(Integer shiftUnitPrice) {
        this.shiftUnitPrice = shiftUnitPrice;
    }

    public Integer getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskContract getTaskContractId() {
        return taskContractId;
    }

    public void setTaskContractId(TaskContract taskContractId) {
        this.taskContractId = taskContractId;
    }
}
