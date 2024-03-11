package com.example.springsecurity.controller;

import com.example.springsecurity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        request.getSession().removeAttribute("error");
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(HttpServletRequest request, Model model) {
        String errorMessage = (String) request.getSession().getAttribute("error");
        request.getSession().removeAttribute("error");
        model.addAttribute("error", errorMessage);
        return "login";
    }
    @GetMapping("/index")
    public String home(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("roles", authentication.getAuthorities());
        return "index";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(String userName, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        userService.updatePassword(userName, encodedPassword);
        return "redirect:/login";
    }

}
