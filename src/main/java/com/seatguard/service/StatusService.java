package com.seatguard.service;

import com.seatguard.dto.UserStatusDto;
import com.seatguard.model.AttendanceLog;
import com.seatguard.model.User;
import com.seatguard.repository.AttendanceLogRepository;
import com.seatguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService {

    private final UserRepository userRepository;
    private final AttendanceLogRepository logRepository;

    @Transactional
    public void updateStatus(Long userId, String status) {
        log.info(">>> updateStatus called: userId={}, status={}", userId, status);
        
        User user = userRepository.findById(userId).orElseThrow();
        log.info(">>> current status: {}", user.getCurrentStatus());
        
        if (!status.equals(user.getCurrentStatus())) {
            AttendanceLog attendanceLog = new AttendanceLog();
            attendanceLog.setUser(user);
            attendanceLog.setStatus(status);
            logRepository.save(attendanceLog);

            user.setCurrentStatus(status);
            user.setLastStatusChange(LocalDateTime.now());
            userRepository.save(user);
            
            log.info(">>> status updated to: {}", status);
        }
    }

    public List<UserStatusDto> getAllUserStatus() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == com.seatguard.model.Role.STUDENT)
                .map(u -> new UserStatusDto(u.getId(), u.getName(), u.getCurrentStatus(), u.getLastStatusChange()))
                .collect(Collectors.toList());
    }
}
