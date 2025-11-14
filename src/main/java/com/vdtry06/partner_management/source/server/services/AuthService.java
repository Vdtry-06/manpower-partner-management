package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.lib.enumerated.EmployeePosition;
import com.vdtry06.partner_management.source.server.dto.auth.LoginRequest;
import com.vdtry06.partner_management.source.server.dto.auth.LoginResponse;
import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.repositories.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmployeeRepository employeeRepository;

    public LoginResponse login(LoginRequest request, HttpSession session) {
        Employee employee = employeeRepository.findByUsername(request.getUsername());
        if (employee == null || !employee.getUsername().equals(request.getUsername())) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Username not found!")
                    .build();
        }

        if (!employee.getPassword().equals(request.getPassword())) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Password was wrong!")
                    .build();
        }

        session.setAttribute("employeeId", employee.getId());
        session.setAttribute("username", employee.getUsername());
        session.setAttribute("fullname", employee.getFullname());
        session.setAttribute("position", employee.getPosition());

        return LoginResponse.builder()
                .success(true)
                .employeeId(employee.getId())
                .username(employee.getUsername())
                .fullname(employee.getFullname())
                .position(employee.getPosition())
                .message("Login successful!")
                .build();
    }

    public boolean checkAuth(HttpSession session, EmployeePosition requiredPosition) {
        if (session.getAttribute("employeeId") == null) return false;
        EmployeePosition position = (EmployeePosition) session.getAttribute("position");
        return position == requiredPosition;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("employeeId") != null;
    }
}
