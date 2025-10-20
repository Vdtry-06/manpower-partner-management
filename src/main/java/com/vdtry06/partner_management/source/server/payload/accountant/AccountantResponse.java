package com.vdtry06.partner_management.source.server.payload.accountant;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountantResponse {
    private Integer id;
    private String username;
    private String fullname;
    @Enumerated(EnumType.STRING)
    private EmployeePostion position;
}
