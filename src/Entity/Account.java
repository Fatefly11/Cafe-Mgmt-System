package Entity;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.mindrot.jbcrypt.BCrypt;

public class Account {
    private int user_id;
    private String username;
    private String full_name;
    private String password;
    private int p_id;
    private int max_slot;

    // Getter for user_id
    public int getUser_id() {
        return user_id;
    }
    // Setter for user_id
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    // Getter for username
    public String getUsername() {
        return username;
    }
    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }
    // Getter for full_name
    public String getFull_name() {
        return full_name;
    }
    // Setter for full_name
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    // Getter for password
    public String getPassword() {
        return password;
    }
    // Setter for password
    public void setPassword(String password) {
        this.password = password;
    }
    // Getter for p_id
    public int getP_id() {
        return p_id;
    }
    // Setter for p_id
    public void setP_id(int p_id) {
        this.p_id = p_id;
    }
    // Getter for max_slot
    public int getMax_slot() {
        return max_slot;
    }
    // Setter for max_slot
    public void setMax_slot(int max_slot) {
        this.max_slot = max_slot;
    }

    public String createNewAccount(String username, String full_name, String password, int p_id, int max_slot) {
        if (Objects.equals(username, "") || Objects.equals(full_name, "") || Objects.equals(password, "")) {
            return "Failed to create the account.";
        }

        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "INSERT INTO account (username, full_name, password, p_id, max_slot) " +
                "VALUES (?, ?, ?, ?, ?)";

        try{
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Hash the raw password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Set parameters for the INSERT statement
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, full_name);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.setInt(4, p_id);
            preparedStatement.setInt(5, max_slot);

            // Execute the INSERT statement
            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1){
                return "Account create successful.";
            } else {
                return "Failed to create the account.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create the account.";
        }
    }
    public List<Account> viewAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM account";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Account account = new Account();
                account.user_id = resultSet.getInt("user_id");
                account.username = resultSet.getString("username");
                account.full_name = resultSet.getString("full_name");
                account.password = resultSet.getString("password");
                account.p_id = resultSet.getInt("p_id");
                account.max_slot = resultSet.getInt("max_slot");
                accounts.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accounts;
    }
    public String updateOneAccount(int user_id, String username, String full_name, String password, int p_id, int max_slot){
        if (Objects.equals(username, "") || Objects.equals(full_name, "")) {
            return "Update account Id: " + user_id + " failed.";
        }
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "UPDATE account SET";

        // Create a list to store parameters for the PreparedStatement
        List<Object> params = new ArrayList<>();

        query += " username = ?,";
        params.add(username);

        query += " full_name = ?,";
        params.add(full_name);

        if (password != null) {
            // Hash the raw password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            query += " password = ?,";
            params.add(hashedPassword);
        }

        if (p_id > 0) {
            query += " p_id = ?,";
            params.add(p_id);
        }

        if (max_slot > 0) {
            query += " max_slot = ?,";
            params.add(max_slot);
        }

        // Remove the trailing comma and add the WHERE clause
        query = query.substring(0, query.length() - 1) + " WHERE user_id = ?";
        params.add(user_id);

//        System.out.println("Final SQL query: " + query);
//        System.out.println("Parameters: " + params);

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set parameters for the UPDATE statement
            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            // Execute the UPDATE statement
            int affectedRow = preparedStatement.executeUpdate();

            if (affectedRow == 1) {
                return "Update account Id: " + user_id + " successful.";
            } else {
                return "Update account Id: " + user_id + " failed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Update account Id: " + user_id + " failed.";
        }
    }
    public String suspendOneAccount(int user_id){
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "DELETE FROM account WHERE user_id = ?";
        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, user_id);

            // Execute the DELETE statement
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                return "Account with id " + user_id + " has been deleted.";
            } else {
                return "Account with id " + user_id + " was not found.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Account with id " + user_id + " was not found.";
        }
    }
    public List<Account> searchAccountBySubString(String substring){
        List<Account> accounts = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM account " + "WHERE user_id LIKE ? OR username LIKE ? OR full_name LIKE ? OR p_id LIKE ? OR max_slot LIKE ?";

        try {
            // Establish a database connection
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set the parameters for the SELECT statement with wildcard characters (%)
            String searchParam = "%" + substring + "%";
            preparedStatement.setString(1, searchParam);
            preparedStatement.setString(2, searchParam);
            preparedStatement.setString(3, searchParam);
            preparedStatement.setString(4, searchParam);
            preparedStatement.setString(5, searchParam);

            // Execute the SELECT statement
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Account account = new Account();
                account.user_id = resultSet.getInt("user_id");
                account.username = resultSet.getString("username");
                account.full_name = resultSet.getString("full_name");
                account.password = resultSet.getString("password");
                account.p_id = resultSet.getInt("p_id");
                account.max_slot = resultSet.getInt("max_slot");
                accounts.add(account);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return accounts;
        } catch (Exception e) {
            // Handle any database-related exceptions
            e.printStackTrace();
            return accounts; // Return an empty list or handle the error as needed
        }
    }
    public int getUserIdByUsername(String username){
        int user_id = -1;
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT user_id FROM account WHERE username = ?";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user_id = resultSet.getInt("user_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user_id;
    }
    public Account viewOneAccount(int user_id){
        Account account = new Account();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM account WHERE user_id = ?";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                account.user_id = resultSet.getInt("user_id");
                account.username = resultSet.getString("username");
                account.full_name = resultSet.getString("full_name");
                account.password = resultSet.getString("password");
                account.p_id = resultSet.getInt("p_id");
                account.max_slot = resultSet.getInt("max_slot");
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return account;
    }
    public List<Account> viewAllStaff(){
        List<Account> accounts = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT * FROM account WHERE p_id IN (4, 7, 5)";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Account account = new Account();
                account.user_id = resultSet.getInt("user_id");
                account.username = resultSet.getString("username");
                account.full_name = resultSet.getString("full_name");
                account.password = resultSet.getString("password");
                account.p_id = resultSet.getInt("p_id");
                account.max_slot = resultSet.getInt("max_slot");
                accounts.add(account);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return accounts;
    }
    public List<Object> viewStaffByRole(int p_id){
        List<Object> accounts = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT a.user_id, a.username, a.full_name, a.password, a.p_id, a.max_slot, COUNT(wa.assignment_id) AS occupied_slot " +
                "FROM account a " +
                "LEFT JOIN workslot_assignment wa ON a.user_id = wa.staff_id " +
                "WHERE a.p_id = ? " +
                "GROUP BY a.user_id, a.username, a.full_name, a.password, a.p_id, a.max_slot";

        try {
            Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, p_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<Object, Object> account = new HashMap<>();
                account.put("user_id", resultSet.getInt("user_id"));
                account.put("username", resultSet.getString("username"));
                account.put("full_name", resultSet.getString("full_name"));
                account.put("password", resultSet.getString("password"));
                account.put("p_id", resultSet.getInt("p_id"));
                account.put("max_slot", resultSet.getInt("max_slot"));
                account.put("occupied_slot", resultSet.getInt("occupied_slot"));
                accounts.add(account);
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accounts;
    }
    public String adminLogin(String enteredUsername, String enteredPassword, int enteredPId) {
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT password, p_id FROM account WHERE username = ? AND p_id = 3";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, enteredUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the hashed password and p_id from the database
                String hashedPasswordFromDB = resultSet.getString("password");
                int pIdFromDB = resultSet.getInt("p_id");

                // Check if the entered password matches the hashed password
                if (BCrypt.checkpw(enteredPassword, hashedPasswordFromDB)) {
                    // Check if the entered p_id matches the expected p_id
                    if (enteredPId == pIdFromDB) {
                        return "Admin Login successful.";
                    } else {
                        return "Login failed.";
                    }
                } else {
                    return "Login failed.";
                }
            } else {
                return "Login failed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Login failed. Error: " + e.getMessage();
        }
    }
    public String adminLogout(){
        return "Admin successfully logged out!";
    }
    public String ownerLogin(String enteredUsername, String enteredPassword, int enteredPId) {
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT password, p_id FROM account WHERE username = ? AND p_id = 1";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, enteredUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the hashed password and p_id from the database
                String hashedPasswordFromDB = resultSet.getString("password");
                int pIdFromDB = resultSet.getInt("p_id");

                // Check if the entered password matches the hashed password
                if (BCrypt.checkpw(enteredPassword, hashedPasswordFromDB)) {
                    // Check if the entered p_id matches the expected p_id
                    if (enteredPId == pIdFromDB) {
                        return "Cafe Owner Login successful.";
                    } else {
                        return "Login failed.";
                    }
                } else {
                    return "Login failed.";
                }
            } else {
                return "Login failed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Login failed. Error: " + e.getMessage();
        }
    }
    public String ownerLogout(){
        return "Owner successfully logged out!";
    }
    public String managerLogin(String enteredUsername, String enteredPassword, int enteredPId) {
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT password, p_id FROM account WHERE username = ? AND p_id = 2";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, enteredUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the hashed password and p_id from the database
                String hashedPasswordFromDB = resultSet.getString("password");
                int pIdFromDB = resultSet.getInt("p_id");

                // Check if the entered password matches the hashed password
                if (BCrypt.checkpw(enteredPassword, hashedPasswordFromDB)) {
                    // Check if the entered p_id matches the expected p_id
                    if (enteredPId == pIdFromDB) {
                        return "Manager Login successful.";
                    } else {
                        return "Login failed.";
                    }
                } else {
                    return "Login failed.";
                }
            } else {
                return "Login failed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Login failed. Error: " + e.getMessage();
        }
    }
    public String managerLogout(){
        return "Manager successfully logged out!";
    }
    public String staffLogin(String enteredUsername, String enteredPassword) {
        String url = "jdbc:mysql://localhost:3306/csit314";
        String dbUsername = "csit314";
        String dbPassword = "admin";
        String query = "SELECT password FROM account WHERE username = ? AND p_id IN (4, 5, 7)";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, enteredUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the hashed password and p_id from the database
                String hashedPasswordFromDB = resultSet.getString("password");

                // Check if the entered password matches the hashed password
                if (BCrypt.checkpw(enteredPassword, hashedPasswordFromDB)) {
                    // Check if the entered p_id matches the expected p_id
                    return "Staff Login successful.";
                } else {
                    return "Login failed.";
                }
            } else {
                return "Login failed.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Login failed. Error: " + e.getMessage();
        }
    }
    public String staffLogout(){
        return "Staff successfully logged out!";
    }
}
