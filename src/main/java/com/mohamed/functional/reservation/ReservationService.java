package com.mohamed.functional.reservation;

import com.mohamed.functional.customer.Customer;
import com.mohamed.functional.customer.CustomerService;
import com.mohamed.functional.room.Room;
import com.mohamed.functional.room.RoomService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ReservationService {
    private final List<Reservation> reservations;
    private final RoomService roomService;
    private final CustomerService customerService;

    private final Function<Reservation, Double> calculateAmount = 
        reservation -> {
            long diffInMillies = reservation.checkOut().getTime() - reservation.checkIn().getTime();
            long days = diffInMillies / (24 * 60 * 60 * 1000);
            return reservation.room().price() * days;
        };
    
    private final BiPredicate<Reservation, Integer> matchesRoomNumber = 
        (reservation, roomNumber) -> reservation.room().roomNumber() == roomNumber;

    public ReservationService(List<Reservation> reservations, RoomService roomService, CustomerService customerService) {
        this.reservations = List.copyOf(reservations);
        this.roomService = roomService;
        this.customerService = customerService;
    }

    public Optional<ReservationService> createReservation(int roomNumber, int customerId, Date checkIn, Date checkOut) {
        // Validate dates
        if (checkIn == null || checkOut == null || checkIn.after(checkOut)) {
            return Optional.empty();
        }

        return roomService.findRoom(roomNumber)
            .flatMap(room -> 
                customerService.findCustomer(customerId)
                    .filter(customer -> room.isAvailable())
                    .map(customer -> {
                        Reservation newReservation = new Reservation(room, customer, checkIn, checkOut, true);
                        RoomService updatedRoomService = roomService.updateRoomAvailability(roomNumber, false);
                        return new ReservationService(
                            Stream.concat(reservations.stream(), Stream.of(newReservation)).toList(),
                            updatedRoomService,
                            customerService
                        );
                    })
            );
    }

    public double calculateBill(int roomNumber) {
        return findActiveReservation(roomNumber)
                .map(calculateAmount)
                .orElse(0.0);
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

    private Double calculateBillRecursively(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            return 0.0;
        }
        return calculateAmount.apply(reservations.get(0)) + 
               calculateBillRecursively(reservations.subList(1, reservations.size()));
    }
}
