package com.seatguard.service;

import com.seatguard.dto.StatsDto;
import com.seatguard.dto.StatsDto.UserAwayTime;
import com.seatguard.dto.StatsDto.UserHeatmap;
import com.seatguard.model.AttendanceLog;
import com.seatguard.model.Role;
import com.seatguard.model.User;
import com.seatguard.repository.AttendanceLogRepository;
import com.seatguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final UserRepository userRepository;
    private final AttendanceLogRepository logRepository;

    public StatsDto getTodayStats() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        List<User> students = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.STUDENT)
                .collect(Collectors.toList());

        Map<String, Long> awayTimeMap = new HashMap<>();
        Map<String, int[]> heatmapMap = new HashMap<>();

        for (User student : students) {
            List<AttendanceLog> logs = logRepository
                    .findByUserAndRecordedAtBetweenOrderByRecordedAtAsc(student, startOfDay, endOfDay);

            long awayMinutes = calculateAwayMinutes(logs);
            awayTimeMap.put(student.getName(), awayMinutes);

            int[] hourCounts = calculateHourlyAway(logs);
            heatmapMap.put(student.getName(), hourCounts);
        }

        List<UserAwayTime> topAway = awayTimeMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> new UserAwayTime(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        long totalAwayMinutes = awayTimeMap.values().stream().mapToLong(Long::longValue).sum();
        long avgAway = students.isEmpty() ? 0 : totalAwayMinutes / students.size();

        double attendanceRate = calculateAttendanceRate(students);

        List<UserHeatmap> heatmapData = heatmapMap.entrySet().stream()
                .map(e -> new UserHeatmap(e.getKey(), Arrays.stream(e.getValue()).boxed().collect(Collectors.toList())))
                .collect(Collectors.toList());

        return new StatsDto(topAway, avgAway, attendanceRate, heatmapData);
    }

    private long calculateAwayMinutes(List<AttendanceLog> logs) {
        long total = 0;
        LocalDateTime awayStart = null;

        for (AttendanceLog log : logs) {
            if ("AWAY".equals(log.getStatus()) || "DANGER".equals(log.getStatus())) {
                if (awayStart == null) {
                    awayStart = log.getRecordedAt();
                }
            } else if ("IN".equals(log.getStatus())) {
                if (awayStart != null) {
                    total += Duration.between(awayStart, log.getRecordedAt()).toMinutes();
                    awayStart = null;
                }
            }
        }

        if (awayStart != null) {
            total += Duration.between(awayStart, LocalDateTime.now()).toMinutes();
        }

        return total;
    }

    private int[] calculateHourlyAway(List<AttendanceLog> logs) {
        int[] hours = new int[24];
        LocalDateTime awayStart = null;

        for (AttendanceLog log : logs) {
            if ("AWAY".equals(log.getStatus()) || "DANGER".equals(log.getStatus())) {
                if (awayStart == null) {
                    awayStart = log.getRecordedAt();
                }
            } else if ("IN".equals(log.getStatus())) {
                if (awayStart != null) {
                    markHours(hours, awayStart, log.getRecordedAt());
                    awayStart = null;
                }
            }
        }

        if (awayStart != null) {
            markHours(hours, awayStart, LocalDateTime.now());
        }

        return hours;
    }

    private void markHours(int[] hours, LocalDateTime start, LocalDateTime end) {
        int startHour = start.getHour();
        int endHour = end.getHour();
        for (int h = startHour; h <= Math.min(endHour, 23); h++) {
            hours[h]++;
        }
    }

    private double calculateAttendanceRate(List<User> students) {
        if (students.isEmpty()) return 100.0;
        
        long presentCount = students.stream()
                .filter(s -> "IN".equals(s.getCurrentStatus()))
                .count();
        
        return Math.round((double) presentCount / students.size() * 1000) / 10.0;
    }
}
