package com.vdtry06.partner_management.source.server.entities;

import com.vdtry06.partner_management.lib.enumerated.ContractStatus;
import jakarta.persistence.*;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDate;

@Table(name = "tblContract", schema = "public")
@Builder
@Entity
public class Contract implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "contract_name", length = 255)
    private String contractName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_contract_value", length = 20)
    private Integer totalContractValue;

    @Column(name = "contract_status")
    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "partner_manager_id")
    private PartnerManager partnerManagerId;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partnerId;

    public Contract() {}

    public Contract(Integer id, String contractName, LocalDate startDate, LocalDate endDate, Integer totalContractValue, ContractStatus status, String description, PartnerManager partnerManagerId, Partner partnerId) {
        this.id = id;
        this.contractName = contractName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalContractValue = totalContractValue;
        this.status = status;
        this.description = description;
        this.partnerManagerId = partnerManagerId;
        this.partnerId = partnerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getTotalContractValue() {
        return totalContractValue;
    }

    public void setTotalContractValue(Integer totalContractValue) {
        this.totalContractValue = totalContractValue;
    }

    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PartnerManager getPartnerManagerId() {
        return partnerManagerId;
    }

    public void setPartnerManagerId(PartnerManager partnerManagerId) {
        this.partnerManagerId = partnerManagerId;
    }

    public Partner getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Partner partnerId) {
        this.partnerId = partnerId;
    }
}
