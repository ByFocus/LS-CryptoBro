package Persistance;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CryptoSQLDAO {

    public boolean updateCrypto(Crypto crypto) {
        if (crypto == null || crypto.getName() == null) {
            throw new IllegalArgumentException("Crypto object and its name must not be null");
        }

        String query = "UPDATE cryptos SET current_price = ?, category = ?, volatility = ? WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setDouble(1, crypto.getCurrentPrice());
            stmt.setString(2, crypto.getCategory());
            stmt.setDouble(3, crypto.getVolatility());
            stmt.setString(4, crypto.getName());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating crypto: " + e.getMessage());
            throw new RuntimeException("Failed to update crypto in database", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            // Note: Don't close conn here as it's managed by SQLConnector
        }
    }

    public List<Crypto> getAllCryptos() {
        List<Crypto> cryptoList = new ArrayList<>();
        String query = "SELECT * FROM cryptos";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

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
            System.err.println("Error fetching all cryptos: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve cryptos from database", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
            // Note: Don't close conn here as it's managed by SQLConnector
        }
        return cryptoList;
    }

    public Crypto getCryptoByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Crypto name must not be null");
        }

        String query = "SELECT * FROM cryptos WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Crypto crypto = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String cryptoName = rs.getString("name");
                double currentPrice = rs.getDouble("current_price");
                double initialPrice = rs.getDouble("initial_price");
                String category = rs.getString("category");
                double volatility = rs.getDouble("volatility");

                crypto = new Crypto(cryptoName, currentPrice, initialPrice, category, volatility);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching crypto by name: " + e.getMessage());
            throw new RuntimeException("Failed to get crypto by name from database", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
            // Note: Don't close conn here as it's managed by SQLConnector
        }
        return crypto;
    }
}