package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

@Table(name = "tblPartnerManager")
@Entity
public class PartnerManager extends Employee implements Serializable {
}
