package com.mohamed.functional;

import com.mohamed.functional.customer.Customer;
import com.mohamed.functional.customer.CustomerService;
import com.mohamed.functional.report.Report;
import com.mohamed.functional.report.ReportingService;
import com.mohamed.functional.reservation.Reservation;
import com.mohamed.functional.reservation.ReservationService;
import com.mohamed.functional.room.Room;
import com.mohamed.functional.room.RoomService;
import com.mohamed.functional.room.RoomType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) throws Exception {
        // Initialize services
        List<Room> initialRooms = new ArrayList<>();
        List<Customer> initialCustomers = new ArrayList<>();
        List<Reservation> initialReservations = new ArrayList<>();
        
        RoomService roomService = new RoomService(initialRooms);
        CustomerService customerService = new CustomerService(initialCustomers);
        ReservationService reservationService = new ReservationService(initialReservations, roomService, customerService);
        ReportingService reportingService = new ReportingService(reservationService, roomService);

        // Add dummy rooms - explicitly setting availability to true
        roomService = roomService.addRoom(new Room(101, RoomType.SINGLE, 100.0, true));
        System.out.println("Room 101 added. Available: " + roomService.findRoom(101).map(Room::isAvailable).orElse(false));
        
        roomService = roomService.addRoom(new Room(102, RoomType.SINGLE, 100.0, true));
        roomService = roomService.addRoom(new Room(201, RoomType.DOUBLE, 150.0, true));
        roomService = roomService.addRoom(new Room(202, RoomType.DOUBLE, 150.0, true));
        roomService = roomService.addRoom(new Room(301, RoomType.SUITE, 300.0, true));

        // Print all rooms and their availability before making reservations
        System.out.println("\nInitial Room Status:");
        roomService.getAllRooms().forEach(room -> 
            System.out.println("Room " + room.roomNumber() + " (" + room.roomType() + ") - Available: " + room.isAvailable())
        );

        // Add dummy customers
        customerService = customerService.addCustomer(new Customer(1, "John Doe", "john@email.com", "Credit Card"));
        customerService = customerService.addCustomer(new Customer(2, "Jane Smith", "jane@email.com", "Debit Card"));
        customerService = customerService.addCustomer(new Customer(3, "Bob Wilson", "bob@email.com", "Cash"));

        // Debug print customers
        System.out.println("\nCustomers in system:");
        customerService.getAllCustomers().forEach(customer -> 
            System.out.println("Customer ID: " + customer.id() + " - " + customer.name())
        );

        // Create new ReservationService instance with updated services
        reservationService = new ReservationService(
            new ArrayList<>(),  // empty initial reservations
            roomService,        // updated room service with all rooms
            customerService     // updated customer service with all customers
        );

        // Update the reporting service with the new reservation service
        reportingService = new ReportingService(reservationService, roomService);

        // Debug prints for service state
        System.out.println("\nVerifying service states:");
        System.out.println("RoomService - Total rooms: " + roomService.getAllRooms().size());
        System.out.println("CustomerService - Total customers: " + customerService.getAllCustomers().size());
        System.out.println("ReservationService - Total reservations: " + reservationService.getAllReservations().size());

        // Print all rooms to verify they're in the service
        System.out.println("\nAll rooms in service:");
        roomService.getAllRooms().forEach(room -> 
            System.out.println("Room " + room.roomNumber() + " - Available: " + room.isAvailable())
        );

        // Print all customers to verify they're in the service
        System.out.println("\nAll customers in service:");
        customerService.getAllCustomers().forEach(customer -> 
            System.out.println("Customer " + customer.id() + " - " + customer.name())
        );

        // Create date formatter
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Create some reservations with error handling
        try {
            System.out.println("\nAttempting to create reservation for room 101...");
            reservationService = reservationService
                .createReservation(101, 1, dateFormat.parse("2024-03-01"), dateFormat.parse("2024-03-03"))
                .orElseThrow(() -> new RuntimeException("Failed to create reservation for room 101"));
            
            System.out.println("Successfully created reservation for room 101");
            
            reservationService = reservationService
                .createReservation(201, 2, dateFormat.parse("2024-03-02"), dateFormat.parse("2024-03-05"))
                .orElseThrow(() -> new RuntimeException("Failed to create reservation for room 201"));
            
            System.out.println("Successfully created reservation for room 201");
            
            reservationService = reservationService
                .createReservation(301, 3, dateFormat.parse("2024-03-01"), dateFormat.parse("2024-03-04"))
                .orElseThrow(() -> new RuntimeException("Failed to create reservation for room 301"));
            
            System.out.println("Successfully created reservation for room 301");
        } catch (RuntimeException e) {
            System.err.println("Error creating reservation: " + e.getMessage());
            return;
        }

        // Generate and print report
        Report report = reportingService.generateReport(
            dateFormat.parse("2024-03-01"),
            dateFormat.parse("2024-03-05")
        );

        // Print report details
        System.out.println("Hotel Management System Report");
        System.out.println("=============================");
        System.out.println("Period: " + dateFormat.format(report.startDate()) + 
                          " to " + dateFormat.format(report.endDate()));
        System.out.println("Total Revenue: $" + String.format("%.2f", report.totalRevenue()));
        System.out.println("Occupancy Rate: " + String.format("%.2f%%", report.occupancyRate() * 100));
        System.out.println("Total Reservations: " + report.totalReservations());
        System.out.println("\nReservations by Room Type:");
        report.reservationsByRoomType().forEach((roomType, count) -> 
            System.out.println(roomType + ": " + count)
        );

        // Test check-out and bill calculation
        System.out.println("\nChecking out room 101...");
        double bill = reservationService.calculateBill(101);
        System.out.println("Bill for room 101: $" + String.format("%.2f", bill));
        reservationService = reservationService.checkout(101);
        
        // Print available rooms after checkout
        System.out.println("\nAvailable Rooms after checkout:");
        roomService.getAvailableRooms()
            .forEach(room -> System.out.println("Room " + room.roomNumber() + " (" + room.roomType() + ")"));
    }
} 