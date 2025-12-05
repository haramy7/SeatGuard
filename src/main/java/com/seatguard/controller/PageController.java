package com.seatguard.controller;

import com.seatguard.model.Role;
import com.seatguard.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin";
        }
        
        model.addAttribute("userName", user.getName());
        return "index";
    }
}
