package com.mohamed.imperative.reservation;

import com.mohamed.imperative.customer.Customer;
import com.mohamed.imperative.room.Room;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Reservation {
    private Room room;
    private Customer customer;
    private Date checkIn;
    private Date checkOut;
    private boolean isActive;
    
    public Reservation(Room room, Customer customer, Date checkIn, Date checkOut) {
        this.room = room;
        this.customer = customer;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.isActive = true;
    }
    

} 