package Persistance;

import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Business.Entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseSQLDAO implements PurchaseDAO{

    public boolean addPurchase(User user, Purchase purchase) {
        if (user == null || user.getUsername() == null || purchase == null || purchase.getCrypto().getName() == null) {
            throw new IllegalArgumentException("User and Purchase objects and their required fields must not be null");
        }

        String query = "INSERT INTO bought (user_name, crypto_name, number, changesWarrant) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, purchase.getCrypto().getName());
            stmt.setDouble(3, purchase.getUnits());
            //stmt.setBoolean(4, purchase.isChangesWarrant()); !!!!!!!!!

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding purchase: " + e.getMessage());
            throw new RuntimeException("Failed to add purchase to database", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            // Note: Don't close conn here as it's managed by SQLConnector
        }
    }

    public List<String> getUsernamesByCryptoName(String cryptoName) {
        if (cryptoName == null) {
            throw new IllegalArgumentException("Crypto name must not be null");
        }

        List<String> usernames = new ArrayList<>();
        String query = "SELECT DISTINCT user_name FROM purchase WHERE crypto_name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, cryptoName);
            rs = stmt.executeQuery();

            while (rs.next()) {
                usernames.add(rs.getString("user_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching usernames: " + e.getMessage());
            throw new RuntimeException("Failed to get usernames by crypto name", e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
            // Note: Don't close conn here as it's managed by SQLConnector
        }
        return usernames;
    }
    public List<Purchase> getPurchasesByUserName(String user) {
        if (user == null) {
            throw new IllegalArgumentException("Username must not be null!");
        }

        List<Purchase> purchases = new ArrayList<>();
        String query = "SELECT DISTINCT * FROM purchase WHERE user_name = ?";
        CryptoDAO cryptoDAO = new CryptoSQLDAO();

        try (
                Connection conn = SQLConnector.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, user);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Crypto crypto = cryptoDAO.getCryptoByName(rs.getString("crypto_name"));
                    int number = rs.getInt("number"); // assuming "number" is int, change type if needed
                    purchases.add(new Purchase(crypto, number, crypto.getCurrentPrice()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get purchases by user name", e);
        }

        return purchases;
    }
}