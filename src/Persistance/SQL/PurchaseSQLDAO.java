package Persistance.SQL;

import Business.Entities.Purchase;
import Business.Entities.User;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.DBModifyData;
import Persistance.PersistanceExceptions.PersistanceException;
import Persistance.PurchaseDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL implementation of the PurchaseDAO interface.
 * Handles operations related to user purchases of cryptocurrencies.
 */
public class PurchaseSQLDAO implements PurchaseDAO {

    /**
     * Adds a new purchase for a user.
     *
     * @param user     the user making the purchase
     * @param purchase the purchase details
     * @throws PersistanceException if database access fails
     */
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
        }
    }

    /**
     * Retrieves all distinct usernames that have purchased a given cryptocurrency.
     *
     * @param cryptoName the name of the cryptocurrency
     * @return list of usernames
     * @throws PersistanceException if the query fails
     */
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
        }
        return usernames;
    }

    /**
     * Retrieves all purchases made by a specific user.
     *
     * @param user the username
     * @return list of Purchase objects
     * @throws PersistanceException if the query fails
     */
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

    /**
     * Retrieves the database ID (buy_id) of a specific purchase.
     *
     * @param purchase the purchase object
     * @param username the username who made the purchase
     * @return the purchase ID from the database
     * @throws PersistanceException if the purchase is not found or query fails
     */
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
        }
        return buyId;
    }

    /**
     * Subtracts units from a user's purchase. If the result is 0, deletes the purchase.
     *
     * @param purchase         the original purchase
     * @param username         the user who owns the purchase
     * @param unitsToSubtract  the number of units to subtract
     * @throws PersistanceException if update fails, purchase is invalid, or user has insufficient units
     */
    public void subtractUnits(Purchase purchase, String username, int unitsToSubtract) throws PersistanceException{
        String query = "UPDATE PURCHASE SET number = ? WHERE buy_id = ?";
        int newUnits = purchase.getUnits() - unitsToSubtract;
        int buyId;
        buyId = getBuyId(purchase, username);
        if (newUnits < 0) {
             throw new DBModifyData("Brother, no tienes tantas cryptos. Pon los pies en la tierra campeón.");
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
        if (newUnits == 0) {
            deletePurchaseById(buyId);
        }
    }

    /**
     * Sells all purchases of a specific cryptocurrency for a given user.
     * Deletes the records and returns the total earnings.
     *
     * @param cryptoName the cryptocurrency to sell
     * @param userName   the user performing the sale
     * @return the total earnings from the sale
     * @throws PersistanceException if retrieval or deletion fails
     */
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
                throw new DBConnectionNotReached("Database connection is null");
            }
            stmt = conn.prepareStatement(queryUnits);

            stmt.setString(1, cryptoName);
            stmt.setString(2, userName);
            rs = stmt.executeQuery();
                if (rs.next()) {
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

    /**
     * Deletes a specific purchase from the database by its ID.
     *
     * @param id the purchase ID (buy_id)
     * @throws PersistanceException if deletion fails or purchase is not found
     */
    private void deletePurchaseById(int id) throws PersistanceException{
        String query = "DELETE FROM purchase WHERE buy_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }
            stmt = conn.prepareStatement(query);

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new DBDataNotFound("Purchase not found");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Error al ejecutar la query");
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources");
            }
        }
    }

    /**
     * Deletes all purchases associated with a specific user.
     *
     * @param userName the username whose purchases should be deleted
     * @throws PersistanceException if the deletion fails
     */
    public void deletePurchasesFromUser(String userName) throws PersistanceException{
        String query = "DELETE FROM purchase WHERE user_name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }
            stmt = conn.prepareStatement(query);

            stmt.setString(1, userName);

            int rows = stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Error al ejecutar la query");
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources");
            }
        }
    }
}
