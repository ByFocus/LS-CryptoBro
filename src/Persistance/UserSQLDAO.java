package Persistance;

import Business.Entities.User;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserSQLDAO implements UserDAO {

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
            if (rowsAffected == 0) {
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
            SQLConnector.getInstance().disconnect(); // Ensures connection is closed
        }
    }

    public User getUserByUsernameOrEmail(String value) throws PersistanceException {
        String query = "SELECT * FROM user WHERE user_name = ? OR email = ?;";
        User user = null;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, value);
            result = preparedStatement.executeQuery();

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
        } finally {
            try {
                if (result != null) result.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources: " + e.getMessage());
            }
            SQLConnector.getInstance().disconnect();
        }

        return user;
    }

    public boolean validateUser(String identifier, String password) throws PersistanceException {
        String query = "SELECT 1 FROM user WHERE (user_name = ? OR email = ?) AND password = ?";
        Connection conn = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, identifier);
                stmt.setString(2, identifier);
                stmt.setString(3, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DBDataNotFound("Error validating user credential " + e.getMessage());
        } finally {
            SQLConnector.getInstance().disconnect();
        }
    }

    public void updateBalance(double newPurchaseValue, String identifier) throws PersistanceException {
        Connection conn = null;
        try {
            double oldBalance = getUserByUsernameOrEmail(identifier).getBalance();
            String query = "UPDATE USER SET balance = ? WHERE name = ? OR email = ?";
            conn = SQLConnector.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDouble(1, newPurchaseValue + oldBalance);
                stmt.setString(2, identifier);
                stmt.setString(3, identifier);
                stmt.executeUpdate();
            }
        } catch (DBDataNotFound e) {
            throw new DBDataNotFound("Error user not found");
        } catch (SQLException e) {
            throw new DBDataNotFound("Error updating balance: " + e.getMessage());
        } finally {
            SQLConnector.getInstance().disconnect();
        }
    }

    public void removeUser(String identifier) throws PersistanceException {
        String query = "DELETE FROM user WHERE user_name = ? OR email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = SQLConnector.getInstance().getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DBConnectionNotReached("Failed to remove user");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Error deleting user " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing statement: " + e.getMessage());
            }
            SQLConnector.getInstance().disconnect();
        }
    }

    public void updateCryptoDeletedFlag(String identifier, boolean flagValue) throws PersistanceException {
        String query = "UPDATE USER SET cryptoDeleted = ? WHERE user_name = ? OR email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, flagValue);
            stmt.setString(2, identifier);
            stmt.setString(3, identifier);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DBConnectionNotReached("Failed to update crypto");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached(e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing statement: " + e.getMessage());
            }
            SQLConnector.getInstance().disconnect();
        }
    }
}
