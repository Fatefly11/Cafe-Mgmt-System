package Entity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkSlot {
    private int slot_id;
    //private String cafe_role;
    private String slot_date;
    private String shift_time;
    //private Integer staff_id;
    private Integer cafe_owner_id;
    //private Integer cafe_manager_id;
    // Getter and Setter for slot_id
    public int getSlot_id() {
        return slot_id;
    }
    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }
    // Getter and Setter for slot_date
    public String getSlot_date() {
        return slot_date;
    }
    public void setSlot_date(String slot_date) {
        this.slot_date = slot_date;
    }
    // Getter and Setter for shift_time
    public String getShift_time() {
        return shift_time;
    }
    public void setShift_time(String shift_time) {
        this.shift_time = shift_time;
    }
    // Getter and Setter for cafe_owner_id
    public Integer getCafe_owner_id() {
        return cafe_owner_id;
    }
    public void setCafe_owner_id(Integer cafe_owner_id) {
        this.cafe_owner_id = cafe_owner_id;
    }
    public String createNewSlot(String slot_date, int cafe_owner_id, String shift_time){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "INSERT INTO workslot (slot_date, cafe_owner_id, shift_time) " +
                "VALUES (?, ?, ?)";

        try{
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters for the INSERT statement
            preparedStatement.setString(1, slot_date);
            preparedStatement.setInt(2, cafe_owner_id);
            preparedStatement.setString(3, shift_time);

            // Execute the INSERT statement
            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1){
                return "Workslot create successful.";
            } else {
                return "Failed to create the workslot.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create the workslot.";
        }
    }
    public List<WorkSlot> viewAllWorkSlots() {
        List<WorkSlot> workSlots = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM workslot";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                WorkSlot workSlot = new WorkSlot();
                workSlot.slot_id = resultSet.getInt("slot_id");
                workSlot.slot_date = resultSet.getString("slot_date");
                workSlot.cafe_owner_id = resultSet.getInt("cafe_owner_id");
                workSlot.shift_time = resultSet.getString("shift_time");
                workSlots.add(workSlot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return workSlots;
    }
    public WorkSlot viewOneSlot(int slot_id){
        WorkSlot workSlot = new WorkSlot();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM workslot WHERE slot_id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, slot_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                workSlot.slot_id = resultSet.getInt("slot_id");
                workSlot.slot_date = resultSet.getString("slot_date");
                workSlot.cafe_owner_id = resultSet.getInt("cafe_owner_id");
                workSlot.shift_time = resultSet.getString("shift_time");
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return workSlot;
    }
    public String updateOneSlot(int slot_id, String slot_date, String shift_time){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "UPDATE workslot SET";

        // Create a list to store parameters for the PreparedStatement
        List<Object> params = new ArrayList<>();

        if (slot_date != null) {
            query += " slot_date = ?,";
            params.add(slot_date);
        }

        if (shift_time != null) {
            query += " shift_time = ?,";
            params.add(shift_time);
        }

        // Remove the trailing comma and add the WHERE clause
        query = query.substring(0, query.length() - 1) + " WHERE slot_id = ?";
        params.add(slot_id);

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters for the UPDATE statement
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            // Execute the UPDATE statement
            int affectedRow = preparedStatement.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        }
        return "Update Work Slot Id: " + slot_id + " successful.";
    }
    public String deleteOneSlot(int slot_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "DELETE FROM workslot WHERE slot_id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, slot_id);

            // Execute the DELETE statement
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                return "Work Slot with id " + slot_id + " has been deleted.";
            } else {
                return "Work Slot with id " + slot_id + " was not found.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Work Slot deletion failed.";
        }
    }
    public List<WorkSlot> searchSlotBySubString(String substring){
        List<WorkSlot> workSlots = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM workslot " + "WHERE slot_id LIKE ? OR slot_date LIKE ? OR shift_time LIKE ?";

        try {
            // Establish a database connection
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters for the SELECT statement with wildcard characters (%)
            String searchParam = "%" + substring + "%";
            preparedStatement.setString(1, searchParam);
            preparedStatement.setString(2, searchParam);
            preparedStatement.setString(3, searchParam);

            // Execute the SELECT statement
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WorkSlot workSlot = new WorkSlot();
                workSlot.slot_id = resultSet.getInt("slot_id");
                workSlot.slot_date = resultSet.getString("slot_date");
                workSlot.cafe_owner_id = resultSet.getInt("cafe_owner_id");
                workSlot.shift_time = resultSet.getString("shift_time");
                workSlots.add(workSlot);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return workSlots;
        } catch (Exception e) {
            // Handle any database-related exceptions
            e.printStackTrace();
            return workSlots; // Return an empty list or handle the error as needed
        }
    }
}
