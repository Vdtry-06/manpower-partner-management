package com.vdtry06.partner_management.source.server.config.security;

import com.vdtry06.partner_management.source.server.entities.Employee;
import com.vdtry06.partner_management.source.server.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final EmployeeService employeeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeService.findByFields(Map.of("username", username));

        if (employee == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        String authority = employee.getPosition().name();

        return User.builder()
                .username(employee.getUsername())
                .password(employee.getPassword())
                .authorities(authority)
                .build();
    }
}