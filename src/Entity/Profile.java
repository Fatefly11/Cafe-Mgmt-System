package Entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Profile {
    private int profile_id;
    private String profile_name;
    private String profile_desc;
    // Getter for profile_id
    public int getProfile_id() {
        return profile_id;
    }
    // Setter for profile_id
    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }
    // Getter for profile_name
    public String getProfile_name() {
        return profile_name;
    }
    // Setter for profile_name
    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }
    // Getter for profile_desc
    public String getProfile_desc() {
        return profile_desc;
    }
    // Setter for profile_desc
    public void setProfile_desc(String profile_desc) {
        this.profile_desc = profile_desc;
    }

    //User story functions
    public String createNewProfile(String profile_name, String profile_desc) {
        if (Objects.equals(profile_name, "") || Objects.equals(profile_desc, "")) {
            return "Failed to create the profile.";
        }
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "INSERT INTO profile (profile_name, profile_desc) " +
                "VALUES (?, ?)";

        try{
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters for the INSERT statement
            preparedStatement.setString(1, profile_name);
            preparedStatement.setString(2, profile_desc);

            // Execute the INSERT statement
            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1){
                return "Profile create successful.";
            } else {
                return "Failed to create the profile.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create the profile.";
        }
    }
    public List<Profile> viewAllProfiles() {
        List<Profile> profiles = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM profile";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Profile profile = new Profile();
                profile.setProfile_id(resultSet.getInt("profile_id"));
                profile.setProfile_name(resultSet.getString("profile_name"));
                profile.setProfile_desc(resultSet.getString("profile_desc"));
                profiles.add(profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return profiles;
    }
    public Profile viewOneProfile(int profile_id){
        Profile profile = new Profile();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM profile WHERE profile_id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, profile_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                profile.setProfile_id(resultSet.getInt("profile_id"));
                profile.setProfile_name(resultSet.getString("profile_name"));
                profile.setProfile_desc(resultSet.getString("profile_desc"));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return profile;
    }
    public String updateOneProfile(int profile_id, String profile_name, String profile_desc){
        if (Objects.equals(profile_name, "") || Objects.equals(profile_desc, "")) {
            return "Update profile id: " + profile_id + " failed.";
        }
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "UPDATE profile SET profile_name=?, profile_desc=? WHERE profile_id=?";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, profile_name);
            preparedStatement.setString(2, profile_desc);
            preparedStatement.setInt(3, profile_id);

            // Execute the UPDATE statement
            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1){
                return "Update profile Id: " + profile_id + " successful.";
            } else {
                return "Update profile Id: " + profile_id + " failed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Update profile Id: " + profile_id + " failed.";
        }
    }
    public String deleteOneProfile(int profile_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "DELETE FROM profile WHERE profile_id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, profile_id);

            // Execute the DELETE statement
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                return "Profile with id " + profile_id + " has been deleted.";
            } else {
                return "Profile with id " + profile_id + " was not found.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Profile deletion failed.";
        }
    }
    public List<Profile> searchProfileBySubString(String substring){
        List<Profile> profiles = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM profile " + "WHERE profile_name LIKE ? OR profile_desc LIKE ? OR profile_id LIKE ?";

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
                Profile profile = new Profile();
                profile.setProfile_id(resultSet.getInt("profile_id"));
                profile.setProfile_name(resultSet.getString("profile_name"));
                profile.setProfile_desc(resultSet.getString("profile_desc"));
                profiles.add(profile);
            }
            return profiles;
        } catch (Exception e) {
            // Handle any database-related exceptions
            e.printStackTrace();
            return profiles; // Return an empty list or handle the error as needed
        }
    }
}
