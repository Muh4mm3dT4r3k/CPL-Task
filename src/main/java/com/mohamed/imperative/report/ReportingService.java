package com.mohamed.imperative.report;

import com.mohamed.imperative.reservation.Reservation;
import com.mohamed.imperative.reservation.ReservationService;
import com.mohamed.imperative.room.RoomService;
import com.mohamed.imperative.room.RoomType;

import java.util.*;

public class ReportingService {
    private ReservationService reservationService;
    private RoomService roomService;

    public ReportingService(ReservationService reservationService, RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    public Report generateReport(Date startDate, Date endDate) {
        Report report = new Report(startDate, endDate);

        List<Reservation> periodReservations = getReservationsInPeriod(startDate, endDate);

        // Calculate total revenue
        double totalRevenue = 0;
        for (Reservation reservation : periodReservations) {
            totalRevenue += calculateReservationRevenue(reservation);
        }
        report.setTotalRevenue(totalRevenue);

        // Calculate occupancy rate
        int totalRoomDays = calculateTotalRoomDays(startDate, endDate);
        int occupiedRoomDays = calculateOccupiedRoomDays(periodReservations);
        double occupancyRate = (double) occupiedRoomDays / totalRoomDays;
        report.setOccupancyRate(occupancyRate);

        // Count reservations by room type
        Map<RoomType, Integer> reservationsByType = new HashMap<>();
        for (Reservation reservation : periodReservations) {
            RoomType roomType = reservation.getRoom().getRoomType();
            reservationsByType.put(roomType,
                    reservationsByType.getOrDefault(roomType, 0) + 1);
        }
        report.setReservationsByRoomType(reservationsByType);

        report.setTotalReservations(periodReservations.size());

        return report;
    }

    private List<Reservation> getReservationsInPeriod(Date startDate, Date endDate) {
        List<Reservation> periodReservations = new ArrayList<>();
        for (Reservation reservation : reservationService.getAllReservations()) {
            if (isReservationInPeriod(reservation, startDate, endDate)) {
                periodReservations.add(reservation);
            }
        }
        return periodReservations;
    }

    private boolean isReservationInPeriod(Reservation reservation, Date startDate, Date endDate) {
        return !reservation.getCheckIn().before(startDate) &&
                !reservation.getCheckOut().after(endDate);
    }

    private double calculateReservationRevenue(Reservation reservation) {
        long days = (reservation.getCheckOut().getTime() -
                reservation.getCheckIn().getTime()) / (1000 * 60 * 60 * 24);
        return reservation.getRoom().getPrice() * days;
    }

    private int calculateTotalRoomDays(Date startDate, Date endDate) {
        long days = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        return (int) days * roomService.getAllRooms().size();
    }

    private int calculateOccupiedRoomDays(List<Reservation> reservations) {
        int occupiedDays = 0;
        for (Reservation reservation : reservations) {
            long days = (reservation.getCheckOut().getTime() -
                    reservation.getCheckIn().getTime()) / (1000 * 60 * 60 * 24);
            occupiedDays += days;
        }
        return occupiedDays;
    }
}