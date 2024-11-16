package carrentalsystem;

import javax.swing.*;
import java.sql.*;

public class LoginCustomer {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Customer Login");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        // Set layout and add components
        frame.setLayout(null);
        emailLabel.setBounds(30, 30, 100, 30);
        passwordLabel.setBounds(30, 70, 100, 30);
        emailField.setBounds(140, 30, 150, 30);
        passwordField.setBounds(140, 70, 150, 30);
        loginButton.setBounds(140, 110, 150, 30);

        frame.add(emailLabel);
        frame.add(passwordLabel);
        frame.add(emailField);
        frame.add(passwordField);
        frame.add(loginButton);

        frame.setSize(400, 300);
        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost/car_rental_system", "root", "password");
                String query = "SELECT * FROM customers WHERE email = ? AND password = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
