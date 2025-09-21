package hospitalmanagementsystem;

import java.sql.*;

public class Authentication {
    public static boolean login(String username, String password, Connection connection){
        String query = "SELECT * FROM admins WHERE username = ? AND password = ?";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if a row found
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
