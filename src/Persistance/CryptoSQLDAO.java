package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.ConfigurationFileError;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CryptoSQLDAO implements CryptoDAO{

    private final String CRYPTO_FAILED = "Failed to create crypto entry";

    public boolean createCrypto(Crypto crypto) throws PersistanceException {

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
            throw new ConfigurationFileError(CRYPTO_FAILED);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
            // Note: Don't close conn here since it's managed by SQLConnector
        }
    }

    public void updateCryptoPrice(String cryptoName, Double price)  throws PersistanceException{

        String query = "UPDATE cryptocurrency SET current_value = ? WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new SQLException("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setDouble(1, price);
            stmt.setString(2, cryptoName);

            int rowsAffected = stmt.executeUpdate();
            //return rowsAffected > 0;
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
//TODO: IGUAL NO SE NECESSITA
    public boolean updateCrypto(Crypto crypto)  throws PersistanceException{
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

    public void deleteCrypto(String cryptoname) throws PersistanceException{
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

    public List<Crypto> getAllCryptos()  throws PersistanceException{
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

    public Crypto getCryptoByName(String name)  throws PersistanceException{
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

    public List<Crypto> getCryptoByCategory (String category) throws PersistanceException {
        String query = "SELECT * FROM cryptocurrency WHERE category = ? AND cryptoDeleted = false";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Crypto crypto = null;
        List<Crypto> cryptos = new ArrayList<>();

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, category);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double currentPrice = rs.getDouble("current_value");
                double initialPrice = rs.getDouble("init_value");
                int volatility = rs.getInt("volatility");

                crypto = new Crypto(name, category, currentPrice, initialPrice, volatility);
                cryptos.add(crypto);
            }
        } catch (SQLException e) {
            throw new DBDataNotFound("Failed to fetch crypto by category");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources: " + e.getMessage());
            }
        }
        return cryptos;
    }

    public List<String> getCategories() throws PersistanceException {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM cryptocurrency WHERE cryptoDeleted = false";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            throw new DBDataNotFound("Failed to retrieve categories " +  e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources: " + e.getMessage());
            }
        }

        return categories;
    }

    public String addCryptosFromFile() throws PersistanceException{
        List<Crypto> cryptos = new ArrayList<>();
        int cryptoCount = 0;
        StringBuilder log = new StringBuilder();
        StringBuilder error = new StringBuilder();
        for (Crypto crypto: cryptos) {
            try {
                getCryptoByName(crypto.getName());
                error.append(crypto.getName() + " ya existe. ");
            }
            catch (DBDataNotFound _) {
                // si no es troba está bé;
                createCrypto(crypto);
                cryptoCount ++;
            }
        }
        log.append("Se han añadido " + cryptoCount + " cryptos.\n");
        if (!error.isEmpty()) {
            log.append("\t[" + error.toString() + ")]");
        }
        return log.toString();
    }

}