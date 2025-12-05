package com.seatguard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserStatusDto {
    private Long id;
    private String name;
    private String status;
    private LocalDateTime lastChange;
}
