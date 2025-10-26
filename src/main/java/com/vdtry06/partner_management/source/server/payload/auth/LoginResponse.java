package com.vdtry06.partner_management.source.server.payload.auth;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private int id;
    private boolean check;
    @Enumerated(EnumType.STRING)
    private EmployeePostion position;
    private TokenResponse token;
}
