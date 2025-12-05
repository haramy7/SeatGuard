package com.seatguard.controller;

import com.seatguard.model.User;
import com.seatguard.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final UserRepository userRepository;

    @PostMapping("/api/fcm/token")
    public ResponseEntity<Void> updateToken(@RequestBody Map<String, String> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            String token = body.get("token");
            user.setFcmToken(token);
            userRepository.save(user);
            System.out.println("FCM Token Saved for " + user.getName());
        }
        return ResponseEntity.ok().build();
    }
}
