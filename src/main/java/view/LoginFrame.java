package view;

import controller.LoginController;
import model.Member;
import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private LoginController controller;

    public LoginFrame() {
        setTitle("Cooperative Society Login");
        setSize(400, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon(getClass().getResource("/resources/logo.png")).getImage());

        initComponents();
    }

    public void setController(LoginController controller) {
        this.controller = controller;
        initListeners();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel logoLabel = new JLabel();
        try {
            java.net.URL logoUrl = getClass().getResource("/resources/logo.png");
            if (logoUrl != null) {
                ImageIcon logoIcon = new ImageIcon(logoUrl);
                Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                logoLabel.setText("Cooperative Society");
                logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            }
            logoLabel.setHorizontalAlignment(JLabel.CENTER);
        } catch (Exception e) {
            logoLabel.setText("Cooperative Society");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 16));
            System.err.println("Error loading logo: " + e.getMessage());
        }
        mainPanel.add(logoLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.add(new JLabel("Email/Username:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        loginButton = new JButton("Login");
        formPanel.add(new JLabel());
        formPanel.add(loginButton);

        registerButton = new JButton("Register");
        formPanel.add(new JLabel());
        formPanel.add(registerButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void initListeners() {
        if (controller != null) {
            loginButton.addActionListener(e -> controller.handleLogin());
            registerButton.addActionListener(e -> showRegistrationDialog());
        }
    }

    public void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "Register New Member", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField surnameField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        formPanel.add(new JLabel("Surname:"));
        formPanel.add(surnameField);
        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Middle Name (optional):"));
        formPanel.add(middleNameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        JButton submitButton = new JButton("Submit");
        formPanel.add(new JLabel());
        formPanel.add(submitButton);

        submitButton.addActionListener(e -> {
            String surname = surnameField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String middleName = middleNameField.getText().trim().isEmpty() ? null : middleNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (surname.isEmpty() || firstName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill all required fields (Surname, First Name, Email, Phone)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length() < 8) {
                JOptionPane.showMessageDialog(dialog, "Password must be at least 8 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(dialog, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Member member = new Member(surname, firstName, middleName, email, phone, password, new Date(), "active");
            controller.handleRegistration(member);
            dialog.dispose();
        });

        dialog.setVisible(true);
    }

    public JButton getLoginButton() { return loginButton; }
    public JTextField getEmailField() { return emailField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JButton getRegisterButton() { return registerButton; }
}


