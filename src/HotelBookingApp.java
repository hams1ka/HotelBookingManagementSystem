// FILE: HotelBookingApp.java
// Hotel Booking Management System - Base Setup

import java.util.*;
import java.io.*;

// ============================================================
// UC1: Application Entry & Welcome Message
// Concepts: class, main(), static, System.out.println,
//           String literals, JavaDoc comments
// ============================================================

/**
 * HotelBookingApp - Entry point for the Hotel Booking Management System.
 * @author Hamsika
 * @version 1.0
 */
class UseCase1HotelBookingApp {
    public static void main(String[] args) {
        // Print welcome message and application details
        System.out.println("============================================");
        System.out.println("   Welcome to Book My Stay App");
        System.out.println("   Hotel Booking Management System v1.0");
        System.out.println("============================================");
        System.out.println("Application started successfully.");
        System.out.println("Initializing system components...");
    }
}
// ============================================================
// UC2: Basic Room Types & Static Availability
// Concepts: Abstract class, Inheritance, Polymorphism,
//           Encapsulation, Static availability variables
// Version: 2.0
// ============================================================

// Abstract base class representing a generic hotel room
abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double pricePerNight;
    protected String amenities;

    // Constructor to initialize common room attributes
    public Room(String roomType, int numberOfBeds, double pricePerNight, String amenities) {
        this.roomType     = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
        this.amenities    = amenities;
    }

    // Abstract method — each room type must define its own display
    public abstract void displayRoomDetails();

    public String getRoomType()       { return roomType; }
    public double getPricePerNight()  { return pricePerNight; }
}

// Single room — extends Room
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single", 1, 2500.0, "WiFi, TV, AC");
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type  : " + roomType);
        System.out.println("Beds       : " + numberOfBeds);
        System.out.println("Price/Night: Rs." + pricePerNight);
        System.out.println("Amenities  : " + amenities);
    }
}

// Double room — extends Room
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double", 2, 4500.0, "WiFi, TV, AC, Mini Bar");
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type  : " + roomType);
        System.out.println("Beds       : " + numberOfBeds);
        System.out.println("Price/Night: Rs." + pricePerNight);
        System.out.println("Amenities  : " + amenities);
    }
}

// Suite room — extends Room
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite", 3, 9000.0, "WiFi, TV, AC, Mini Bar, Jacuzzi, Butler");
    }
    @Override
    public void displayRoomDetails() {
        System.out.println("Room Type  : " + roomType);
        System.out.println("Beds       : " + numberOfBeds);
        System.out.println("Price/Night: Rs." + pricePerNight);
        System.out.println("Amenities  : " + amenities);
    }
}

class UseCase2RoomInitialization {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v2.0");
        System.out.println("   Room Initialization");
        System.out.println("============================================");

        // Create room objects using polymorphism (Room reference)
        Room single = new SingleRoom();
        Room dbl    = new DoubleRoom();
        Room suite  = new SuiteRoom();

        // Static availability variables (limitation: not scalable)
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable  = 2;

        // Display each room's details
        System.out.println("\n--- Single Room ---");
        single.displayRoomDetails();
        System.out.println("Available  : " + singleAvailable);

        System.out.println("\n--- Double Room ---");
        dbl.displayRoomDetails();
        System.out.println("Available  : " + doubleAvailable);

        System.out.println("\n--- Suite Room ---");
        suite.displayRoomDetails();
        System.out.println("Available  : " + suiteAvailable);
    }
}