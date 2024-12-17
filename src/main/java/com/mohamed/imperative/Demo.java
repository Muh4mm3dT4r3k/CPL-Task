package com.mohamed.imperative;

import com.mohamed.imperative.customer.Customer;
import com.mohamed.imperative.customer.CustomerService;
import com.mohamed.imperative.report.Report;
import com.mohamed.imperative.report.ReportingService;
import com.mohamed.imperative.reservation.ReservationService;
import com.mohamed.imperative.room.Room;
import com.mohamed.imperative.room.RoomService;
import com.mohamed.imperative.room.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Demo {
    public static void main(String[] args) throws ParseException {
        // Initialize services
        RoomService roomService = new RoomService();
        CustomerService customerService = new CustomerService();
        ReservationService reservationService = new ReservationService(roomService, customerService);
        ReportingService reportingService = new ReportingService(reservationService, roomService);

        // Add dummy rooms
        roomService.addRoom(new Room(101, RoomType.SINGLE, 100.0));
        roomService.addRoom(new Room(102, RoomType.SINGLE, 100.0));
        roomService.addRoom(new Room(201, RoomType.DOUBLE, 150.0));
        roomService.addRoom(new Room(202, RoomType.DOUBLE, 150.0));
        roomService.addRoom(new Room(301, RoomType.SUITE, 300.0));

        // Add dummy customers
        customerService.addCustomer(new Customer(1, "John Doe", "john@email.com", "Credit Card"));
        customerService.addCustomer(new Customer(2, "Jane Smith", "jane@email.com", "Debit Card"));
        customerService.addCustomer(new Customer(3, "Bob Wilson", "bob@email.com", "Cash"));

        // Create date formatter
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Create some reservations
        reservationService.createReservation(101, 1, 
            dateFormat.parse("2024-03-01"), 
            dateFormat.parse("2024-03-03"));
        
        reservationService.createReservation(201, 2, 
            dateFormat.parse("2024-03-02"), 
            dateFormat.parse("2024-03-05"));
        
        reservationService.createReservation(301, 3, 
            dateFormat.parse("2024-03-01"), 
            dateFormat.parse("2024-03-04"));

        // Generate and print report
        Report report = reportingService.generateReport(
            dateFormat.parse("2024-03-01"),
            dateFormat.parse("2024-03-05")
        );

        // Print report details
        System.out.println("Hotel Management System Report");
        System.out.println("=============================");
        System.out.println("Period: " + dateFormat.format(report.getStartDate()) + 
                          " to " + dateFormat.format(report.getEndDate()));
        System.out.println("Total Revenue: $" + String.format("%.2f", report.getTotalRevenue()));
        System.out.println("Occupancy Rate: " + String.format("%.2f%%", report.getOccupancyRate() * 100));
        System.out.println("Total Reservations: " + report.getTotalReservations());
        System.out.println("\nReservations by Room Type:");
        report.getReservationsByRoomType().forEach((roomType, count) -> 
            System.out.println(roomType + ": " + count)
        );

        // Test check-out and bill calculation
        System.out.println("\nChecking out room 101...");
        double bill = reservationService.calculateBill(101);
        System.out.println("Bill for room 101: $" + String.format("%.2f", bill));
        reservationService.checkOut(101);
        
        // Print available rooms after checkout
        System.out.println("\nAvailable Rooms after checkout:");
        roomService.getAvailableRooms().forEach(room -> 
            System.out.println("Room " + room.getRoomNumber() + " (" + room.getRoomType() + ")")
        );
    }
} 