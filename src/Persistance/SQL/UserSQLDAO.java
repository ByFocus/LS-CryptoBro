package Persistance.SQL;

import Business.Entities.User;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.DBModifyData;
import Persistance.PersistanceExceptions.PersistanceException;
import Persistance.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL implementation of the UserDAO interface.
 * Handles persistence operations related to user accounts.
 */
public class UserSQLDAO implements UserDAO {

    /**
     * Registers a new user in the database.
     *
     * @param user the user to register
     * @throws PersistanceException if the insertion fails or connection is not available
     */
    public void registerUser(User user) throws PersistanceException {
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
            if(rowsAffected == 0){
                throw new DBConnectionNotReached("Failed to create user");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to create user");
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing statement: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieves a user by username or email.
     *
     * @param value the username or email to search
     * @return the User object if found
     * @throws PersistanceException if no matching user is found or the query fails
     */
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
                user = new User(user_name, password, email, balance, cryptoDeleted);
            } else {
                throw new DBDataNotFound("No user found with username or email: " + value);
            }
        } catch (SQLException e) {
           throw new DBConnectionNotReached("Failed to get user " + e.getMessage());
        }

        return user;
    }

    /**
     * Validates user credentials.
     *
     * @param identifier username or email
     * @param password   user password
     * @return true if valid, false otherwise
     * @throws PersistanceException if the query fails
     */
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

    /**
     * Updates the balance of a user after a purchase.
     *
     * @param newPurchaseValue the amount to add
     * @param identifier       username or email
     * @throws PersistanceException if the user is not found or the update fails
     */
    public void updateBalance(double newPurchaseValue, String identifier) throws PersistanceException{
        String query = "UPDATE USER SET balance = ? WHERE user_name = ? OR email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            double oldBalance = getUserByUsernameOrEmail(identifier).getBalance();
            try{
                conn = SQLConnector.getInstance().getConnection();
                if (conn == null) {
                    throw new DBConnectionNotReached("Could not reach DB!");
                }

                stmt = conn.prepareStatement(query);
                stmt.setDouble(1, newPurchaseValue + oldBalance);
                stmt.setString(2, identifier);
                stmt.setString(3, identifier);
                int rows = stmt.executeUpdate();
                if (rows == 0) {
                    throw new DBModifyData("No rows updated");
                }

            }catch (SQLException e){
                throw new DBConnectionNotReached(e.getMessage());
            }
        } catch (DBDataNotFound e) {
            throw new DBDataNotFound("Couldn't update balance because user not retrieved from database");
        }


    }

    /**
     * Removes a user account from the database.
     *
     * @param identifier username or email
     * @throws PersistanceException if the deletion fails
     */
    public void removeUser (String identifier)  throws PersistanceException{
        String query = "DELETE FROM user WHERE user_name = ? OR email = ?";
        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new DBConnectionNotReached("Failed to remove user");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Error deleting user " + e.getMessage());
        }
    }

    /**
     * Updates the cryptoDeleted flag for a user.
     *
     * @param identifier username or email
     * @param flagValue  true if deleted, false otherwise
     * @throws PersistanceException if the update fails
     */
    public void updateCryptoDeletedFlag(String identifier, boolean flagValue) throws PersistanceException{
        String query = "UPDATE USER SET cryptoDeleted = ? WHERE user_name = ? OR email  = ?";
        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, flagValue? "1": "0");
            stmt.setString(2, identifier);
            stmt.setString(3, identifier);

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new DBConnectionNotReached("Failed to update user");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached(e.getMessage());
        }
    }

    /**
     * Updates the password of a user.
     *
     * @param identifier username or email
     * @param password   new password
     * @throws PersistanceException if the update fails
     */
    public void updatePassword(String identifier, String password) throws PersistanceException{
        String query = "UPDATE USER SET password = ? WHERE user_name = ? OR email = ?";
        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, password);
            stmt.setString(2, identifier);
            stmt.setString(3, identifier);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new DBConnectionNotReached("Failed to update user");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached(e.getMessage());
        }
    }
}