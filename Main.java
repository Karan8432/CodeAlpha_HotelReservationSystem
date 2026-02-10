package main;

import model.Room;
import model.Booking;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<Booking> bookings = new ArrayList<>();

    // Save bookings to file (simple)
    static void saveBookings() {
        try {
            FileWriter fw = new FileWriter("bookings.txt");

            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                fw.write(b.roomNumber + "," + b.customerName + "," + b.amountPaid + "\n");
            }

            fw.close();
        } catch (Exception e) {
            System.out.println("Error saving bookings.");
        }
    }

    // Load bookings from file (simple)
    static void loadBookings() {
        try {
            File file = new File("bookings.txt");
            if (!file.exists()) {
                return;
            }

            Scanner fs = new Scanner(file);
            while (fs.hasNextLine()) {
                String line = fs.nextLine();
                String[] data = line.split(",");

                int roomNo = Integer.parseInt(data[0]);
                String name = data[1];
                double amount = Double.parseDouble(data[2]);

                bookings.add(new Booking(roomNo, name, amount));
            }

            fs.close();
        } catch (Exception e) {
            System.out.println("Error loading bookings.");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Create rooms
        ArrayList<Room> rooms = new ArrayList<>();
        rooms.add(new Room(101, "Standard", 2000));
        rooms.add(new Room(102, "Deluxe", 3500));
        rooms.add(new Room(103, "Suite", 5000));

        // Load bookings
        loadBookings();

        // Mark booked rooms
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);

            for (int j = 0; j < rooms.size(); j++) {
                Room r = rooms.get(j);
                if (r.roomNumber == b.roomNumber) {
                    r.isBooked = true;
                }
            }
        }

        int choice;

        do {
            System.out.println("\n=== Hotel Reservation System ===");
            System.out.println("1. View All Rooms");
            System.out.println("2. Search Available Rooms by Category");
            System.out.println("3. Book Room");
            System.out.println("4. Cancel Booking");
            System.out.println("5. View Booking Details");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("\n--- All Rooms ---");
                    for (int i = 0; i < rooms.size(); i++) {
                        Room r = rooms.get(i);
                        System.out.println(
                                "Room " + r.roomNumber +
                                " | " + r.category +
                                " | ₹" + r.price +
                                " | Status: " + (r.isBooked ? "Booked" : "Available")
                        );
                    }
                    break;

                case 2:
                    System.out.print("Enter category (Standard/Deluxe/Suite): ");
                    String cat = sc.nextLine();
                    boolean found = false;

                    for (int i = 0; i < rooms.size(); i++) {
                        Room r = rooms.get(i);
                        if (r.category.equalsIgnoreCase(cat) && !r.isBooked) {
                            System.out.println("Room " + r.roomNumber + " | ₹" + r.price);
                            found = true;
                        }
                    }

                    if (!found) {
                        System.out.println("No available rooms in this category.");
                    }
                    break;

                case 3:
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter room number: ");
                    int rn = sc.nextInt();

                    boolean booked = false;

                    for (int i = 0; i < rooms.size(); i++) {
                        Room r = rooms.get(i);
                        if (r.roomNumber == rn && !r.isBooked) {
                            System.out.println("Pay ₹" + r.price + " to confirm booking (simulated)");
                            r.isBooked = true;
                            bookings.add(new Booking(rn, name, r.price));
                            saveBookings();
                            System.out.println("Booking successful!");
                            booked = true;
                            break;
                        }
                    }

                    if (!booked) {
                        System.out.println("Room not available or already booked.");
                    }
                    break;

                case 4:
                    System.out.print("Enter room number to cancel: ");
                    int cancel = sc.nextInt();

                    boolean cancelled = false;

                    for (int i = 0; i < bookings.size(); i++) {
                        if (bookings.get(i).roomNumber == cancel) {
                            bookings.remove(i);
                            cancelled = true;
                            break;
                        }
                    }

                    for (int i = 0; i < rooms.size(); i++) {
                        Room r = rooms.get(i);
                        if (r.roomNumber == cancel) {
                            r.isBooked = false;
                        }
                    }

                    if (cancelled) {
                        saveBookings();
                        System.out.println("Booking cancelled successfully.");
                    } else {
                        System.out.println("Invalid room number or room not booked.");
                    }
                    break;

                case 5:
                    System.out.println("\n--- Booking Details ---");
                    if (bookings.size() == 0) {
                        System.out.println("No bookings found.");
                    } else {
                        for (int i = 0; i < bookings.size(); i++) {
                            Booking b = bookings.get(i);
                            System.out.println(
                                    "Room: " + b.roomNumber +
                                    ", Name: " + b.customerName +
                                    ", Paid: ₹" + b.amountPaid
                            );
                        }
                    }
                    break;

                case 6:
                    System.out.println("Thank you for using Hotel Reservation System.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        sc.close();
    }
}