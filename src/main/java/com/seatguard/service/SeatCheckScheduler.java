package com.seatguard.service;

import com.seatguard.model.Role;
import com.seatguard.model.User;
import com.seatguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatCheckScheduler {

    private final UserRepository userRepository;
    private final FcmService fcmService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkAttendance() {
        LocalDateTime now = LocalDateTime.now();
        List<User> students = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.STUDENT)
                .toList();

        for (User student : students) {
            if (!"IN".equals(student.getCurrentStatus())) {
                long minutesAway = Duration.between(student.getLastStatusChange(), now).toMinutes();

                if (minutesAway >= 10) {
                    if (!"DANGER".equals(student.getCurrentStatus())) {
                        student.setCurrentStatus("DANGER");
                        userRepository.save(student);
                    }
                    
                    String msg = student.getName() + " 학생이 " + minutesAway + "분째 자리 비움!";
                    fcmService.sendNotificationToAdmins("⚠️ 장기 이석 경고", msg);
                }
            }
        }
    }
}
