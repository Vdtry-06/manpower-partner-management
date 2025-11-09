package com.vdtry06.partner_management.source.server.entities;

import com.vdtry06.partner_management.lib.enumerated.ContractStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Table(name = "tblContract", schema = "public")
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "contract_name", nullable = false, length = 255)
    private String contractName;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "total_contract_value", length = 20)
    private Integer totalContractValue;

    @Column(name = "contract_status")
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "partner_manager_id")
    private PartnerManager partnerManagerId;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partnerId;
}