package persistance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseSQLDAO {

    public void addPurchase (User user, Purchase purchase) {
        String query = "INSERT INTO bought (user_name, crypto_name, number, changesWarrant) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, purchase.getCryptoName());
            stmt.setInt(3, purchase.getNumber());
            stmt.setBoolean(4, purchase.isChangesWarrant()); // si es booleano

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getUsernamesByCryptoName (String crypto) {
        List<String> usernames = new ArrayList<>();
        String query = "SELECT DISTINCT user_name FROM bought WHERE crypto_name = ?";

        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cryptoName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                usernames.add(rs.getString("user_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usernames.toArray(new String[0]);
    }
}
