package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;

@Table(name = "tblAccountant")
@Entity
public class Accountant extends Employee implements Serializable {
}
