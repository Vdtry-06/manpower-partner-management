package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.enumerated.EmployeePosition;
import com.vdtry06.partner_management.source.server.config.AppUrls;
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
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AppUrls appUrls;

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        if (authService.isAuthenticated(session)) {
            EmployeePosition position = (EmployeePosition) session.getAttribute("position");
            return getRedirectUrl(position);
        }
        return appUrls.getLogin();
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
        LoginResponse response = authService.login(loginRequest, session);

        if (response.isSuccess()) return getRedirectUrl(response.getPosition());
        else {
            model.addAttribute("error", response.getMessage());
            return appUrls.getLogin();
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "redirect:" + appUrls.getLogout();
    }

    private String getRedirectUrl(EmployeePosition position) {
        if (position == null) return "redirect:" + appUrls.getLogin();

        return switch (position) {
            case EmployeePosition.PARTNER_MANAGER -> "redirect:" + appUrls.getHome().get("partner-manager");
            case EmployeePosition.ACCOUNTANT -> "redirect:" + appUrls.getHome().get("accountant");
            case EmployeePosition.ADMIN -> "redirect:" + appUrls.getHome().get("admin");
            default -> "redirect:" + appUrls.getLogin();
        };
    }
}
