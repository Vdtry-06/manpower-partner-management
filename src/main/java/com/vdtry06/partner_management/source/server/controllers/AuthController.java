package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.api.ApiResponse;
import com.vdtry06.partner_management.source.server.payload.auth.LoginRequest;
import com.vdtry06.partner_management.source.server.payload.auth.LoginResponse;
import com.vdtry06.partner_management.source.server.payload.auth.LogoutRequest;
import com.vdtry06.partner_management.source.server.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ApiResponse<LoginResponse>(true, authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ApiResponse<Object> logout(LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);
        return new ApiResponse<Object>(true, "Logout successfull!");
    }
}