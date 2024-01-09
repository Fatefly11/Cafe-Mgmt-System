package Entity;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Assignment {
    private int assignment_id;
    private int slot_id;
    private int staff_id;
    private String assignment_date;
    // Getter and Setter for assignment_id
    public int getAssignment_id() {
        return assignment_id;
    }
    public void setAssignment_id(int assignment_id) {
        this.assignment_id = assignment_id;
    }
    // Getter and Setter for workslot_id
    public int getSlot_id() {
        return slot_id;
    }
    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }
    // Getter and Setter for staff_id
    public int getStaff_id() {
        return staff_id;
    }
    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }
    // Getter and Setter for assignment_date
    public String getAssignment_date() {
        return assignment_date;
    }
    public void setAssignment_date(String assignment_date) {
        this.assignment_date = assignment_date;
    }
    public String fillASlot(int workslot_id, int staff_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "INSERT INTO workslot_assignment (slot_id, staff_id, assignment_date) " +
                "VALUES (?, ?, ?)";

        try{
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters for the INSERT statement
            preparedStatement.setInt(1, workslot_id);
            preparedStatement.setInt(2, staff_id);
            // Get the current date and set it as assignment_date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());
            preparedStatement.setString(3, currentDate);

            // Execute the INSERT statement
            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1){
                return "Assign slot successful.";
            } else {
                return "Failed to assign the slot.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to assign the slot.";
        }
    }
    public String unfillASlot(int assignment_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query1 = "SELECT b.bid_id FROM workslot_assignment AS wa " +
                "INNER JOIN bid AS b ON wa.slot_id = b.slot_id AND wa.staff_id = b.staff_id " +
                "WHERE wa.assignment_id = ?";
        String query2 = "UPDATE bid SET status = 'Rejected' WHERE bid_id = ?";
        String query3 = "DELETE FROM workslot_assignment WHERE assignment_id = ?";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            // Retrieve bid_id using a JOIN operation
            int bidId = 0; // Assuming 0 is not a valid bid_id
            try (PreparedStatement preparedStatementSelectBidInfo = connection.prepareStatement(query1)) {
                preparedStatementSelectBidInfo.setInt(1, assignment_id);
                ResultSet resultSet = preparedStatementSelectBidInfo.executeQuery();
                if (resultSet.next()) {
                    bidId = resultSet.getInt("bid_id");
                }
            }

            // If a bid exists, update its status to 'Rejected'
            if (bidId > 0) {
                try (PreparedStatement preparedStatementUpdateBidStatus = connection.prepareStatement(query2)) {
                    preparedStatementUpdateBidStatus.setInt(1, bidId);
                    preparedStatementUpdateBidStatus.executeUpdate();
                }
            }

            // Delete the assignment
            try (PreparedStatement preparedStatementDeleteAssignment = connection.prepareStatement(query3)) {
                preparedStatementDeleteAssignment.setInt(1, assignment_id);
                int affectedRow = preparedStatementDeleteAssignment.executeUpdate();

                if (affectedRow == 1){
                    return "Unfill a slot successful.";
                } else {
                    return "Failed to unfill the slot.";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to unfill the slot.";
        }
    }
    public List<Object> viewAssignmentByWorkSlot(int slot_id){
        List<Object> assignments = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT wa.*, a.p_id, a.full_name " +
                "FROM workslot_assignment wa " +
                "JOIN account a ON wa.staff_id = a.user_id " +
                "WHERE wa.slot_id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, slot_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<Object, Object> assignment= new HashMap<>();
                assignment.put("assignment_id", resultSet.getInt("assignment_id"));
                assignment.put("slot_id", resultSet.getInt("slot_id"));
                assignment.put("full_name", resultSet.getString("full_name"));
                assignment.put("p_id", resultSet.getInt("p_id"));
                assignments.add(assignment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assignments;
    }
}

