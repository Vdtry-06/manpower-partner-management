package com.vdtry06.partner_management.source.server.service;

import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.payload.auth.LoginRequest;
import com.vdtry06.partner_management.source.server.payload.auth.LoginResponse;
import com.vdtry06.partner_management.source.server.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    public AuthService(EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Employee employee = employeeRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new RuntimeException("Tên đăng nhập không tìm thấy"));
        if (!employee.getPassword().equals(loginRequest.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng, vui lòng đăng nhập lại");
        }

        return LoginResponse.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .fullname(employee.getFullname())
                .build();
    }

}