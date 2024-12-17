package com.mohamed.imperative.room;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private int roomNumber;
    private RoomType roomType;
    private double price;
    private boolean isAvailable;
    
    public Room(int roomNumber, RoomType roomType, double price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.isAvailable = true;
    }

} 