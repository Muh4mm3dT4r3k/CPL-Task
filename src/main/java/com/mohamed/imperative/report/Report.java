package com.mohamed.imperative.report;

import com.mohamed.imperative.room.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Report {
    private Date startDate;
    private Date endDate;
    private double totalRevenue;
    private double occupancyRate;
    private int totalReservations;
    private Map<RoomType, Integer> reservationsByRoomType;

    public Report(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservationsByRoomType = new HashMap<>();
    }
} 