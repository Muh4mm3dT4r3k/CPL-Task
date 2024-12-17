package com.mohamed.functional.room;

public record Room(
        int roomNumber,
        RoomType roomType,
        Double price,
        boolean isAvailable
) {
    public Room updateAvailability(boolean newAvailability) {
        return new Room(roomNumber, roomType, price, newAvailability);
    }
}
