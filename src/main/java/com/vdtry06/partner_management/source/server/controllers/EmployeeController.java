package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.source.server.payload.auth.LoginRequest;
import com.vdtry06.partner_management.source.server.payload.auth.LoginResponse;
import com.vdtry06.partner_management.source.server.payload.auth.LogoutRequest;
import com.vdtry06.partner_management.source.server.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class EmployeeController {
    private final AuthService authService;

    public EmployeeController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "LoginView";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, Model model, HttpServletResponse response) {
        try {
            LoginResponse loginResponse = authService.login(loginRequest);

            // Lưu token vào cookie HTTP-only
            Cookie jwtCookie = new Cookie("token", loginResponse.getToken().getAccessToken());
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(60 * 60 * 100); // 1 ngày
            response.addCookie(jwtCookie);

            // Redirect theo vai trò
            String role = String.valueOf(loginResponse.getPosition());
            if ("PARTNER_MANAGER".equalsIgnoreCase(role)) {
                return "redirect:/partner-manager";
            } else if ("ACCOUNTANT".equalsIgnoreCase(role)) {
                return "redirect:/accountant";
            } else {
                return "redirect:/auth/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            return "LoginView";
        }
    }

    @PostMapping("/logout")
    public String logout(@ModelAttribute LogoutRequest logoutRequest, HttpServletResponse response) {
        authService.logout(logoutRequest);

        // Xóa cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/auth/login";
    }
}
