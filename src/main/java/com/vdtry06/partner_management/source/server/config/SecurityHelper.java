package com.vdtry06.partner_management.source.server.config;

import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
@RequiredArgsConstructor
public class SecurityHelper {
    private final EmployeeService employeeService;
    public String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("User not found");
        }

        // Lấy username từ UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Query Employee từ DB
        Employee employee = employeeService.findByFields(Map.of("username", username));
        return employee.getPosition().name();
    }
}