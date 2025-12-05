package com.seatguard.controller;

import com.seatguard.dto.StatusUpdateRequest;
import com.seatguard.dto.UserStatusDto;
import com.seatguard.model.User;
import com.seatguard.service.StatusService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiStatusController {

    private final StatusService statusService;

    @PostMapping("/status")
    public ResponseEntity<Void> updateStatus(@RequestBody StatusUpdateRequest req, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            statusService.updateStatus(user.getId(), req.getStatus());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<List<UserStatusDto>> getDashboardData(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != com.seatguard.model.Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(statusService.getAllUserStatus());
    }
}
