package com.mohamed.functional.room;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class RoomService {
    private final List<Room> rooms;

    public RoomService(List<Room> rooms) {
        this.rooms = List.copyOf(rooms);
    }

    public RoomService addRoom(Room room) {
        return new RoomService(
                Stream.concat(rooms.stream(), Stream.of(room))
                        .toList()
        );
    }

    public Optional<Room> findRoom(int roomNumber) {
        Predicate<Room> roomPredicate = room -> room.roomNumber() == roomNumber;
        return rooms
                .stream()
                .filter(roomPredicate)
                .findFirst();
    }

    public RoomService updateRoomAvailability(int roomNumber, boolean isAvailable) {
        List<Room> updatedRooms = rooms
                .stream()
                .map(room -> room.roomNumber() == roomNumber
                        ? room.updateAvailability(isAvailable)
                        : room)
                .toList();
        return new RoomService(updatedRooms);
    }

    public List<Room> getAvailableRooms() {
        Predicate<Room> roomPredicate = Room::isAvailable;
        return rooms
                .stream()
                .filter(roomPredicate)
                .toList();
    }

    public List<Room> getAllRooms() {
        return List.copyOf(rooms);
    }
}
