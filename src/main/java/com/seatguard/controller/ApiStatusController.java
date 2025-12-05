package com.seatguard.controller;

import com.seatguard.dto.StatusUpdateRequest;
import com.seatguard.dto.StatsDto;
import com.seatguard.dto.UserStatusDto;
import com.seatguard.model.User;
import com.seatguard.model.Role;
import com.seatguard.service.StatusService;
import com.seatguard.service.StatsService;
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
    private final StatsService statsService;

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
        if (user == null || user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(statusService.getAllUserStatus());
    }

    @GetMapping("/admin/stats")
    public ResponseEntity<StatsDto> getStats(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(statsService.getTodayStats());
    }
}
