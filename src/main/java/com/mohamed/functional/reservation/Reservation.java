package com.mohamed.functional.reservation;

import com.mohamed.functional.customer.Customer;
import com.mohamed.functional.room.Room;

import java.time.LocalDate;
import java.util.Date;

public record Reservation(
        Room room,
        Customer customer,
        Date checkIn,
        Date checkOut,
        boolean isActive
) {
    public Reservation updateActive(boolean newActive) {
        return new Reservation(room, customer, checkIn, checkOut, newActive);
    }
}
