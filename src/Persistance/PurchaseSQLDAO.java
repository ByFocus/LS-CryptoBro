package Persistance;

import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Business.Entities.User;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseSQLDAO implements PurchaseDAO{

    public boolean addPurchase(User user, Purchase purchase)  throws PersistanceException {

        String query = "INSERT INTO bought (user_name, crypto_name, number) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, purchase.getCrypto());
            stmt.setDouble(3, purchase.getUnits());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to add purchase to database " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Failed to close statement " + e.getMessage());
            }
            // Note: Don't close conn here as it's managed by SQLConnector
        }
    }

    public List<String> getUsernamesByCryptoName(String cryptoName)  throws PersistanceException{
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
            throw new DBConnectionNotReached("Failed to get usernames by crypto name " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Failed to close statement " + e.getMessage());
            }
            // Note: Don't close conn here as it's managed by SQLConnector
        }
        return usernames;
    }

    public List<Purchase> getPurchasesByUserName(String user)  throws PersistanceException{
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
            throw new DBConnectionNotReached("Failed to get purchases by user name " + e.getMessage());
        }

        return purchases;
    }
}