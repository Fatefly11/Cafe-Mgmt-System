package Entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;

import java.text.SimpleDateFormat;


public class Bid {
    private int bid_id;
    private int staff_id;
    private int slot_id;
    private String status;
    // Getter for bid_id
    public int getBid_id() {
        return bid_id;
    }
    // Setter for bid_id
    public void setBid_id(int bid_id) {
        this.bid_id = bid_id;
    }
    // Getter for staff_id
    public int getStaff_id() {
        return staff_id;
    }
    // Setter for staff_id
    public void setStaff_id(int staff_id) {
        this.staff_id = staff_id;
    }
    // Getter for slot_id
    public int getSlot_id() {
        return slot_id;
    }
    // Setter for slot_id
    public void setSlot_id(int slot_id) {
        this.slot_id = slot_id;
    }
    // Getter for status
    public String getStatus() {
        return status;
    }
    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }
    public List<Object> staffViewFinal(int staff_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT wa.slot_id, a.p_id, a.full_name, ws.slot_date, ws.shift_time " +
                "FROM workslot_assignment AS wa " +
                "INNER JOIN account AS a ON wa.staff_id = a.user_id " +
                "INNER JOIN workslot AS ws ON wa.slot_id = ws.slot_id " +
                "WHERE wa.staff_id = ?";
        List<Object> successfulRecords = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, staff_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                HashMap<String, Object> oneRecord = new HashMap<>();
                // Common fields for both bid and assignment
                oneRecord.put("profile_id", resultSet.getInt("p_id"));

                // Format the date to DD-MM-YY
                Date slotDate = resultSet.getDate("slot_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String formattedDate = dateFormat.format(slotDate);

                oneRecord.put("date", formattedDate);
                oneRecord.put("time", resultSet.getString("shift_time"));
                oneRecord.put("name", resultSet.getString("full_name"));
                successfulRecords.add(oneRecord);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        // Log the contents of the ArrayList
        for (Object item : successfulRecords) {
            System.out.println(item);
        }

        return successfulRecords;
    }
    public String createNewBid(int staff_id, int slot_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "INSERT INTO bid (staff_id, slot_id, status) " +
                "VALUES (?, ?, ?)";

        try{
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters for the INSERT statement
            preparedStatement.setInt(1, staff_id);
            preparedStatement.setInt(2, slot_id);
            preparedStatement.setString(3, "Pending");

            // Execute the INSERT statement
            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1){
                return "Bid create successful.";
            } else {
                return "Failed to create the bid.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create the bid.";
        }
    }
    public List<Object> viewPendingBidsByStaffId(int staff_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT b.bid_id, b.status, a.full_name, a.p_id, ws.slot_date, ws.shift_time " +
                "FROM bid AS b " +
                "INNER JOIN account AS a ON b.staff_id = a.user_id " +
                "INNER JOIN workslot AS ws ON b.slot_id = ws.slot_id " +
                "WHERE b.status = 'Pending' AND b.staff_id = ?";
        List<Object> bids = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, staff_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                HashMap<String, Object> oneBid = new HashMap<>();
                oneBid.put("bid_id", resultSet.getInt("bid_id"));
                oneBid.put("status", resultSet.getString("status"));
                // Format the date to DD-MM-YY
                Date slotDate = resultSet.getDate("slot_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String formattedDate = dateFormat.format(slotDate);

                oneBid.put("date", formattedDate);
                oneBid.put("time", resultSet.getString("shift_time"));
                oneBid.put("name", resultSet.getString("full_name"));
                oneBid.put("profile_id", resultSet.getInt("p_id"));
                bids.add(oneBid);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return bids;
    }
    public List<Object> viewSuccessBidsByStaffId(int staff_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT b.bid_id, b.status, a.full_name, a.p_id, ws.slot_date, ws.shift_time " +
                "FROM bid AS b " +
                "INNER JOIN account AS a ON b.staff_id = a.user_id " +
                "INNER JOIN workslot AS ws ON b.slot_id = ws.slot_id " +
                "WHERE b.status = 'Successful' AND b.staff_id = ?";
        List<Object> bids = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, staff_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                HashMap<String, Object> oneBid = new HashMap<>();
                oneBid.put("bid_id", resultSet.getInt("bid_id"));
                oneBid.put("status", resultSet.getString("status"));
                // Format the date to DD-MM-YY
                Date slotDate = resultSet.getDate("slot_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String formattedDate = dateFormat.format(slotDate);

                oneBid.put("date", formattedDate);
                oneBid.put("time", resultSet.getString("shift_time"));
                oneBid.put("name", resultSet.getString("full_name"));
                oneBid.put("profile_id", resultSet.getInt("p_id"));
                bids.add(oneBid);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return bids;
    }
    public List<Object> viewRejectBidsByStaffId(int staff_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT b.bid_id, b.status, a.full_name, a.p_id, ws.slot_date, ws.shift_time " +
                "FROM bid AS b " +
                "INNER JOIN account AS a ON b.staff_id = a.user_id " +
                "INNER JOIN workslot AS ws ON b.slot_id = ws.slot_id " +
                "WHERE b.status = 'Rejected' AND b.staff_id = ?";
        List<Object> bids = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, staff_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                HashMap<String, Object> oneBid = new HashMap<>();
                oneBid.put("bid_id", resultSet.getInt("bid_id"));
                oneBid.put("status", resultSet.getString("status"));
                // Format the date to DD-MM-YY
                Date slotDate = resultSet.getDate("slot_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String formattedDate = dateFormat.format(slotDate);

                oneBid.put("date", formattedDate);
                oneBid.put("time", resultSet.getString("shift_time"));
                oneBid.put("name", resultSet.getString("full_name"));
                oneBid.put("profile_id", resultSet.getInt("p_id"));
                bids.add(oneBid);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return bids;
    }
    public String updateOneBid(int bid_id, int staff_id, int slot_id, String status){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "UPDATE bid SET";

        // Create a list to store parameters for the PreparedStatement
        List<Object> params = new ArrayList<>();

        if (staff_id > 0) {
            query += " staff_id = ?,";
            params.add(staff_id);
        }

        if (slot_id > 0) {
            query += " slot_id = ?,";
            params.add(slot_id);
        }

        if (status != null) {
            query += " status = ?,";
            params.add(status);
        }

        // Remove the trailing comma and add the WHERE clause
        query = query.substring(0, query.length() - 1) + " WHERE bid_id = ?";
        params.add(bid_id);

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
        return "Update bid Id: " + bid_id + " successful.";
    }
    public String deleteOneBid(int bid_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "DELETE FROM bid WHERE bid_id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, bid_id);

            // Execute the DELETE statement
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                return "Bid with id " +bid_id + " has been deleted.";
            } else {
                return "Bid with id " +bid_id + " was not found.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Bid deletion failed.";
        }
    }
    public Object viewOneBid(int bid_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT b.bid_id, b.status, a.full_name, a.p_id, ws.slot_date, ws.shift_time, ws.slot_id " +
                "FROM bid AS b " +
                "INNER JOIN account AS a ON b.staff_id = a.user_id " +
                "INNER JOIN workslot AS ws ON b.slot_id = ws.slot_id " +
                "WHERE b.bid_id = ?";
        HashMap<String, Object> oneBid = new HashMap<>();
        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, bid_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                oneBid.put("bid_id", resultSet.getInt("bid_id"));
                oneBid.put("status", resultSet.getString("status"));
                // Format the date to DD-MM-YY
                Date slotDate = resultSet.getDate("slot_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String formattedDate = dateFormat.format(slotDate);

                oneBid.put("date", formattedDate);
                oneBid.put("time", resultSet.getString("shift_time"));
                oneBid.put("name", resultSet.getString("full_name"));
                oneBid.put("profile_id", resultSet.getInt("p_id"));
                oneBid.put("slot_id", resultSet.getInt("slot_id"));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return oneBid;
    }
    public List<Object> searchBidBySubString(int staff_id, String status, String substring){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT b.bid_id, b.status, a.full_name, a.p_id, ws.slot_date, ws.shift_time " +
                "FROM bid AS b " +
                "INNER JOIN account AS a ON b.staff_id = a.user_id " +
                "INNER JOIN workslot AS ws ON b.slot_id = ws.slot_id " +
                "INNER JOIN profile AS p ON a.p_id = p.profile_id " +
                "WHERE b.staff_id = ? AND b.status = ? AND (b.bid_id LIKE ? OR ws.slot_date LIKE ? OR ws.shift_time LIKE ? OR p.profile_name LIKE ?) ";
        List<Object> bids = new ArrayList<>();
        try {
            // Establish a database connection
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters for the SELECT statement with wildcard characters (%)
            String searchParam = "%" + substring + "%";
            preparedStatement.setInt(1, staff_id);
            preparedStatement.setString(2, status);
            preparedStatement.setString(3, searchParam);
            preparedStatement.setString(4, searchParam);
            preparedStatement.setString(5, searchParam);
            preparedStatement.setString(6, searchParam);


            // Execute the SELECT statement
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, Object> oneBid = new HashMap<>();
                oneBid.put("bid_id", resultSet.getInt("bid_id"));
                oneBid.put("status", resultSet.getString("status"));
                // Format the date to DD-MM-YY
                Date slotDate = resultSet.getDate("slot_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String formattedDate = dateFormat.format(slotDate);

                oneBid.put("date", formattedDate);
                oneBid.put("time", resultSet.getString("shift_time"));
                oneBid.put("name", resultSet.getString("full_name"));
                oneBid.put("profile_id", resultSet.getInt("p_id"));
                bids.add(oneBid);
            }
        } catch (Exception e) {
            // Handle any database-related exceptions
            e.printStackTrace();
        }
        return bids;
    }
    public List<Object> viewAllBids(){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT b.bid_id, b.status, a.full_name, a.p_id, ws.slot_date, ws.shift_time, ws.slot_id " +
                "FROM bid AS b " +
                "INNER JOIN account AS a ON b.staff_id = a.user_id " +
                "INNER JOIN workslot AS ws ON b.slot_id = ws.slot_id ";
        List<Object> bids = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                HashMap<String, Object> oneBid = new HashMap<>();
                oneBid.put("bid_id", resultSet.getInt("bid_id"));
                oneBid.put("status", resultSet.getString("status"));
                // Format the date to DD-MM-YY
                Date slotDate = resultSet.getDate("slot_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String formattedDate = dateFormat.format(slotDate);

                oneBid.put("date", formattedDate);
                oneBid.put("time", resultSet.getString("shift_time"));
                oneBid.put("name", resultSet.getString("full_name"));
                oneBid.put("profile_id", resultSet.getInt("p_id"));
                oneBid.put("slot_id", resultSet.getInt("slot_id"));
                bids.add(oneBid);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return bids;
    }
    public String approveOneBid(int bid_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "UPDATE bid SET status = 'Successful' WHERE bid_id = ?";
        String query2 = "INSERT INTO workslot_assignment (slot_id, staff_id, assignment_date) VALUES (?, ?, ?)";
        String query3 = "SELECT slot_id, staff_id FROM bid WHERE bid_id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement updateStatement = connection.prepareStatement(query);
            PreparedStatement insertStatement = connection.prepareStatement(query2);
            PreparedStatement selectStatement = connection.prepareStatement(query3);

            updateStatement.setInt(1, bid_id);
            // Execute the UPDATE statement
            int affectedRows = updateStatement.executeUpdate();
            if (affectedRows == 1) {
                // Retrieve slot_id and staff_id from the bid table
                selectStatement.setInt(1, bid_id);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    int slot_id = resultSet.getInt("slot_id");
                    int staff_id = resultSet.getInt("staff_id");

                    // Set parameters for the INSERT statement into workslot_assignment
                    insertStatement.setInt(1, slot_id);
                    insertStatement.setInt(2, staff_id);

                    // Get the current date and set it as assignment_date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String currentDate = dateFormat.format(new Date());
                    insertStatement.setString(3, currentDate);

                    // Execute the INSERT statement
                    int rowsInserted = insertStatement.executeUpdate();

                    if (rowsInserted == 1) {
                        return "Bid with bid_id " + bid_id + " has been approved, and a record has been added to workslot_assignment.";
                    } else {
                        return "Failed to add a record to workslot_assignment for bid with bid_id " + bid_id + ".";
                    }
                } else {
                    return "Failed to retrieve slot_id and staff_id for bid with bid_id " + bid_id + ".";
                }
            } else {
                return "Failed to approve bid with bid_id " + bid_id + ". Bid may not exist or has already been approved.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to approve bid with bid_id " + bid_id + ". Error: " + e.getMessage();
        }

    }
    public String rejectOneBid(int bid_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "UPDATE bid SET status = 'Rejected' WHERE bid_id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement updateStatement = connection.prepareStatement(query);

            updateStatement.setInt(1, bid_id);
            // Execute the UPDATE statement
            int affectedRows = updateStatement.executeUpdate();

            if (affectedRows == 1){
                return "Reject bid Id: " + bid_id + " successful.";
            } else {
                return "Reject bid Id: " + bid_id + " failed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to reject bid with bid_id " + bid_id + ". Error: " + e.getMessage();
        }

    }
}
