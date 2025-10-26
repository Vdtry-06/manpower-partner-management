package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import com.vdtry06.partner_management.source.server.dto.auth.LoginRequest;
import com.vdtry06.partner_management.source.server.dto.auth.LoginResponse;
import com.vdtry06.partner_management.source.server.services.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // Nếu đã đăng nhập, redirect về trang tương ứng
        if (authService.isAuthenticated(session)) {
            EmployeePostion position = (EmployeePostion) session.getAttribute("position");
            return authService.getRedirectUrl(position);
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest,
                        HttpSession session,
                        Model model) {
        LoginResponse response = authService.login(loginRequest, session);

        if (response.isSuccess()) {
            // Redirect theo vị trí
            return authService.getRedirectUrl(response.getPosition());
        } else {
            model.addAttribute("error", response.getMessage());
            return "auth/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "redirect:/auth/login?logout=true";
    }
}