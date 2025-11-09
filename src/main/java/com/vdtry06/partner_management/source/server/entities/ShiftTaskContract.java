package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Table(name = "tblShiftTaskContract", schema = "public")
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShiftTaskContract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "task_contract_id")
    private TaskContract taskContractId;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shiftId;
}
