package Persistance;

import Business.Entities.User;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSQLDAO implements UserDAO{

    public boolean registerUser(User user) throws PersistanceException {
        if (user == null || user.getUsername() == null || user.getMail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User and its fields must not be null");
        }

        String query = "INSERT INTO user (user_name, email, password, balance, cryptoDeleted) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Could not reach DB!");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getMail());
            stmt.setString(3, user.getPassword());
            stmt.setDouble(4, user.getBalance());
            stmt.setBoolean(5, false);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to create user");
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing statement: " + e.getMessage());
            }
            // Note: Don't close conn here since it's managed by SQLConnector
        }
    }

    public User getUserByUsernameOrEmail(String value) throws PersistanceException {
        String query = "SELECT * FROM user WHERE user_name = ? OR email = ?;";
        User user = null;

        try {
            PreparedStatement preparedStatement = SQLConnector.getInstance().getConnection().prepareStatement(query);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, value);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String user_name = result.getString("user_name");
                String email = result.getString("email");
                String password = result.getString("password");
                Double balance = result.getDouble("balance");
                Boolean cryptoDeleted = result.getBoolean("cryptoDeleted");
                user = new User(user_name, password, email, balance, cryptoDeleted); // Adjust constructor parameters if needed
            } else {
                // Throw custom exception if no data is found
                throw new DBDataNotFound("No user found with username or email: " + value);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // It's often better to handle logging or further exception handling here
        }

        return user;
    }

    //TODO: ESTA FUNCIÓN PARA QUÉ??
    public boolean validateAdmin(String password) throws PersistanceException {
        String query = "SELECT * FROM user WHERE role = ? AND password = ?";

        try (PreparedStatement stmt = SQLConnector.getInstance().getConnection().prepareStatement(query)) {
            stmt.setString(1, "admin");
            stmt.setString(2, password);
            ResultSet result = stmt.executeQuery();
            return result.next();
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Error accessing the database" + e.getMessage());
        }
    }


    public boolean validateUser(String identifier, String password) throws PersistanceException{
        String query = "SELECT 1 FROM user WHERE (user_name = ? OR email = ?) AND password = ?";
        Connection conn = SQLConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            stmt.setString(3, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DBDataNotFound("Error validating user credential " + e.getMessage());
        }
    }

    public void updateBalance(double newPurchaseValue, String identifier) throws PersistanceException{
        try{
            double oldBalance = getUserByUsernameOrEmail(identifier).getBalance();
            String query = "UPDATE USER SET balance = ? WHERE name = ? OR email = ?";
            Connection conn = SQLConnector.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)){
                stmt.setDouble(1, newPurchaseValue + oldBalance);
                stmt.setString(2, identifier);
                stmt.setString(3, identifier);
            }catch (SQLException e){
                throw new DBDataNotFound("Error validating user credential " + e.getMessage());
            }
        }catch (DBDataNotFound e){
            throw new DBDataNotFound("Error user not found");
        }


    }

    public boolean removeUser (String identifier)  throws PersistanceException{
        String query = "DELETE FROM user WHERE user_name = ? OR email = ?";
        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Error deleting user " + e.getMessage());
        }
    }

    public void updateCryptoDeletedFlag(String identifier, boolean flagValue) throws PersistanceException{
        String query = "UPDATE USER SET cryptoDeleted = ? WHERE user_name = ? OR email  = ?";
        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, flagValue? "1": "0");
            stmt.setString(2, identifier);
            stmt.setString(3, identifier);
            //TODO: Tirar excepcion si no se ha afectado a ninguna columna
            int rowsAffected = stmt.executeUpdate();
            //return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DBConnectionNotReached(e.getMessage());
        }
    }
}