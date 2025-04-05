package persistance;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSQLDAO {

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
}