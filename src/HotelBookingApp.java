// FILE: HotelBookingApp.java
// Hotel Booking Management System - Base Setup

import java.util.*;

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
// ============================================================
// UC3: Centralized Room Inventory Management
// Concepts: HashMap, O(1) lookup, Single source of truth,
//           Encapsulation of inventory logic
// Version: 3.0
// ============================================================

// Centralized inventory — stores room type -> available count
class RoomInventory {
    // HashMap: roomType -> number of available rooms
    private HashMap<String, Integer> inventory;

    // Initialize inventory with default room counts
    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite",  2);
    }

    // Get availability for a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Decrement availability (called during booking)
    public void decrementAvailability(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }

    // Increment availability (called during cancellation)
    public void incrementAvailability(String roomType) {
        inventory.put(roomType, getAvailability(roomType) + 1);
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }

    public HashMap<String, Integer> getInventory() { return inventory; }
}

class UseCase3InventorySetup {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v3.0");
        System.out.println("   Centralized Inventory Management");
        System.out.println("============================================");

        // Create centralized inventory
        RoomInventory roomInventory = new RoomInventory();

        // Display initial inventory
        roomInventory.displayInventory();

        // Simulate a booking — decrement Single room
        System.out.println("\n[Booking] Reserving 1 Single room...");
        roomInventory.decrementAvailability("Single");

        // Display updated inventory
        roomInventory.displayInventory();
    }
}
// ============================================================
// UC4: Room Search & Availability Check
// Concepts: Read-only access, Defensive programming,
//           Separation of concerns, Validation logic
// ============================================================

// Search service — read-only access to inventory and room info
class RoomSearchService {
    private RoomInventory inventory;
    private List<Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
        // Room catalog — all available room types
        roomCatalog = new ArrayList<>();
        roomCatalog.add(new SingleRoom());
        roomCatalog.add(new DoubleRoom());
        roomCatalog.add(new SuiteRoom());
    }

    // Display only rooms with availability > 0 (read-only, no state change)
    public void searchAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        boolean anyAvailable = false;
        for (Room room : roomCatalog) {
            int available = inventory.getAvailability(room.getRoomType());
            if (available > 0) {
                // Only show rooms that can actually be booked
                room.displayRoomDetails();
                System.out.println("Available  : " + available + " rooms");
                System.out.println("------------------------------");
                anyAvailable = true;
            }
        }
        if (!anyAvailable) {
            System.out.println("No rooms currently available.");
        }
    }
}

class UseCase4RoomSearch {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v4.0");
        System.out.println("   Room Search & Availability Check");
        System.out.println("============================================");

        // Setup inventory
        RoomInventory inventory = new RoomInventory();

        // Simulate no suites available
        inventory.decrementAvailability("Suite");
        inventory.decrementAvailability("Suite");

        // Search service reads inventory without modifying it
        RoomSearchService searchService = new RoomSearchService(inventory);
        searchService.searchAvailableRooms();
    }
}
// ============================================================
// UC5: Booking Request Queue (First-Come-First-Served)
// Concepts: Queue, FIFO, Fairness, Request ordering,
//           Decoupling intake from allocation
// ============================================================

// Reservation — represents a guest's booking intent
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;
    private String reservationId;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName     = guestName;
        this.roomType      = roomType;
        this.nights        = nights;
        // Generate simple reservation ID
        this.reservationId = "RES-" + System.currentTimeMillis();
    }

    public String getGuestName()     { return guestName; }
    public String getRoomType()      { return roomType; }
    public int getNights()           { return nights; }
    public String getReservationId() { return reservationId; }

    @Override
    public String toString() {
        return "[" + reservationId + "] " + guestName + " | " + roomType + " | " + nights + " night(s)";
    }
}

// Booking queue — stores requests in FIFO order
class BookingRequestQueue {
    // Queue preserves arrival order (FIFO)
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add a new booking request to the end of the queue
    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("[Queued] " + reservation);
    }

    // Peek at next request without removing
    public Reservation peek() { return requestQueue.peek(); }

    // Remove and return the front request (FIFO)
    public Reservation poll() { return requestQueue.poll(); }

    public boolean isEmpty()  { return requestQueue.isEmpty(); }
    public int size()         { return requestQueue.size(); }

    public void displayQueue() {
        System.out.println("\n--- Booking Request Queue ---");
        if (requestQueue.isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }
}

class UseCase5BookingRequestQueue {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v5.0");
        System.out.println("   Booking Request Queue (FIFO)");
        System.out.println("============================================");

        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulate guests submitting booking requests
        System.out.println("\n[Guests Submitting Requests]");
        queue.addRequest(new Reservation("Hamsika",  "Single", 2));
        queue.addRequest(new Reservation("Ayush",    "Double", 3));
        queue.addRequest(new Reservation("Tirthapooja", "Suite", 1));

        // Display queued requests
        queue.displayQueue();
        System.out.println("\nTotal requests in queue: " + queue.size());
        System.out.println("Next to be processed   : " + queue.peek());
    }
}