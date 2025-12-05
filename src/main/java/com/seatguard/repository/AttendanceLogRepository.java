package com.seatguard.repository;

import com.seatguard.model.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {
}
