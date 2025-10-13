package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.source.server.service.AuthService;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final AuthService authService;

    public EmployeeController(AuthService authService) {
        this.authService = authService;
    }

}
