package com.mohamed.functional.report;

import com.mohamed.functional.room.RoomType;

import java.util.Date;
import java.util.Map;

public record Report(
        Date startDate,
        Date endDate,
        double totalRevenue,
        double occupancyRate,
        int totalReservations,
        Map<RoomType, Integer> reservationsByRoomType
) {
    public Report {
        reservationsByRoomType = Map.copyOf(reservationsByRoomType);
    }
}
