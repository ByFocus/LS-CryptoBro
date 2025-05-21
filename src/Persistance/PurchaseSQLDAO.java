package Persistance;

import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Business.Entities.User;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.DBModifyData;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseSQLDAO implements PurchaseDAO{

    public void addPurchase(User user, Purchase purchase)  throws PersistanceException {

        String query = "INSERT INTO purchase (user_name, crypto_name, number, buy_price) VALUES (?, ?, ?, ?)";
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
            stmt.setDouble(4, purchase.getPriceUnit());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new DBConnectionNotReached("Failed to add purchase");
            }
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
        List<Purchase> purchases = new ArrayList<>();
        String query = "SELECT DISTINCT * FROM purchase WHERE user_name = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try  {
            Connection conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }
            stmt = conn.prepareStatement(query);

            stmt.setString(1, user);
            rs = stmt.executeQuery();
                while (rs.next()) {
                    String name = rs.getString("crypto_name");
                    int number = rs.getInt("number"); // assuming "number" is int, change type if needed
                    double price = rs.getDouble("buy_price");
                    purchases.add(new Purchase(name, number,price));
                }

        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to get purchases by user name " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources");
            }
        }
        return purchases;
    }

    private int getBuyId(Purchase purchase, String username) throws PersistanceException{
        int buyId = -1;
        String query = "SELECT buy_id FROM purchase WHERE crypto_name = ? AND user_name = ? AND number = ? AND buy_price = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, purchase.getCrypto());
            stmt.setString(2, username);
            stmt.setInt(3, purchase.getUnits());
            stmt.setDouble(4, purchase.getPriceUnit());
            rs = stmt.executeQuery();

            while (rs.next()) {
                buyId = rs.getInt("buy_id");
            }
            if(buyId == -1) {
                throw new DBDataNotFound("Purchase not found!");
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
        return buyId;
    }


    public void substractUnits(Purchase purchase, String username, int unitsToSubstract) throws PersistanceException{
        String query = "UPDATE PURCHASE SET number = ? WHERE buy_id = ?";
        int newUnits;
        int buyId;
        try{
            buyId = getBuyId(purchase, username);
             newUnits = purchase.getUnits() - unitsToSubstract;
             if (newUnits < 0) {
                 throw new DBModifyData("Brother, no tienes tantas cryptos. Pon los pies en la tierra campeón.");
             }
        }catch (PersistanceException ex){
            throw ex;
        }
        try {
            Connection conn = SQLConnector.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, newUnits);
            stmt.setInt(2, buyId);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new DBConnectionNotReached("Failed to update purchase");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached(e.getMessage());
        }
    }

    public double sellAllPurchasesFromCrypto(String cryptoName, String userName) throws PersistanceException{
        String queryUnits = "SELECT SUM(number) FROM purchase WHERE crypto_name = ? AND user_name = ?";
        String queryDelete = "DELETE FROM purchase WHERE crypto_name = ? AND user_name = ?";
        double benefits;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }
            stmt = conn.prepareStatement(queryUnits);

            stmt.setString(1, cryptoName);
            stmt.setString(2, userName);
            rs = stmt.executeQuery();
                if (rs.next()) {
                    // aquí tenemos el número de unidades, ahora hay que venderlas
                    benefits = rs.getDouble(1);
                    try (PreparedStatement deleteStmt = conn.prepareStatement(queryDelete)) {
                        deleteStmt.setString(1, cryptoName);
                        deleteStmt.setString(2, userName);
                        deleteStmt.executeUpdate();
                        benefits = benefits * (new CryptoSQLDAO().getCryptoCurrentPrice(cryptoName));

                    }
                } else {
                    throw new DBDataNotFound("Purchase not found");
                }

        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to get purchases by user name " + e.getMessage());
        }finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources");
            }
        }
        return benefits;
    }
}
