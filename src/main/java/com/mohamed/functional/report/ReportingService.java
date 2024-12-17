package com.mohamed.functional.report;

import com.mohamed.functional.reservation.ReservationService;
import com.mohamed.functional.room.RoomService;
import com.mohamed.functional.room.RoomType;
import com.mohamed.functional.reservation.Reservation;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportingService {
    private final ReservationService reservationService;
    private final RoomService roomService;

    public ReportingService(ReservationService reservationService, RoomService roomService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
    }

    public Report generateReport(Date startDate, Date endDate) {
        List<Reservation> periodReservations = getReservationsInPeriod(startDate, endDate);

        double totalRevenue = calculateTotalRevenue(periodReservations);
        double occupancyRate = calculateOccupancyRate(periodReservations, startDate, endDate);
        Map<RoomType, Integer> reservationsByType = calculateReservationsByType(periodReservations);

        return new Report(
                startDate,
                endDate,
                totalRevenue,
                occupancyRate,
                periodReservations.size(),
                reservationsByType
        );
    }

    private List<Reservation> getReservationsInPeriod(Date startDate, Date endDate) {
        return reservationService.getAllReservations().stream()
                .filter(reservation -> isReservationInPeriod(reservation, startDate, endDate))
                .collect(Collectors.toList());
    }

    private boolean isReservationInPeriod(Reservation reservation, Date startDate, Date endDate) {
        return !reservation.checkIn().before(startDate) &&
                !reservation.checkOut().after(endDate);
    }

    private double calculateTotalRevenue(List<Reservation> reservations) {
        return reservations.stream()
                .mapToDouble(this::calculateReservationRevenue)
                .sum();
    }

    private double calculateReservationRevenue(Reservation reservation) {
        long days = (reservation.checkOut().getTime() -
                reservation.checkIn().getTime()) / (1000 * 60 * 60 * 24);
        return reservation.room().price() * days;
    }

    private double calculateOccupancyRate(List<Reservation> reservations,
                                          Date startDate,
                                          Date endDate) {
        int totalRoomDays = calculateTotalRoomDays(startDate, endDate);
        int occupiedRoomDays = calculateOccupiedRoomDays(reservations);
        return (double) occupiedRoomDays / totalRoomDays;
    }

    private int calculateTotalRoomDays(Date startDate, Date endDate) {
        long days = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        return (int) days * roomService.getAllRooms().size();
    }

    private int calculateOccupiedRoomDays(List<Reservation> reservations) {
        return reservations.stream()
                .mapToInt(reservation -> {
                    long days = (reservation.checkOut().getTime() -
                            reservation.checkIn().getTime()) / (1000 * 60 * 60 * 24);
                    return (int) days;
                })
                .sum();
    }

    private Map<RoomType, Integer> calculateReservationsByType(List<Reservation> reservations) {
        return reservations.stream()
                .collect(Collectors.groupingBy(
                        reservation -> reservation.room().roomType(),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }
}