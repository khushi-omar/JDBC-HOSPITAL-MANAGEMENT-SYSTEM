package hospitalmanagementsystem;

import java.sql.*;

public class User {
    private Connection connection;

    public User(Connection connection) {
        this.connection = connection;
    }

    // Authenticate admin from DB
    public boolean authenticateAdmin(String username, String password) {
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if row exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
