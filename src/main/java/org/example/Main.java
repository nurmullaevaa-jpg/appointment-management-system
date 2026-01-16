package org.example;

import org.example.dao.*;
import org.example.entity.*;
import org.example.service.AppointmentService;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClientDAO clientDAO = new ClientDAO();
    private static final ServiceDAO serviceDAO = new ServiceDAO();
    private static final AppointmentService appointmentService = new AppointmentService();

    public static void main(String[] args) {
        System.out.println("=== Appointment Management System ===");
        System.out.println("Welcome to the Appointment Management System!\n");

        try {
            DatabaseConnection.getConnection();
            System.out.println("✓ Database connection established\n");
        } catch (SQLException e) {
            System.err.println("✗ Failed to connect to database: " + e.getMessage());
            System.err.println("Please check your database.properties file and ensure PostgreSQL is running.");
            return;
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> handleClientMenu();
                    case 2 -> handleServiceMenu();
                    case 3 -> handleAppointmentMenu();
                    case 4 -> {
                        running = false;
                        System.out.println("Thank you for using Appointment Management System!");
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Manage Clients");
        System.out.println("2. Manage Services");
        System.out.println("3. Manage Appointments");
        System.out.println("4. Exit");
    }

    private static void handleClientMenu() throws SQLException {
        while (true) {
            System.out.println("\n--- Client Management ---");
            System.out.println("1. Register New Client");
            System.out.println("2. List All Clients");
            System.out.println("3. Find Client by ID");
            System.out.println("4. Find Client by Email");
            System.out.println("5. Update Client");
            System.out.println("6. Delete Client");
            System.out.println("7. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> {
                    String name = getStringInput("Enter client name: ");
                    String email = getStringInput("Enter email: ");
                    String phone = getStringInput("Enter phone: ");
                    Client client = new Client(name, email, phone);
                    Integer id = clientDAO.create(client);
                    System.out.println("✓ Client registered with ID: " + id);
                }
                case 2 -> {
                    List<Client> clients = clientDAO.findAll();
                    System.out.println("\nAll Clients:");
                    if (clients.isEmpty()) {
                        System.out.println("No clients found.");
                    } else {
                        clients.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    Integer id = getIntInput("Enter client ID: ");
                    Client client = clientDAO.findById(id);
                    if (client != null) {
                        System.out.println(client);
                    } else {
                        System.out.println("Client not found");
                    }
                }
                case 4 -> {
                    String email = getStringInput("Enter email: ");
                    Client client = clientDAO.findByEmail(email);
                    if (client != null) {
                        System.out.println(client);
                    } else {
                        System.out.println("Client not found");
                    }
                }
                case 5 -> {
                    Integer id = getIntInput("Enter client ID: ");
                    Client client = clientDAO.findById(id);
                    if (client != null) {
                        client.setName(getStringInput("Enter new name (current: " + client.getName() + "): "));
                        client.setEmail(getStringInput("Enter new email (current: " + client.getEmail() + "): "));
                        client.setPhone(getStringInput("Enter new phone (current: " + client.getPhone() + "): "));
                        if (clientDAO.update(client)) {
                            System.out.println("✓ Client updated");
                        }
                    } else {
                        System.out.println("Client not found");
                    }
                }
                case 6 -> {
                    Integer id = getIntInput("Enter client ID to delete: ");
                    if (clientDAO.delete(id)) {
                        System.out.println("✓ Client deleted");
                    } else {
                        System.out.println("Client not found");
                    }
                }
                case 7 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void handleServiceMenu() throws SQLException {
        while (true) {
            System.out.println("\n--- Service Management ---");
            System.out.println("1. Create Service");
            System.out.println("2. List All Services");
            System.out.println("3. Find Service by ID");
            System.out.println("4. Update Service");
            System.out.println("5. Delete Service");
            System.out.println("6. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> {
                    String name = getStringInput("Enter service name: ");
                    Integer duration = getIntInput("Enter duration (minutes): ");
                    BigDecimal price = new BigDecimal(getStringInput("Enter price: "));
                    Service service = new Service(name, duration, price);
                    Integer id = serviceDAO.create(service);
                    System.out.println("✓ Service created with ID: " + id);
                }
                case 2 -> {
                    List<Service> services = serviceDAO.findAll();
                    System.out.println("\nAll Services:");
                    if (services.isEmpty()) {
                        System.out.println("No services found.");
                    } else {
                        services.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    Integer id = getIntInput("Enter service ID: ");
                    Service service = serviceDAO.findById(id);
                    if (service != null) {
                        System.out.println(service);
                    } else {
                        System.out.println("Service not found");
                    }
                }
                case 4 -> {
                    Integer id = getIntInput("Enter service ID: ");
                    Service service = serviceDAO.findById(id);
                    if (service != null) {
                        service.setName(getStringInput("Enter new name (current: " + service.getName() + "): "));
                        service.setDuration(getIntInput("Enter new duration (current: " + service.getDuration() + "): "));
                        service.setPrice(new BigDecimal(getStringInput("Enter new price (current: " + service.getPrice() + "): ")));
                        if (serviceDAO.update(service)) {
                            System.out.println("✓ Service updated");
                        }
                    } else {
                        System.out.println("Service not found");
                    }
                }
                case 5 -> {
                    Integer id = getIntInput("Enter service ID to delete: ");
                    if (serviceDAO.delete(id)) {
                        System.out.println("✓ Service deleted");
                    } else {
                        System.out.println("Service not found");
                    }
                }
                case 6 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static void handleAppointmentMenu() throws SQLException {
        while (true) {
            System.out.println("\n--- Appointment Management ---");
            System.out.println("1. Book Appointment");
            System.out.println("2. List All Appointments");
            System.out.println("3. Find Appointment by ID");
            System.out.println("4. Find Appointments by Client");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Complete Appointment");
            System.out.println("7. Back to Main Menu");

            int choice = getIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> {
                    Integer clientId = getIntInput("Enter client ID: ");
                    Integer serviceId = getIntInput("Enter service ID: ");
                    String dateStr = getStringInput("Enter appointment date (YYYY-MM-DD): ");
                    String timeStr = getStringInput("Enter appointment time (HH:MM:SS): ");
                    Date date = Date.valueOf(dateStr);
                    Time time = Time.valueOf(timeStr);
                    Integer id = appointmentService.bookAppointment(clientId, serviceId, date, time);
                    System.out.println("✓ Appointment booked with ID: " + id);
                }
                case 2 -> {
                    AppointmentDAO appointmentDAO = new AppointmentDAO();
                    List<Appointment> appointments = appointmentDAO.findAll();
                    System.out.println("\nAll Appointments:");
                    if (appointments.isEmpty()) {
                        System.out.println("No appointments found.");
                    } else {
                        appointments.forEach(System.out::println);
                    }
                }
                case 3 -> {
                    Integer id = getIntInput("Enter appointment ID: ");
                    Appointment appointment = appointmentService.getAppointment(id);
                    if (appointment != null) {
                        System.out.println(appointment);
                    } else {
                        System.out.println("Appointment not found");
                    }
                }
                case 4 -> {
                    Integer clientId = getIntInput("Enter client ID: ");
                    AppointmentDAO appointmentDAO = new AppointmentDAO();
                    List<Appointment> appointments = appointmentDAO.findByClientId(clientId);
                    System.out.println("\nAppointments for Client ID " + clientId + ":");
                    if (appointments.isEmpty()) {
                        System.out.println("No appointments found for this client.");
                    } else {
                        appointments.forEach(System.out::println);
                    }
                }
                case 5 -> {
                    Integer id = getIntInput("Enter appointment ID to cancel: ");
                    if (appointmentService.cancelAppointment(id)) {
                        System.out.println("✓ Appointment cancelled");
                    }
                }
                case 6 -> {
                    Integer id = getIntInput("Enter appointment ID to complete: ");
                    if (appointmentService.completeAppointment(id)) {
                        System.out.println("✓ Appointment marked as completed");
                    }
                }
                case 7 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            int value = Integer.parseInt(scanner.nextLine().trim());
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Please try again.");
            return getIntInput(prompt);
        }
    }
}
