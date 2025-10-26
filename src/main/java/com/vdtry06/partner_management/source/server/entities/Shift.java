package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "tblShift", schema = "public")
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shift implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "work_date")
    private LocalDate workDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "worker_count")
    private Integer workerCount;

    @Column(name = "shift_unit_price")
    private Long shiftUnitPrice;

    @Column(name = "remaining_amount")
    private Integer remainingAmount;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "task_contract_id")
    private TaskContract taskContractId;
}
