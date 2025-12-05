package com.seatguard.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_logs")
@Getter @Setter
public class AttendanceLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String status;

    private LocalDateTime recordedAt;

    @PrePersist
    public void prePersist() {
        this.recordedAt = LocalDateTime.now();
    }
}
