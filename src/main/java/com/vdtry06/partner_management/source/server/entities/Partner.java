package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Table(name = "tblPartner", schema = "public")
@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDate connperationDate;

    @Column(name = "description", length = 255, nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "partner_manager_id")
    private PartnerManager partnerManagerId;
}
