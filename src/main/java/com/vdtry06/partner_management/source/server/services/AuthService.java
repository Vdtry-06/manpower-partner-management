package com.vdtry06.partner_management.source.server.services;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
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
        // Tìm employee theo username
        Employee employee = employeeRepository.findByUsername(request.getUsername())
                .orElse(null);

        // Kiểm tra username và password
        if (employee == null || !employee.getPassword().equals(request.getPassword())) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Tên đăng nhập hoặc mật khẩu không đúng!")
                    .build();
        }

        // Lưu thông tin vào session
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
                .message("Đăng nhập thành công!")
                .build();
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public String getRedirectUrl(EmployeePostion position) {
        if (position == null) {
            return "redirect:/auth/login";
        }

        return switch (position) {
            case PARTNER_MANAGER -> "redirect:/partner-manager/home";
            case ACCOUNTANT -> "redirect:/accountant/home";
            case ADMIN -> "redirect:/admin/home";
            default -> "redirect:/auth/login";
        };
    }

    public boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("employeeId") != null;
    }
}