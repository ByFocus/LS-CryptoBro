package Persistance;

import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Business.Entities.User;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseSQLDAO implements PurchaseDAO {

    public void addPurchase(User user, Purchase purchase) throws PersistanceException {

        String query = "INSERT INTO bought (user_name, crypto_name, number) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, purchase.getCrypto());
            stmt.setDouble(3, purchase.getUnits());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DBConnectionNotReached("Failed to add purchase");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to add purchase to database " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Failed to close statement " + e.getMessage());
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }
    }

    public List<String> getUsernamesByCryptoName(String cryptoName) throws PersistanceException {

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
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }
        return usernames;
    }

    public List<Purchase> getPurchasesByUserName(String user) throws PersistanceException {
        List<Purchase> purchases = new ArrayList<>();
        String query = "SELECT DISTINCT * FROM purchase WHERE user_name = ?";
        CryptoDAO cryptoDAO = new CryptoSQLDAO();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) throw new DBConnectionNotReached("Database connection is null");

            stmt = conn.prepareStatement(query);
            stmt.setString(1, user);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Crypto crypto = cryptoDAO.getCryptoByName(rs.getString("crypto_name"));
                int number = rs.getInt("number");
                purchases.add(new Purchase(crypto, number, crypto.getCurrentPrice()));
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to get purchases by user name " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources");
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }

        return purchases;
    }

    public double sellAllPurchasesFromCrypto(String cryptoName, String userName) throws PersistanceException {
        String queryUnits = "SELECT SUM(number) FROM purchase WHERE crypto_name = ? AND user_name = ?";
        String queryDelete = "DELETE FROM purchase WHERE crypto_name = ? AND user_name = ?";
        double benefits;

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) throw new DBConnectionNotReached("Database connection is null");

            stmt = conn.prepareStatement(queryUnits);
            stmt.setString(1, cryptoName);
            stmt.setString(2, userName);
            rs = stmt.executeQuery();

            if (rs.next()) {
                benefits = rs.getDouble(1); // total units
                try (PreparedStatement deleteStmt = conn.prepareStatement(queryDelete)) {
                    deleteStmt.setString(1, cryptoName);
                    deleteStmt.setString(2, userName);
                    deleteStmt.executeUpdate();
                    benefits *= new CryptoSQLDAO().getCryptoCurrentPrice(cryptoName);
                }
            } else {
                throw new DBDataNotFound("Purchase not found");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to get purchases by user name " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources");
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }

        return benefits;
    }
}
