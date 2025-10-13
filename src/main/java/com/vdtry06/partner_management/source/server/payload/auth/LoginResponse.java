package com.vdtry06.partner_management.source.server.payload.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private int id;
    private String username;
    private String fullname;
    private TokenResponse token;
}
