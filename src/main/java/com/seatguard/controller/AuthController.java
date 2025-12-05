package com.seatguard.controller;

import com.seatguard.dto.LoginRequest;
import com.seatguard.dto.SignupRequest;
import com.seatguard.model.Role;
import com.seatguard.model.User;
import com.seatguard.repository.UserRepository;
import com.seatguard.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return user.getRole() == Role.ADMIN ? "redirect:/admin" : "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest req, HttpSession session, HttpServletResponse response) {
        User user = authService.login(req);
        if (user == null) {
            return "redirect:/login?error";
        }
        
        session.setAttribute("user", user);

        String token = UUID.randomUUID().toString();
        user.setLoginToken(token);
        userRepository.save(user);

        Cookie cookie = new Cookie("SEATGUARD_TOKEN", token);
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        cookie.setPath("/");
        cookie.setHttpOnly(true); 
        response.addCookie(cookie);

        return user.getRole() == Role.ADMIN ? "redirect:/admin" : "redirect:/";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupRequest req) {
        authService.signup(req);
        return "redirect:/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        
        Cookie cookie = new Cookie("SEATGUARD_TOKEN", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        
        return "redirect:/login";
    }
}
