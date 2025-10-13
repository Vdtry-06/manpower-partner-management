package com.vdtry06.partner_management.source.server.payload.employee;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {
    private Integer employeeId;
    private String username;

    private String password;

    private String fullname;

    @Enumerated(EnumType.STRING)
    private EmployeePostion position;

    private String phoneNumber;
}
