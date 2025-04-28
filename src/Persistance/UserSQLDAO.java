package Persistance;

import Business.Entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSQLDAO implements UserDAO{

    public boolean createUser(User user) {
        if (user == null || user.getUsername() == null || user.getMail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User and its fields must not be null");
        }

        String query = "INSERT INTO user (user_name, email, password) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new RuntimeException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getMail());
            stmt.setString(3, user.getPassword());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            throw new RuntimeException("Failed to create user", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            // Note: Don't close conn here since it's managed by SQLConnector
        }
    }

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM user WHERE user_name = '" + username + "';";
        ResultSet result = SQLConnector.getInstance().selectQuery(query);
        User user = null;

        try {
            if (result.next()) {
                String user_name = result.getString("user_name");
                String mail = result.getString("email");
                String password = result.getString("password");
                user = new User(user_name, mail, password, 0, false); //fix
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByMail(String mail) {
        String query = "SELECT * FROM user WHERE email = '" + mail + "';";
        ResultSet result = SQLConnector.getInstance().selectQuery(query);
        User user = null;

        try {
            if (result.next()) {
                String user_name = result.getString("user_name");
                String email = result.getString("email");
                String password = result.getString("password");
                user = new User(user_name, email, password, 0, false); //fix
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean validateAdmin(String password) {
        String query = "SELECT * FROM user WHERE role = 'admin' AND password = '" + password + "';";
        ResultSet result = SQLConnector.getInstance().selectQuery(query);

        try {
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateCredentials(String identifier, String password) {
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
            throw new RuntimeException("Error validating user credential", e);
        }
    }

    public boolean deleteUser (String identifier) {
        String query = "DELETE FROM user WHERE user_name = ? OR email = ?";
        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, identifier);
            stmt.setString(2, identifier);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }
}