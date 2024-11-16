package carrentalsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ReturnCar {

    public static void main(String[] args) {
        // Create JFrame for return car window
        JFrame frame = new JFrame("Return Car");

        // Create labels and input fields for car return
        JLabel carIdLabel = new JLabel("Car ID:");
        JTextField carIdField = new JTextField();

        JButton returnButton = new JButton("Return Car");

        // Set bounds for components
        carIdLabel.setBounds(30, 30, 150, 25);
        carIdField.setBounds(180, 30, 150, 25);
        returnButton.setBounds(150, 80, 100, 30);

        // Add components to the frame
        frame.add(carIdLabel);
        frame.add(carIdField);
        frame.add(returnButton);

        frame.setLayout(null);
        frame.setSize(400, 200);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Action listener for return button
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int carId = Integer.parseInt(carIdField.getText());

                // Call the method to return the car
                returnCar(carId);
            }
        });
    }

    public static void returnCar(int carId) {
        Connection con = null;
        PreparedStatement psUpdateCar = null;

        try {
            // Establish connection to the database
            con = DriverManager.getConnection("jdbc:mysql://localhost/carrentalsystem", "root", "vaishu*123");

            // Update the availability of the car
            String updateCarQuery = "UPDATE cars SET availability = 'Available' WHERE id = ?";
            psUpdateCar = con.prepareStatement(updateCarQuery);
            psUpdateCar.setInt(1, carId);
            int rowsUpdated = psUpdateCar.executeUpdate();

            if (rowsUpdated > 0) {
                // Show success message
                JOptionPane.showMessageDialog(null, "Car returned successfully and availability updated to 'Available'!");
            } else {
                // Show error message if no rows were updated
                JOptionPane.showMessageDialog(null, "Car ID not found. Please check and try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error returning the car. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (psUpdateCar != null) psUpdateCar.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
