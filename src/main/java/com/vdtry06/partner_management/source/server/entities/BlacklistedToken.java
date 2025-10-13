package com.vdtry06.partner_management.source.server.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Table(name = "tbl_blacklisted_token")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class BlacklistedToken {
    @Id
    private String id;

    @Column(name = "expiry_time")
    private Date expiryTime;
}