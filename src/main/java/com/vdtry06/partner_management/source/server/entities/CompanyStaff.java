package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Table(name = "tblCompanyStaff", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class CompanyStaff implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_staff_id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "username", unique = true, nullable = false, length = 255)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "fullname", length = 255)
    private String fullname;

    @Column(name = "position", length = 255)
    private String position;

    @Column(name = "phone_number", length = 10)
    private String phoneNumber;

    public CompanyStaff() {
    }

    public CompanyStaff(Integer id, String username, String password, String fullname, String position, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.position = position;
        this.phoneNumber = phoneNumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
