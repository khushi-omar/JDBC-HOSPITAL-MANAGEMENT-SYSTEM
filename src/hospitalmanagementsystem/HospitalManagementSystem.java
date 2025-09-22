package hospitalmanagementsystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Simpson123!@#";

    public static void main(String[] args) {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }

    Scanner scanner = new Scanner(System.in);

    try {
        Connection connection = DriverManager.getConnection(url, username, password);
        Patient patient = new Patient(connection, scanner);
        Doctor doctor = new Doctor(connection);
        User user = new User(connection); // For admin authentication
        UserAuth userAuth = new UserAuth(connection, scanner);

        // === NEW LOGIN / REGISTER FLOW ===
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("\nWELCOME TO HOSPITAL MANAGEMENT SYSTEM");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                loggedIn = userAuth.login();
            } else if (choice == 2) {
                userAuth.register();
            } else {
                System.out.println("Invalid choice!");
            }
        }
        // === END LOGIN FLOW ===

        // === EXISTING MAIN MENU ===
        while (true) {
            System.out.println("\nHOSPITAL MANAGEMENT SYSTEM ");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. View Doctors");
            System.out.println("4. Book Appointment");
            System.out.println("5. Exit");
            System.out.println("6. Manage Doctors (Admin Only)");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    patient.addPatient();
                    break;
                case 2:
                    patient.viewPatients();
                    break;
                case 3:
                    doctor.viewDoctors();
                    break;
                case 4:
                    bookAppointment(patient, doctor, connection, scanner);
                    break;
                case 5:
                    System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!!");
                    return;
                case 6:
                    System.out.print("Enter admin username: ");
                    String adminUser = scanner.next();
                    System.out.print("Enter admin password: ");
                    String adminPass = scanner.next();

                    if (user.authenticateAdmin(adminUser, adminPass)) {
                        manageDoctors(doctor, scanner);
                    } else {
                        System.out.println("Access denied! Only admins can manage doctors.");
                    }
                    break;
                default:
                    System.out.println("Enter valid choice!!!");
                    break;
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked!");
                    }else{
                        System.out.println("Failed to Book Appointment!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor not available on this date!!");
            }
        }else{
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // Admin menu
    public static void manageDoctors(Doctor doctor, Scanner scanner) {
        while (true) {
            System.out.println("\nADMIN - MANAGE DOCTORS");
            System.out.println("1. Add Doctor");
            System.out.println("2. Remove Doctor");
            System.out.println("3. View Doctors");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    doctor.addDoctor(scanner);
                    break;
                case 2:
                    doctor.removeDoctor(scanner);
                    break;
                case 3:
                    doctor.viewDoctors();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
