package com.seatguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SeatGuardApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeatGuardApplication.class, args);
    }
}
