package carrentalsystem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookCar {

    public static void main(String[] args) {
        // Create JFrame for booking window
        JFrame frame = new JFrame("Car Booking");

        // Create labels and input fields for customer details and booking
        JLabel nameLabel = new JLabel("Customer Name:");
        JTextField nameField = new JTextField();

        JLabel carIdLabel = new JLabel("Car ID:");
        JTextField carIdField = new JTextField();

        JLabel dateLabel = new JLabel("Booking Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();

        JLabel returnDateLabel = new JLabel("Return Date (YYYY-MM-DD):");
        JTextField returnDateField = new JTextField();

        JButton bookButton = new JButton("Book Car");

        // Set bounds for components
        nameLabel.setBounds(30, 30, 150, 25);
        nameField.setBounds(180, 30, 150, 25);

        carIdLabel.setBounds(30, 70, 150, 25);
        carIdField.setBounds(180, 70, 150, 25);

        dateLabel.setBounds(30, 110, 180, 25);
        dateField.setBounds(210, 110, 150, 25);

        returnDateLabel.setBounds(30, 150, 200, 25);
        returnDateField.setBounds(210, 150, 150, 25);

        bookButton.setBounds(150, 200, 100, 30);

        // Add components to the frame
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(carIdLabel);
        frame.add(carIdField);
        frame.add(dateLabel);
        frame.add(dateField);
        frame.add(returnDateLabel);
        frame.add(returnDateField);
        frame.add(bookButton);

        frame.setLayout(null);
        frame.setSize(450, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Action listener for book button
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customerName = nameField.getText();
                int carId = Integer.parseInt(carIdField.getText());
                String bookingDate = dateField.getText();
                String returnDate = returnDateField.getText();

                // Validate inputs
                if (customerName.isEmpty() || carIdField.getText().isEmpty() || bookingDate.isEmpty() || returnDate.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call the method to book the car and calculate rent
                bookCar(customerName, carId, bookingDate, returnDate);
            }
        });
    }

    public static void bookCar(String customerName, int carId, String bookingDate, String returnDate) {
        Connection con = null;
        PreparedStatement psBooking = null;
        PreparedStatement psUpdateCar = null;
        PreparedStatement psGetRate = null;

        try {
            // Establish connection to the database
            con = DriverManager.getConnection("jdbc:mysql://localhost/carrentalsystem", "root", "vaishu*123");

            // Calculate rent based on rental rate and booking duration
            String rateQuery = "SELECT rental_rate FROM cars WHERE id = ?";
            psGetRate = con.prepareStatement(rateQuery);
            psGetRate.setInt(1, carId);
            ResultSet rsRate = psGetRate.executeQuery();

            if (rsRate.next()) {
                double rentalRate = rsRate.getDouble("rental_rate");

                // Calculate the number of days between booking and return date
                LocalDate startDate = LocalDate.parse(bookingDate);
                LocalDate endDate = LocalDate.parse(returnDate);
                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

                // Calculate total rent
                double totalRent = daysBetween * rentalRate;

                // Insert booking details into bookings table
                String bookingQuery = "INSERT INTO booking(customer_name, car_id, booking_date, return_date, total_rent) VALUES (?, ?, ?, ?, ?)";
                psBooking = con.prepareStatement(bookingQuery);
                psBooking.setString(1, customerName);
                psBooking.setInt(2, carId);
                psBooking.setString(3, bookingDate);
                psBooking.setString(4, returnDate);
                psBooking.setDouble(5, totalRent);
                psBooking.executeUpdate();

                // Update the availability of the car
                String updateCarQuery = "UPDATE cars SET availability = 'Booked' WHERE id = ?";
                psUpdateCar = con.prepareStatement(updateCarQuery);
                psUpdateCar.setInt(1, carId);
                psUpdateCar.executeUpdate();

                // Show success message
                JOptionPane.showMessageDialog(null, "Car booked successfully! Total Rent: " + totalRent);
            } else {
                JOptionPane.showMessageDialog(null, "Car not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error booking the car. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (psBooking != null) psBooking.close();
                if (psUpdateCar != null) psUpdateCar.close();
                if (psGetRate != null) psGetRate.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
