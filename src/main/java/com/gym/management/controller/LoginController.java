package com.gym.management.controller;

import com.gym.management.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("pageTitle", "Admin Login");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("pageTitle", "Create Account");
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@RequestParam("username") String username,
                                 @RequestParam(value = "email", required = false) String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match!");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("pageTitle", "Create Account");
            return "register";
        }

        try {
            adminService.registerAdmin(username, email, password);
            redirectAttributes.addFlashAttribute("registeredMessage", "Account created successfully! Please log in.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("pageTitle", "Create Account");
            return "register";
        }
    }
}
