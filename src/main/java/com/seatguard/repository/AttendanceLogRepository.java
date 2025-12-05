package com.seatguard.repository;

import com.seatguard.model.AttendanceLog;
import com.seatguard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
    
    List<AttendanceLog> findByUserAndRecordedAtBetweenOrderByRecordedAtAsc(
        User user, LocalDateTime start, LocalDateTime end);
    
    List<AttendanceLog> findByRecordedAtBetweenOrderByRecordedAtAsc(
        LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT a FROM AttendanceLog a WHERE a.user = :user ORDER BY a.recordedAt DESC")
    List<AttendanceLog> findByUserOrderByRecordedAtDesc(@Param("user") User user);
}
