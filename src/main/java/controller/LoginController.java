package controller;

import dao.AdminDAO;
import dao.MemberDAO;
import model.Admin;
import model.Member;
import view.AdminDashboard;
import view.MemberDashboard;
import view.LoginFrame;
import javax.swing.*;
import java.sql.SQLException;

public class LoginController {
    private LoginFrame loginFrame;
    private AdminDAO adminDAO;
    private MemberDAO memberDAO;

    public LoginController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.adminDAO = new AdminDAO();
        this.memberDAO = new MemberDAO();
    }

    public void handleLogin() {
        String emailOrUsername = loginFrame.getEmailField().getText().trim().toLowerCase();
        String password = new String(loginFrame.getPasswordField().getPassword()).trim();

        System.out.println("Login attempt for: " + emailOrUsername);

        if (emailOrUsername.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame, "Please enter both email/username and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Admin admin = adminDAO.getAdminByUsername(emailOrUsername);
            if (admin != null) {
                if (admin.getPassword() == null) {
                    System.err.println("Admin password is null for username: " + emailOrUsername);
                    JOptionPane.showMessageDialog(loginFrame, "Admin account has no password set", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String hashedInput = hashPassword(password);
                System.out.println("Admin login - Input hash: " + hashedInput + ", DB hash: " + admin.getPassword());
                if (admin.getPassword().equals(hashedInput)) {
                    loginFrame.dispose();
                    new AdminDashboard().setVisible(true);
                    return;
                }
            }

            Member member = memberDAO.getMemberByEmail(emailOrUsername);
            if (member != null) {
                if (member.getPassword() == null) {
                    System.err.println("Member password is null for email: " + emailOrUsername);
                    JOptionPane.showMessageDialog(loginFrame, "Member account has no password set", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String hashedInput = hashPassword(password);
                System.out.println("Member login - Input hash: " + hashedInput + ", DB hash: " + member.getPassword());
                if (member.getPassword().equals(hashedInput)) {
                    loginFrame.dispose();
                    new MemberDashboard(member).setVisible(true);
                    return;
                }
            } else {
                System.out.println("No member or admin found for: " + emailOrUsername);
            }

            JOptionPane.showMessageDialog(loginFrame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
            JOptionPane.showMessageDialog(loginFrame, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleRegistration(Member member) {
        try {
            Member registeredMember = memberDAO.registerMember(member);
            if (registeredMember != null) {
                JOptionPane.showMessageDialog(loginFrame, "Registration successful! Logging in automatically.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loginFrame.dispose();
                new MemberDashboard(registeredMember).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            JOptionPane.showMessageDialog(loginFrame, "Registration error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid registration input: " + e.getMessage());
            JOptionPane.showMessageDialog(loginFrame, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
