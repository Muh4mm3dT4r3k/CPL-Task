package com.mohamed.functional.reservation;

import com.mohamed.functional.customer.CustomerService;
import com.mohamed.functional.room.Room;
import com.mohamed.functional.room.RoomService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReservationService {
    private final List<Reservation> reservations;
    private final RoomService roomService;
    private final CustomerService customerService;

    public ReservationService(List<Reservation> reservations, RoomService roomService, CustomerService customerService) {
        this.reservations = List.copyOf(reservations);
        this.roomService = roomService;
        this.customerService = customerService;
    }


    public Optional<ReservationService> createReservation(int roomNumber, int customerId, LocalDate checkIn, LocalDate checkOut) {
        return roomService
                .findRoom(roomNumber)
                .flatMap(room -> customerService
                        .findCustomer(customerId)
                        .filter(customer -> room.isAvailable())
                        .map(customer -> {
                            Room updatedRoom = room.updateAvailability(false);
                            Reservation reservation = new Reservation(
                                    room, customer, checkIn, checkOut, true);
                            return new ReservationService(
                                    Stream.concat(reservations.stream(), Stream.of(reservation)).toList(),
                                            roomService.updateRoomAvailability(roomNumber, false),
                                            customerService);
                        })
                );
    }

    public double calculateBill(int roomNumber) {
        return findActiveReservation(roomNumber)
                .map(ReservationService::getAmount)
                .orElse(0.0);
    }

    private static Double getAmount(Reservation reservation) {
        long days = ChronoUnit.DAYS.between(reservation.checkIn(), reservation.checkOut());
        return reservation.room().price() * days;
    }

    public ReservationService checkout(int roomNumber) {
        Optional<Reservation> activeReservation = findActiveReservation(roomNumber);
        if (activeReservation.isEmpty()) return this;
        List<Reservation> updatedReservations = reservations
                .stream()
                .map(reservation -> reservation.equals(activeReservation.get())
                        ? reservation.updateActive(false)
                        : reservation)
                .toList();
        return new ReservationService(updatedReservations, roomService.updateRoomAvailability(roomNumber, true), customerService);
    }

    private Optional<Reservation> findActiveReservation(int roomNumber) {
        Predicate<Reservation> reservationIsAvailable = Reservation::isActive;
        Predicate<Reservation> roomPredicate = reservation -> reservation.room().roomNumber() ==roomNumber;
        return reservations
                .stream()
                .filter(reservationIsAvailable.and(roomPredicate))
                .findFirst();
    }

    public List<Reservation> getAllReservations() {
        return List.copyOf(reservations);
    }
}
