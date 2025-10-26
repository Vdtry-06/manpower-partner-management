package com.vdtry06.partner_management.source.server.controllers;

import com.vdtry06.partner_management.lib.enumerated.EmployeePostion;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accountant")
@RequiredArgsConstructor
public class AccountantController {

    @GetMapping("/home")
    public String dashboard(HttpSession session, Model model) {
        // Kiểm tra đăng nhập
        if (session.getAttribute("employeeId") == null) {
            return "redirect:/auth/login";
        }

        // Kiểm tra quyền
        EmployeePostion position = (EmployeePostion) session.getAttribute("position");
        if (position != EmployeePostion.ACCOUNTANT) {
            return "redirect:/auth/login";
        }

        model.addAttribute("fullname", session.getAttribute("fullname"));
        model.addAttribute("username", session.getAttribute("username"));

        return "accountant/home";
    }
}