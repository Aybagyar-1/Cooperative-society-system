package dao;

import model.Admin;
import java.sql.*;

public class AdminDAO {
    public Admin getAdminByUsername(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            System.err.println("Invalid username parameter: " + username);
            return null;
        }
        String sql = "SELECT * FROM Admins WHERE LOWER(username) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username.toLowerCase());
            System.out.println("Executing query: " + sql + " with username: " + username.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin(
                    rs.getInt("admin_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                System.out.println("Retrieved admin: " + admin.getUsername() + ", Password: " + (admin.getPassword() != null ? "set" : "null"));
                return admin;
            }
            System.out.println("No admin found for username: " + username.toLowerCase());
            return null;
        } catch (SQLException e) {
            System.err.println("SQLException in getAdminByUsername: " + e.getMessage());
            throw e;
        }
    }
}
