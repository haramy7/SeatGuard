package com.seatguard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsDto {
    private List<UserAwayTime> topAwayUsers;
    private long avgAwayMinutes;
    private double attendanceRate;
    private List<UserHeatmap> heatmapData;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAwayTime {
        private String name;
        private long awayMinutes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserHeatmap {
        private String name;
        private List<Integer> hours;
    }
}
