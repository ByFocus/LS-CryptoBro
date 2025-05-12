package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CryptoSQLDAO implements CryptoDAO{

    public boolean createCrypto(Crypto crypto){
        if(crypto == null ){
            throw new IllegalArgumentException("Crypto object must be not null");
        }

        String query = "INSERT INTO cryptocurrency (name, init_value, current_value, category, volatility, cryptoDeleted) VALUES (?, ?, ?, ?, ?, ?)"; // Removed '?' from cryptoDeleted

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new RuntimeException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, crypto.getName());
            stmt.setDouble(2, crypto.getInitialPrice());
            stmt.setDouble(3, crypto.getCurrentPrice());
            stmt.setString(4, crypto.getCategory());
            stmt.setInt(5, crypto.getVolatility());
            stmt.setBoolean(6, false); // Assuming you want to set cryptoDeleted as false initially

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create crypto entry", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            // Note: Don't close conn here since it's managed by SQLConnector
        }
    }
    public boolean updateCrypto(Crypto crypto) {
        if (crypto == null || crypto.getName() == null) {
            throw new IllegalArgumentException("Crypto object and its name must not be null");
        }

        String query = "UPDATE cryptocurrency SET current_value = ?, category = ?, volatility = ? WHERE name = ?";
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
            throw new RuntimeException("Failed to update crypto in database", e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing statement " + e.getMessage());
            }
            // Note: Don't close conn here as it's managed by SQLConnector
        }
    }

    public double getCryptoCurrentPrice(String cryptoName) throws PersistanceException {
        // Correct the query syntax to SELECT the current_value from the table
        String query = "SELECT current_value FROM cryptocurrency WHERE name = ?";
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

            if (rs.next()) {
                // Retrieve the double value from the ResultSet
                return rs.getDouble("current_value");
            } else {
                throw new DBDataNotFound("No cryptocurrency found with name: " + cryptoName);
            }
        } catch (SQLException e) {
            throw new DBDataNotFound(e.getMessage());
        } finally {
            // Safely close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources!");
                // Use logging for resources closing error if necessary
            }
        }
    }

    public void deleteCrypto(String cryptoname){
        if(cryptoname == null){
            throw new IllegalArgumentException("Crypto name cannot be null!");
        }
        String query = "UPDATE cryptocurrency SET cryptoDeleted = ? WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try{
            conn = SQLConnector.getInstance().getConnection();
            if(conn == null){
                throw new SQLException("Database connection is null");
            }
            stmt = conn.prepareStatement(query);
            stmt.setBoolean(1, true);
            stmt.setString(2, cryptoname);

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No records were updated. Check if the crypto name exists.");
            }
        } catch (SQLException e){
            throw new RuntimeException("Failed to delete crypto in database", e);
        } finally{
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
        }
    }

    public List<Crypto> getAllCryptos() {
        List<Crypto> cryptoList = new ArrayList<>();
        String query = "SELECT * FROM cryptocurrency";
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
                double currentPrice = rs.getDouble("current_value");
                double initialPrice = rs.getDouble("init_value");
                String category = rs.getString("category");
                int volatility = rs.getInt("volatility");

                Crypto crypto = new Crypto(name, category, currentPrice, initialPrice, volatility);
                cryptoList.add(crypto);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all cryptocurrency: " + e.getMessage());
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

        String query = "SELECT * FROM cryptocurrency WHERE name = ?";
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
                double currentPrice = rs.getDouble("current_value");
                double initialPrice = rs.getDouble("init_value");
                String category = rs.getString("category");
                int volatility = rs.getInt("volatility");

                crypto = new Crypto(name, category, currentPrice, initialPrice, volatility);
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