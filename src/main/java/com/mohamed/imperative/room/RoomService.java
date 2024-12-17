package com.mohamed.imperative.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomService {
    private List<Room> rooms;
    
    public RoomService() {
        this.rooms = new ArrayList<>();
    }
    
    public void addRoom(Room room) {
        rooms.add(room);
    }

    
    public void updateRoomAvailability(int roomNumber, boolean isAvailable) {
        Room room = findRoom(roomNumber);
        if (room != null) {
            room.setAvailable(isAvailable);
        }
    }

    public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public Room findRoom(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public List<Room> getAllRooms() {
        return rooms;
    }
}