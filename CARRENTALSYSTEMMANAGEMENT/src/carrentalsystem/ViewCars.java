package carrentalsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewCars {
    public static void main(String[] args) {
        // Create JFrame for displaying available cars
        JFrame frame = new JFrame("Available Cars");

        // Create a DefaultTableModel to hold car details
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Car ID");
        model.addColumn("Car Name");
        model.addColumn("Model");
        model.addColumn("Availability");

        // Create a JTable to display car details
        JTable carsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(carsTable);

        // Set layout and add components
        frame.setLayout(null);
        scrollPane.setBounds(30, 30, 340, 200);
        frame.add(scrollPane);

        frame.setSize(500, 300);
        frame.setVisible(true);

        try {
            // Establish database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/carrentalsystem", "root", "vaishu*123");
            
            // Query to select car details including availability status
            String query = "SELECT id, car_name, model, availability FROM cars";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            // Iterate through the result set and add rows to the table model
            while (rs.next()) {
                String status = rs.getString("availability");  // Use getString instead of getBoolean
                model.addRow(new Object[] { rs.getInt("id"), rs.getString("car_name"), rs.getString("model"), status });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
