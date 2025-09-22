package hospitalmanagementsystem;

import java.sql.*;
import java.util.Scanner;

public class UserAuth {
    private Connection connection;
    private Scanner scanner;

    public UserAuth(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Register a new user
    public void register() {
        System.out.print("Choose a username: ");
        String username = scanner.next();
        System.out.print("Choose a password: ");
        String password = scanner.next();

        try {
            String query = "INSERT INTO users(username, password) VALUES(?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            System.out.println("Registration successful! Please login now.");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username already exists! Try again.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Login existing user
    public boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful! Welcome, " + username);
                return true;
            } else {
                System.out.println("Invalid credentials! Try again or register first.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
