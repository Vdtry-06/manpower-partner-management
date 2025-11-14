package com.vdtry06.partner_management.source.server.entities;

import com.vdtry06.partner_management.lib.enumerated.EmployeePosition;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "tblEmployee", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false, length = 255)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "fullname", length = 255)
    private String fullname;

    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private EmployeePosition position;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;
}