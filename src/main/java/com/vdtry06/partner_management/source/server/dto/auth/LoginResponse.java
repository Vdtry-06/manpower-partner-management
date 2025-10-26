package com.vdtry06.partner_management.source.server.dto.auth;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Integer employeeId;
    private String username;
    private String fullname;
    private EmployeePostion position;
    private String message;
    private boolean success;
}