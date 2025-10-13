package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

@Table(name = "tblPartner", schema = "public")
@Builder
@Entity
public class Partner implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "partner_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name_partner", nullable = false, length = 255)
    private String namePartner;

    @Column(name = "partner_representative", nullable = false, length = 255)
    private String partnerRepresentative;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "tax_code", length = 20)
    private String taxCode;

    @Column(name = "connperation_date")
    private Date connperationDate;

    @Column(name = "description", length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "partner_manager_id")
    private PartnerManager partnerManagerId;


    public Partner() {}

    public Partner(Integer id, String namePartner, String partnerRepresentative, String phoneNumber, String email, String address, String taxCode, Date connperationDate, String description, PartnerManager partnerManagerId) {
        this.id = id;
        this.namePartner = namePartner;
        this.partnerRepresentative = partnerRepresentative;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.taxCode = taxCode;
        this.connperationDate = connperationDate;
        this.description = description;
        this.partnerManagerId = partnerManagerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamePartner() {
        return namePartner;
    }

    public void setNamePartner(String namePartnerPartner) {
        this.namePartner = namePartner;
    }

    public String getPartnerRepresentative() {
        return partnerRepresentative;
    }

    public void setPartnerRepresentative(String partnerRepresentative) {
        this.partnerRepresentative = partnerRepresentative;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Date getConnperationDate() {
        return connperationDate;
    }

    public void setConnperationDate(Date connperationDate) {
        this.connperationDate = connperationDate;
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
}
