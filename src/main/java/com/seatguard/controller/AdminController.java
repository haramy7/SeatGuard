package com.seatguard.controller;

import com.seatguard.model.User;
import com.seatguard.model.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user.getRole() != Role.ADMIN) {
            return "redirect:/";
        }
        return "admin";
    }
}
