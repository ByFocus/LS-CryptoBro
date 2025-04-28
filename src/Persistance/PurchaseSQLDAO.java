package Persistance;

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

    public String[] getUsernamesByCryptoName(String cryptoName) {
        if (cryptoName == null) {
            throw new IllegalArgumentException("Crypto name must not be null");
        }

        List<String> usernames = new ArrayList<>();
        String query = "SELECT DISTINCT user_name FROM bought WHERE crypto_name = ?";
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
        return usernames.toArray(new String[0]);
    }
}