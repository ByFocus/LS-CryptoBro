package Persistance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CryptoSQLDAO {

    public void updateCrypto (Crypto crypto) {
        String query = "UPDATE cryptos SET current_price = ?, category = ?, volatility = ? WHERE name = ?";

        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, crypto.getCurrentPrice());
            stmt.setString(2, crypto.getCategory());
            stmt.setDouble(3, crypto.getVolatility());
            stmt.setString(4, crypto.getName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Crypto> getAllCryptos() {
        List<Crypto> cryptoList = new ArrayList<>();
        String query = "SELECT * FROM cryptos";

        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                double currentPrice = rs.getDouble("current_price");
                double initialPrice = rs.getDouble("initial_price");
                String category = rs.getString("category");
                double volatility = rs.getDouble("volatility");

                Crypto crypto = new Crypto(name, currentPrice, initialPrice, category, volatility);
                cryptoList.add(crypto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cryptoList;
    }

    public Crypto getCryptoByName (String name) {
        String query = "SELECT * FROM cryptos WHERE name = ?";
        Crypto crypto = null;

        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String cryptoName = rs.getString("name");
                double currentPrice = rs.getDouble("current_price");
                double initialPrice = rs.getDouble("initial_price");
                String category = rs.getString("category");
                double volatility = rs.getDouble("volatility");

                crypto = new Crypto(cryptoName, currentPrice, initialPrice, category, volatility);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crypto;
    }
}
