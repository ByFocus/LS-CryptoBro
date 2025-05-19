package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.ConfigurationFileError;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CryptoSQLDAO implements CryptoDAO {

    private final String CRYPTO_FAILED = "Failed to create crypto entry";

    public void createCrypto(Crypto crypto) throws PersistanceException {
        String query = "INSERT INTO cryptocurrency (name, init_value, current_value, category, volatility) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, crypto.getName());
            stmt.setDouble(2, crypto.getInitialPrice());
            stmt.setDouble(3, crypto.getCurrentPrice());
            stmt.setString(4, crypto.getCategory());
            stmt.setInt(5, crypto.getVolatility());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DBConnectionNotReached("Failed to create crypto");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Couldn't connect to database " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Couldn't close stmt " + e.getMessage());
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }
    }

    public void updateCryptoPrice(String cryptoName, Double price) throws PersistanceException {
        String query = "UPDATE cryptocurrency SET current_value = ? WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setDouble(1, price);
            stmt.setString(2, cryptoName);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DBConnectionNotReached("Couldn't update crypto");
            }
        } catch (SQLException e) {
            throw new DBDataNotFound("Failed to update crypto in database " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing statement " + e.getMessage());
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }
    }

    public void updateCrypto(Crypto crypto) throws PersistanceException {
        String query = "UPDATE cryptocurrency SET current_value = ?, category = ?, volatility = ? WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setDouble(1, crypto.getCurrentPrice());
            stmt.setString(2, crypto.getCategory());
            stmt.setDouble(3, crypto.getVolatility());
            stmt.setString(4, crypto.getName());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DBConnectionNotReached("Failed to update crypto");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to update crypto in database" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing statement " + e.getMessage());
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }
    }

    public double getCryptoCurrentPrice(String cryptoName) throws PersistanceException {
        String query = "SELECT current_value FROM cryptocurrency WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, cryptoName);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("current_value");
            } else {
                throw new DBDataNotFound("No cryptocurrency found with name: " + cryptoName);
            }
        } catch (SQLException e) {
            throw new DBDataNotFound(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources!");
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }
    }

    public void deleteCrypto(String cryptoname) throws PersistanceException {
        String query = "DELETE FROM cryptocurrency WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }
            stmt = conn.prepareStatement(query);
            stmt.setString(1, cryptoname);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DBDataNotFound("Couldn't find crypto");
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to delete crypto in database" + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources");
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }
    }

    public List<Crypto> getAllCryptos() throws PersistanceException {
        List<Crypto> cryptoList = new ArrayList<>();
        String query = "SELECT * FROM cryptocurrency";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
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
            throw new DBDataNotFound("Failed to retrieve cryptos from database " + e.getMessage());
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
        return cryptoList;
    }

    public Crypto getCryptoByName(String name) throws PersistanceException {
        String query = "SELECT * FROM cryptocurrency WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Crypto crypto = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
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
            } else {
                throw new DBDataNotFound("No crypto found with name: " + name);
            }
        } catch (SQLException e) {
            throw new DBDataNotFound("Failed to get crypto by name from database " + e.getMessage());
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
        return crypto;
    }

    public List<Crypto> getCryptoByCategory(String category) throws PersistanceException {
        String query = "SELECT * FROM cryptocurrency WHERE category = ? AND cryptoDeleted = false";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Crypto> cryptos = new ArrayList<>();

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, category);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                double currentPrice = rs.getDouble("current_value");
                double initialPrice = rs.getDouble("init_value");
                int volatility = rs.getInt("volatility");

                Crypto crypto = new Crypto(name, category, currentPrice, initialPrice, volatility);
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
            } finally {
                SQLConnector.getInstance().disconnect();
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
            throw new DBDataNotFound("Failed to retrieve categories " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached("Error closing resources: " + e.getMessage());
            } finally {
                SQLConnector.getInstance().disconnect();
            }
        }

        return categories;
    }

    public String addCryptosFromFile(File file) throws PersistanceException {
        List<Crypto> cryptos = new CryptoFileReadingJSONDAO().readCryptoFromFile(file);
        int cryptoCount = 0;
        StringBuilder log = new StringBuilder();
        StringBuilder error = new StringBuilder();
        for (Crypto crypto : cryptos) {
            try {
                getCryptoByName(crypto.getName());
                error.append(crypto.getName()).append(" ya existe. ");
            } catch (DBDataNotFound _) {
                createCrypto(crypto);
                cryptoCount++;
            }
        }
        log.append("Se han añadido ").append(cryptoCount).append(" cryptos.\n");
        if (!error.isEmpty()) {
            log.append("\t\t[").append(error.toString()).append("]");
        }
        return log.toString();
    }
}
