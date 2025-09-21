package hospitalmanagementsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;

    public Doctor(Connection connection){
        this.connection = connection;
    }

    // View all doctors
    public void viewDoctors(){
        String query = "SELECT * FROM doctors";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+------------+--------------------+------------------+");
            System.out.println("| Doctor Id  | Name               | Specialization   |");
            System.out.println("+------------+--------------------+------------------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("| %-10s | %-18s | %-16s |\n", id, name, specialization);
                System.out.println("+------------+--------------------+------------------+");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Check doctor exists by ID
    public boolean getDoctorById(int id){
        String query = "SELECT * FROM doctors WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // Admin-only: Add doctor
    public void addDoctor(Scanner scanner) {
        System.out.print("Enter Doctor Name: ");
        String name = scanner.next();
        System.out.print("Enter Specialization: ");
        String specialization = scanner.next();

        String query = "INSERT INTO doctors(name, specialization) VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, specialization);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Doctor added successfully!");
            } else {
                System.out.println("Failed to add doctor!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Admin-only: Remove doctor
    public void removeDoctor(Scanner scanner) {
        System.out.print("Enter Doctor ID to remove: ");
        int id = scanner.nextInt();

        String query = "DELETE FROM doctors WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Doctor removed successfully!");
            } else {
                System.out.println("Doctor not found!");
            }
        } catch (SQLException e) {
            if(e instanceof java.sql.SQLIntegrityConstraintViolationException)
             System.out.println("The doctor is appointed, cannot delete now!!");
             else
            e.printStackTrace();
        }
    }
}
