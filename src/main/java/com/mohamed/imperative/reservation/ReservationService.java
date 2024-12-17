package com.mohamed.imperative.reservation;

import com.mohamed.imperative.customer.Customer;
import com.mohamed.imperative.customer.CustomerService;
import com.mohamed.imperative.room.Room;
import com.mohamed.imperative.room.RoomService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationService {
    private List<Reservation> reservations;
    private RoomService roomService;
    private CustomerService customerService;
    
    public ReservationService(RoomService roomService, CustomerService customerService) {
        this.reservations = new ArrayList<>();
        this.roomService = roomService;
        this.customerService = customerService;
    }
    
    public boolean createReservation(int roomNumber, int customerId, Date checkIn, Date checkOut) {
        Room room = roomService.findRoom(roomNumber);
        Customer customer = customerService.findCustomer(customerId);
        
        if (room == null || customer == null || !room.isAvailable()) {
            return false;
        }
        
        roomService.updateRoomAvailability(roomNumber, false);
        Reservation reservation = new Reservation(room, customer, checkIn, checkOut);
        reservations.add(reservation);
        return true;
    }
    
    public void checkOut(int roomNumber) {
        Reservation activeReservation = findActiveReservation(roomNumber);
        if (activeReservation != null) {
            activeReservation.setActive(false);
            roomService.updateRoomAvailability(roomNumber, true);
        }
    }

    public double calculateBill(int roomNumber) {
        Reservation reservation = findActiveReservation(roomNumber); if (reservation == null) return 0.0;
        long days = (reservation.getCheckOut().getTime() -
                reservation.getCheckIn().getTime()) / (1000 * 60 * 60 * 24);
        return reservation.getRoom().getPrice() * days;
    }
    
    private Reservation findActiveReservation(int roomNumber) {
        for (Reservation reservation : reservations) {
            if (reservation.getRoom().getRoomNumber() == roomNumber &&
                    reservation.isActive()) {
                return reservation;
            }
        }
        return null;
    }

    public List<Reservation> getAllReservations() {
        return reservations;
    }
}