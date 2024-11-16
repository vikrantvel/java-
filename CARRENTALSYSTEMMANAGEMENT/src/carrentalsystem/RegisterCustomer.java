package carrentalsystem;

import javax.swing.*;
import java.sql.*;

public class RegisterCustomer {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Customer Registration");
        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel passwordLabel = new JLabel("Password:");
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        // Set layout and add components
        frame.setLayout(null);
        nameLabel.setBounds(30, 30, 100, 30);
        emailLabel.setBounds(30, 70, 100, 30);
        passwordLabel.setBounds(30, 110, 100, 30);
        nameField.setBounds(140, 30, 150, 30);
        emailField.setBounds(140, 70, 150, 30);
        passwordField.setBounds(140, 110, 150, 30);
        registerButton.setBounds(140, 150, 150, 30);
        
        frame.add(nameLabel);
        frame.add(emailLabel);
        frame.add(passwordLabel);
        frame.add(nameField);
        frame.add(emailField);
        frame.add(passwordField);
        frame.add(registerButton);

        frame.setSize(400, 300);
        frame.setVisible(true);

        registerButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost/carrentalsystem", "root", "vaishu*123");
                String query = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registration successful!");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }
}
