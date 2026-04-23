// FILE: HotelBookingApp.java
// Hotel Booking Management System - Base Setup

import java.io.*;
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
// ============================================================
// UC6: Reservation Confirmation & Room Allocation
// Concepts: Set (uniqueness), HashMap<String,Set<String>>,
//           Atomic allocation, Inventory synchronization
// ============================================================

// Room Allocation Service — assigns unique room IDs
class RoomAllocationService {
    // Set ensures no room ID is assigned twice (prevents double-booking)
    private Set<String> allocatedRoomIds;

    // Maps room type -> set of assigned room IDs
    private HashMap<String, Set<String>> roomTypeAllocations;

    private RoomInventory inventory;
    private int roomCounter = 1;

    public RoomAllocationService(RoomInventory inventory) {
        this.inventory           = inventory;
        this.allocatedRoomIds    = new HashSet<>();
        this.roomTypeAllocations = new HashMap<>();
    }

    // Allocate a room for a reservation
    public String allocateRoom(Reservation reservation) {
        String roomType = reservation.getRoomType();

        // Check availability before allocating
        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("[FAILED] No rooms available for: " + roomType);
            return null;
        }

        // Generate unique room ID
        String roomId = roomType.toUpperCase().substring(0, 3) + "-" + String.format("%03d", roomCounter++);

        // Ensure uniqueness using Set
        if (allocatedRoomIds.contains(roomId)) {
            System.out.println("[FAILED] Room ID already exists: " + roomId);
            return null;
        }

        // Record the allocation
        allocatedRoomIds.add(roomId);
        roomTypeAllocations.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

        // Decrement inventory immediately
        inventory.decrementAvailability(roomType);

        System.out.println("[CONFIRMED] " + reservation.getGuestName() +
                           " -> Room: " + roomId +
                           " | Reservation: " + reservation.getReservationId());
        return roomId;
    }

    public void displayAllocations() {
        System.out.println("\n--- Room Allocations ---");
        for (Map.Entry<String, Set<String>> entry : roomTypeAllocations.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    public Set<String> getAllocatedRoomIds() { return allocatedRoomIds; }
}

class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v6.0");
        System.out.println("   Reservation Confirmation & Allocation");
        System.out.println("============================================");

        // Setup inventory and queue
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService(inventory);

        // Add booking requests
        queue.addRequest(new Reservation("Hamsika",     "Single", 2));
        queue.addRequest(new Reservation("Ayush",       "Double", 3));
        queue.addRequest(new Reservation("Tirthapooja", "Suite",  1));

        // Process requests in FIFO order
        System.out.println("\n[Processing Queue]");
        while (!queue.isEmpty()) {
            Reservation r = queue.poll();
            allocationService.allocateRoom(r);
        }

        // Show allocations and updated inventory
        allocationService.displayAllocations();
        inventory.displayInventory();
    }
}
// ============================================================
// UC7: Add-On Service Selection
// Concepts: One-to-many relationship, Map<String,List<Service>>,
//           Composition, Cost aggregation
// ============================================================

// Add-On Service — represents an optional hotel service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost        = cost;
    }

    public String getServiceName() { return serviceName; }
    public double getCost()        { return cost; }

    @Override
    public String toString() {
        return serviceName + " (Rs." + cost + ")";
    }
}

// Add-On Service Manager — maps reservation IDs to selected services
class AddOnServiceManager {
    // One reservation -> many services
    private HashMap<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    // Add a service to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
        System.out.println("[Service Added] " + reservationId + " -> " + service);
    }

    // Calculate total add-on cost for a reservation
    public double getTotalAddOnCost(String reservationId) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        double total = 0;
        for (AddOnService s : services) { total += s.getCost(); }
        return total;
    }

    // Display all services for a reservation
    public void displayServices(String reservationId) {
        System.out.println("\n--- Add-On Services for " + reservationId + " ---");
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }
        for (AddOnService s : services) { System.out.println("  -> " + s); }
        System.out.println("Total Add-On Cost: Rs." + getTotalAddOnCost(reservationId));
    }
}

class UseCase7AddOnServiceSelection {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v7.0");
        System.out.println("   Add-On Service Selection");
        System.out.println("============================================");

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Simulate guest adding services to their reservation
        String resId = "RES-001";
        System.out.println("\n[Guest selecting add-ons for " + resId + "]");
        serviceManager.addService(resId, new AddOnService("Airport Pickup",  800.0));
        serviceManager.addService(resId, new AddOnService("Breakfast Plan",  500.0));
        serviceManager.addService(resId, new AddOnService("Spa Package",    1500.0));

        // Display selected services and total cost
        serviceManager.displayServices(resId);
    }
}
// ============================================================
// UC8: Booking History & Reporting
// Concepts: List (ordered storage), Historical tracking,
//           Reporting service, Persistence mindset
// ============================================================

// Booking History — stores confirmed reservations in order
class BookingHistory {
    // List preserves insertion order — natural chronological record
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add a confirmed reservation to history
    public void addToHistory(Reservation reservation) {
        history.add(reservation);
        System.out.println("[History Recorded] " + reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getAllBookings() { return history; }

    // Display full booking history
    public void displayHistory() {
        System.out.println("\n--- Booking History ---");
        if (history.isEmpty()) {
            System.out.println("No bookings recorded.");
            return;
        }
        int index = 1;
        for (Reservation r : history) {
            System.out.println(index++ + ". " + r);
        }
    }
}

// Booking Report Service — generates summaries from history
class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Generate a simple summary report
    public void generateReport() {
        System.out.println("\n========== Booking Report ==========");
        List<Reservation> bookings = history.getAllBookings();
        System.out.println("Total Bookings : " + bookings.size());

        // Count by room type
        HashMap<String, Integer> countByType = new HashMap<>();
        for (Reservation r : bookings) {
            countByType.merge(r.getRoomType(), 1, Integer::sum);
        }
        System.out.println("Bookings by Room Type:");
        for (Map.Entry<String, Integer> e : countByType.entrySet()) {
            System.out.println("  " + e.getKey() + " : " + e.getValue());
        }
        System.out.println("=====================================");
    }
}

class UseCase8BookingHistoryReport {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v8.0");
        System.out.println("   Booking History & Reporting");
        System.out.println("============================================");

        BookingHistory history = new BookingHistory();

        // Simulate confirmed reservations being recorded
        System.out.println("\n[Recording Confirmed Bookings]");
        history.addToHistory(new Reservation("Hamsika",     "Single", 2));
        history.addToHistory(new Reservation("Ayush",       "Double", 3));
        history.addToHistory(new Reservation("Tirthapooja", "Suite",  1));
        history.addToHistory(new Reservation("Ravi",        "Single", 1));

        // Display history
        history.displayHistory();

        // Generate report
        BookingReportService reportService = new BookingReportService(history);
        reportService.generateReport();
    }
}
// ============================================================
// UC9: Error Handling & Validation
// Concepts: Custom exceptions, Input validation,
//           Fail-fast design, Graceful failure
// ============================================================

// Custom exception for invalid room type
class InvalidRoomTypeException extends Exception {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

// Custom exception for unavailable rooms
class RoomNotAvailableException extends Exception {
    public RoomNotAvailableException(String message) {
        super(message);
    }
}

// Validator — validates input before processing
class InvalidBookingValidator {
    private static final List<String> VALID_ROOM_TYPES =
        Arrays.asList("Single", "Double", "Suite");

    // Validate room type (case-sensitive)
    public static void validateRoomType(String roomType) throws InvalidRoomTypeException {
        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidRoomTypeException(
                "Invalid room type: '" + roomType + "'. Valid types: " + VALID_ROOM_TYPES);
        }
    }

    // Validate availability
    public static void validateAvailability(String roomType, int available)
            throws RoomNotAvailableException {
        if (available <= 0) {
            throw new RoomNotAvailableException(
                "No rooms available for type: " + roomType);
        }
    }

    // Validate number of nights
    public static void validateNights(int nights) throws IllegalArgumentException {
        if (nights <= 0) {
            throw new IllegalArgumentException(
                "Number of nights must be greater than 0. Provided: " + nights);
        }
    }
}

class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v9.0");
        System.out.println("   Error Handling & Validation");
        System.out.println("============================================");

        RoomInventory inventory = new RoomInventory();

        // Test 1: Invalid room type
        System.out.println("\n[Test 1: Invalid Room Type]");
        try {
            InvalidBookingValidator.validateRoomType("Penthouse"); // Invalid
        } catch (InvalidRoomTypeException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        // Test 2: Valid room type
        System.out.println("\n[Test 2: Valid Room Type]");
        try {
            InvalidBookingValidator.validateRoomType("Single"); // Valid
            System.out.println("[OK] Room type is valid.");
        } catch (InvalidRoomTypeException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        // Test 3: Room not available
        System.out.println("\n[Test 3: Room Not Available]");
        try {
            // Make Suite unavailable
            inventory.decrementAvailability("Suite");
            inventory.decrementAvailability("Suite");
            InvalidBookingValidator.validateAvailability("Suite", inventory.getAvailability("Suite"));
        } catch (RoomNotAvailableException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        // Test 4: Invalid nights
        System.out.println("\n[Test 4: Invalid Number of Nights]");
        try {
            InvalidBookingValidator.validateNights(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        System.out.println("\n[System stable after all error scenarios]");
    }
}
// ============================================================
// UC10: Booking Cancellation & Inventory Rollback
// Concepts: Stack (LIFO), State reversal, Controlled mutation,
//           Inventory restoration, Rollback validation
// ============================================================

// Cancellation Service — handles booking cancellations
class CancellationService {
    // Stack tracks recently released room IDs (LIFO rollback)
    private Stack<String> releasedRoomIds;
    private RoomInventory inventory;
    private BookingHistory history;

    // Active bookings: reservationId -> roomId
    private HashMap<String, String> activeBookings;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory       = inventory;
        this.history         = history;
        this.releasedRoomIds = new Stack<>();
        this.activeBookings  = new HashMap<>();
    }

    // Register a confirmed booking
    public void registerBooking(String reservationId, String roomId, String roomType) {
        activeBookings.put(reservationId, roomId + ":" + roomType);
        System.out.println("[Registered] " + reservationId + " -> " + roomId);
    }

    // Cancel a booking and rollback inventory
    public void cancelBooking(String reservationId) {
        // Validate booking exists
        if (!activeBookings.containsKey(reservationId)) {
            System.out.println("[FAILED] Reservation not found: " + reservationId);
            return;
        }

        String[] parts    = activeBookings.get(reservationId).split(":");
        String roomId     = parts[0];
        String roomType   = parts[1];

        // Push released room ID onto stack (LIFO rollback tracking)
        releasedRoomIds.push(roomId);

        // Restore inventory
        inventory.incrementAvailability(roomType);

        // Remove from active bookings
        activeBookings.remove(reservationId);

        System.out.println("[CANCELLED] Reservation: " + reservationId +
                           " | Room: " + roomId + " released.");
        System.out.println("[INVENTORY RESTORED] " + roomType +
                           " now has " + inventory.getAvailability(roomType) + " rooms.");
    }

    public void displayReleasedRooms() {
        System.out.println("\n--- Released Room IDs (Stack) ---");
        System.out.println(releasedRoomIds);
    }
}

class UseCase10BookingCancellation {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v10.0");
        System.out.println("   Booking Cancellation & Inventory Rollback");
        System.out.println("============================================");

        RoomInventory inventory  = new RoomInventory();
        BookingHistory history   = new BookingHistory();
        CancellationService cs   = new CancellationService(inventory, history);

        // Register some bookings
        System.out.println("\n[Registering Bookings]");
        cs.registerBooking("RES-001", "SIN-001", "Single");
        cs.registerBooking("RES-002", "DOU-001", "Double");
        cs.registerBooking("RES-003", "SUI-001", "Suite");

        // Show inventory before cancellation
        inventory.displayInventory();

        // Cancel a booking
        System.out.println("\n[Cancelling RES-002]");
        cs.cancelBooking("RES-002");

        // Try cancelling non-existent booking
        System.out.println("\n[Cancelling non-existent RES-999]");
        cs.cancelBooking("RES-999");

        // Show inventory after cancellation
        inventory.displayInventory();

        // Show released rooms stack
        cs.displayReleasedRooms();
    }
}
// ============================================================
// UC11: Concurrent Booking Simulation (Thread Safety)
// Concepts: Race conditions, Thread safety, Shared mutable state,
//           Critical sections, Synchronized access
// ============================================================

// Thread-safe booking processor
class ConcurrentBookingProcessor {
    private RoomInventory inventory;
    private Set<String> allocatedRoomIds;
    private int roomCounter = 1;

    public ConcurrentBookingProcessor(RoomInventory inventory) {
        this.inventory        = inventory;
        this.allocatedRoomIds = new HashSet<>();
    }

    // Synchronized method — only one thread can execute at a time
    public synchronized void processBooking(Reservation reservation) {
        String roomType = reservation.getRoomType();

        // Critical section: check and update inventory atomically
        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("[THREAD " + Thread.currentThread().getName() +
                               "] FAILED - No rooms for: " + roomType);
            return;
        }

        // Generate unique room ID
        String roomId = roomType.toUpperCase().substring(0, 3) +
                        "-" + String.format("%03d", roomCounter++);

        // Allocate and update inventory
        allocatedRoomIds.add(roomId);
        inventory.decrementAvailability(roomType);

        System.out.println("[THREAD " + Thread.currentThread().getName() +
                           "] CONFIRMED: " + reservation.getGuestName() +
                           " -> " + roomId);
    }
}

// Booking thread — simulates a guest submitting a request
class BookingThread extends Thread {
    private ConcurrentBookingProcessor processor;
    private Reservation reservation;

    public BookingThread(String name, ConcurrentBookingProcessor processor,
                         Reservation reservation) {
        super(name);
        this.processor   = processor;
        this.reservation = reservation;
    }

    @Override
    public void run() {
        processor.processBooking(reservation);
    }
}

class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v11.0");
        System.out.println("   Concurrent Booking Simulation");
        System.out.println("============================================");

        RoomInventory inventory = new RoomInventory();
        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(inventory);

        System.out.println("\n[Launching Concurrent Booking Threads]");

        // Create multiple threads simulating simultaneous guest bookings
        Thread[] threads = {
            new BookingThread("Guest-1", processor, new Reservation("Hamsika",     "Single", 2)),
            new BookingThread("Guest-2", processor, new Reservation("Ayush",       "Single", 1)),
            new BookingThread("Guest-3", processor, new Reservation("Tirthapooja", "Double", 3)),
            new BookingThread("Guest-4", processor, new Reservation("Ravi",        "Suite",  1)),
            new BookingThread("Guest-5", processor, new Reservation("Priya",       "Single", 2))
        };

        // Start all threads simultaneously
        for (Thread t : threads) t.start();

        // Wait for all threads to finish
        for (Thread t : threads) t.join();

        System.out.println("\n[All threads completed]");
        inventory.displayInventory();
    }
}
// ============================================================
// UC12: Data Persistence & System Recovery
// Concepts: Serialization, Deserialization, File I/O,
//           Inventory snapshot, Failure tolerance
// ============================================================

// Persistence Service — saves and restores system state
class PersistenceService {
    private static final String INVENTORY_FILE = "inventory_state.txt";
    private static final String HISTORY_FILE   = "booking_history.txt";

    // Save inventory state to file
    public void saveInventory(RoomInventory inventory) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {
            for (Map.Entry<String, Integer> entry : inventory.getInventory().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("[SAVED] Inventory state saved to " + INVENTORY_FILE);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save inventory: " + e.getMessage());
        }
    }

    // Restore inventory state from file
    public void restoreInventory(RoomInventory inventory) {
        File file = new File(INVENTORY_FILE);
        if (!file.exists()) {
            System.out.println("[RECOVERY] No saved inventory found. Using default state.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(INVENTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String roomType = parts[0];
                    int count       = Integer.parseInt(parts[1]);
                    // Restore: set correct count
                    int current = inventory.getAvailability(roomType);
                    for (int i = 0; i < current; i++) inventory.decrementAvailability(roomType);
                    for (int i = 0; i < count; i++)   inventory.incrementAvailability(roomType);
                }
            }
            System.out.println("[RESTORED] Inventory state loaded from " + INVENTORY_FILE);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to restore inventory: " + e.getMessage());
        }
    }

    // Save booking history to file
    public void saveBookingHistory(BookingHistory history) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            for (Reservation r : history.getAllBookings()) {
                writer.write(r.getReservationId() + "|" +
                             r.getGuestName()     + "|" +
                             r.getRoomType()      + "|" +
                             r.getNights());
                writer.newLine();
            }
            System.out.println("[SAVED] Booking history saved to " + HISTORY_FILE);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save history: " + e.getMessage());
        }
    }

    // Restore booking history from file
    public void restoreBookingHistory(BookingHistory history) {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            System.out.println("[RECOVERY] No saved booking history found.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    Reservation r = new Reservation(parts[1], parts[2],
                                                    Integer.parseInt(parts[3]));
                    history.addToHistory(r);
                }
            }
            System.out.println("[RESTORED] Booking history loaded from " + HISTORY_FILE);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to restore history: " + e.getMessage());
        }
    }
}

class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        System.out.println("============================================");
        System.out.println("   Book My Stay App  v12.0");
        System.out.println("   Data Persistence & System Recovery");
        System.out.println("============================================");

        PersistenceService persistence = new PersistenceService();

        // --- SIMULATE SYSTEM BEFORE SHUTDOWN ---
        System.out.println("\n[Phase 1: System Running - Saving State]");
        RoomInventory inventory   = new RoomInventory();
        BookingHistory history    = new BookingHistory();

        // Simulate some bookings
        inventory.decrementAvailability("Single");
        inventory.decrementAvailability("Double");
        history.addToHistory(new Reservation("Hamsika", "Single", 2));
        history.addToHistory(new Reservation("Ayush",   "Double", 3));

        // Display state before saving
        inventory.displayInventory();
        history.displayHistory();

        // Save state to files
        persistence.saveInventory(inventory);
        persistence.saveBookingHistory(history);

        // --- SIMULATE SYSTEM RESTART ---
        System.out.println("\n[Phase 2: System Restarted - Restoring State]");
        RoomInventory recoveredInventory = new RoomInventory();
        BookingHistory recoveredHistory  = new BookingHistory();

        // Restore from files
        persistence.restoreInventory(recoveredInventory);
        persistence.restoreBookingHistory(recoveredHistory);

        // Display recovered state
        System.out.println("\n[Recovered State]");
        recoveredInventory.displayInventory();
        recoveredHistory.displayHistory();
    }
}