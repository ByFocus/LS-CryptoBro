package persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSQLDAO {

    public boolean createUser (User user) {
        String query = "INSERT INTO users (user_name, user_email, user_password) VALUES (?, ?, ?)";
        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retorna true en cas de que s'hagui modifica alguna row de la bbd
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE user_name = '" + username + "';";
        ResultSet result = SQLConnector.getInstance().selectQuery(query);
        User user = null;

        try {
            if (result.next()) {
                String user_name = result.getString("user_name");
                String mail = result.getString("email");
                String password = result.getString("password");
                user = new User(user_name, mail, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByMail(String mail) {
        String query = "SELECT * FROM users WHERE email = '" + mail + "';";
        ResultSet result = SQLConnector.getInstance().selectQuery(query);
        User user = null;

        try {
            if (result.next()) {
                String user_name = result.getString("user_name");
                String email = result.getString("email");
                String password = result.getString("password");
                user = new User(user_name, email, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean validateAdmin(String password) {
        String query = "SELECT * FROM users WHERE role = 'admin' AND password = '" + password + "';";
        ResultSet result = SQLConnector.getInstance().selectQuery(query);

        try {
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean validateCredentials(String identifier, String password) {
        String query = "SELECT 1 FROM users WHERE (user_name = ? OR user_email = ?) AND user_password = ?";
        Connection conn = SQLConnector.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        String query = "DELETE FROM users WHERE user_name = ? OR user_email = ?";
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