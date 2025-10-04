package dao;

import model.Member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MemberDAO {
    public Member registerMember(Member member) throws SQLException {
        if (member.getPassword() == null || member.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        String sql = "INSERT INTO Members (surname, first_name, middle_name, email, phone, password, join_date, status) " +
                     "VALUES (?, ?, ?, LOWER(?), ?, ?, CURDATE(), 'active')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, member.getSurname());
            stmt.setString(2, member.getFirstName());
            stmt.setString(3, member.getMiddleName());
            stmt.setString(4, member.getEmail().toLowerCase());
            stmt.setString(5, member.getPhone());
            stmt.setString(6, hashPassword(member.getPassword()));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int memberId = rs.getInt(1);
                    return new Member(
                        memberId,
                        member.getSurname(),
                        member.getFirstName(),
                        member.getMiddleName(),
                        member.getEmail(),
                        member.getPhone(),
                        hashPassword(member.getPassword()), // Store hashed password
                        new java.sql.Date(System.currentTimeMillis()),
                        "active"
                    );
                }
            }
            System.err.println("Registration failed: No rows affected");
            return null;
        } catch (SQLException e) {
            System.err.println("SQLException in registerMember: " + e.getMessage());
            if (e.getErrorCode() == 1062) {
                throw new SQLException("Email already exists. Please use a different email.");
            }
            throw e;
        }
    }

    public Member getMemberByEmail(String email) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            System.err.println("Invalid email parameter: " + email);
            return null;
        }
        String sql = "SELECT * FROM Members WHERE LOWER(email) = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email.toLowerCase());
            System.out.println("Executing query: " + sql + " with email: " + email.toLowerCase());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Member member = new Member(
                    rs.getInt("member_id"),
                    rs.getString("surname"),
                    rs.getString("first_name"),
                    rs.getString("middle_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password"),
                    rs.getDate("join_date"),
                    rs.getString("status")
                );
                System.out.println("Retrieved member: " + member.getEmail() + ", Password: " + (member.getPassword() != null ? "set" : "null"));
                return member;
            }
            System.out.println("No member found for email: " + email.toLowerCase());
            return null;
        } catch (SQLException e) {
            System.err.println("SQLException in getMemberByEmail: " + e.getMessage());
            throw e;
        }
    }

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM Members";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Member member = new Member(
                    rs.getInt("member_id"),
                    rs.getString("surname"),
                    rs.getString("first_name"),
                    rs.getString("middle_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password"),
                    rs.getDate("join_date"),
                    rs.getString("status")
                );
                members.add(member);
            }
            System.out.println("Retrieved " + members.size() + " members");
        } catch (SQLException e) {
            System.err.println("SQLException in getAllMembers: " + e.getMessage());
            throw e;
        }
        return members;
    }

    private String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
