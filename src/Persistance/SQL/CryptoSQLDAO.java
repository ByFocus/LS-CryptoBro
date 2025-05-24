package Persistance.SQL;

import Business.Entities.Crypto;
import Persistance.CryptoDAO;
import Persistance.CryptoFileReadingJSONDAO;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type CryptoSQLDAO.
 * SQL implementation of the CryptoDAO interface for managing cryptocurrency entities in the database.
 */
public class CryptoSQLDAO implements CryptoDAO {

    private final String CRYPTO_FAILED = "Error al editar o crear la entrada de la criptomoneda";
    private final String DB_CONNECTION_FAILED = "Error al conectar con la base de datos";

    /**
     * Inserts a new cryptocurrency into the database.
     *
     * @param crypto the Crypto object to insert
     * @throws PersistanceException if the insertion fails or the database connection is null
     */
    public void createCrypto(Crypto crypto) throws PersistanceException {

        String query = "INSERT INTO cryptocurrency (name, init_value, current_value, category, volatility) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, crypto.getName());
            stmt.setDouble(2, crypto.getInitialPrice());
            stmt.setDouble(3, crypto.getCurrentPrice());
            stmt.setString(4, crypto.getCategory());
            stmt.setInt(5, crypto.getVolatility());

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new DBConnectionNotReached(CRYPTO_FAILED);
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
            }
        }
    }

    /**
     * Updates the current price of the given cryptocurrency in the database.
     *
     * @param cryptoName the name of the cryptocurrency
     * @param price the new price to set
     * @throws PersistanceException if the update fails or the cryptocurrency is not found
     */
    public synchronized void updateCryptoPrice(String cryptoName, Double price)  throws PersistanceException{

        String query = "UPDATE cryptocurrency SET current_value = ? WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
            }

            stmt = conn.prepareStatement(query);
            stmt.setDouble(1, price);
            stmt.setString(2, cryptoName);

            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0){
                throw new DBConnectionNotReached(CRYPTO_FAILED);
            }
        } catch (SQLException e) {
            throw new DBDataNotFound(CRYPTO_FAILED);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
            }
        }
    }

    /**
     * Retrieves the current price of a cryptocurrency by name.
     *
     * @param cryptoName the name of the cryptocurrency
     * @return the current price
     * @throws PersistanceException if the cryptocurrency is not found or the database connection fails
     */
    public double getCryptoCurrentPrice(String cryptoName) throws PersistanceException {
        String query = "SELECT current_value FROM cryptocurrency WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
            }

            stmt = conn.prepareStatement(query);
            stmt.setString(1, cryptoName);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("current_value");
            } else {
                throw new DBDataNotFound("No se ha encontrado la cryptomoneda con el nombre: " + cryptoName);
            }
        } catch (SQLException e) {
            throw new DBDataNotFound(e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
            }
        }
    }

    /**
     * Deletes a cryptocurrency from the database by name.
     *
     * @param cryptoName the name of the cryptocurrency to delete
     * @throws PersistanceException if the deletion fails or the crypto is not found
     */
    public void deleteCrypto(String cryptoName) throws PersistanceException{

        String query = "DELETE FROM cryptocurrency WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try{
            conn = SQLConnector.getInstance().getConnection();
            if(conn == null){
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
            }
            stmt = conn.prepareStatement(query);
            stmt.setString(1, cryptoName);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DBDataNotFound(CRYPTO_FAILED);
            }
        } catch (SQLException e){
            throw new DBConnectionNotReached(CRYPTO_FAILED);
        } finally{
            if(stmt != null){
                try {
                    stmt.close();
                } catch (SQLException e){
                    throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
                }
            }
        }
    }

    /**
     * Retrieves all cryptocurrencies stored in the database.
     *
     * @return a list of Crypto objects
     * @throws PersistanceException if the retrieval fails
     */
    public List<Crypto> getAllCryptos()  throws PersistanceException{
        List<Crypto> cryptoList = new ArrayList<>();
        String query = "SELECT * FROM cryptocurrency";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
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
            throw new DBDataNotFound(CRYPTO_FAILED);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
            }
        }
        return cryptoList;
    }

    /**
     * Retrieves a cryptocurrency by its name.
     *
     * @param name the name of the cryptocurrency
     * @return the matching Crypto object
     * @throws PersistanceException if the crypto is not found or the query fails
     */
    public Crypto getCryptoByName(String name)  throws PersistanceException{

        String query = "SELECT * FROM cryptocurrency WHERE name = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Crypto crypto = null;

        try {
            conn = SQLConnector.getInstance().getConnection();
            if (conn == null) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
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
                throw new DBDataNotFound("No se ha encontrado la cryptomoneda con el nombre: " + name);
            }
        } catch (SQLException e) {
            throw new DBDataNotFound(CRYPTO_FAILED);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                throw new DBConnectionNotReached(DB_CONNECTION_FAILED);
            }
        }
        return crypto;
    }

    /**
     * Adds cryptocurrencies from a JSON file to the database.
     * Cryptocurrencies that already exist are skipped.
     *
     * @param file the JSON file to read from
     * @return a string log of how many and which cryptocurrencies were added
     * @throws PersistanceException if reading or inserting fails
     */
    public String addCryptosFromFile(File file) throws PersistanceException{
        List<Crypto> cryptos = new CryptoFileReadingJSONDAO().readCryptoFromFile(file);
        int cryptoCount = 0;
        StringBuilder log = new StringBuilder();
        StringBuilder error = new StringBuilder();
        for (Crypto crypto: cryptos) {
            try {
                getCryptoByName(crypto.getName());
                error.append("\t\t" + crypto.getName() + " ya existe.\n");
            }
            catch (DBDataNotFound _) {
                createCrypto(crypto);
                cryptoCount++;
            }
        }
        log.append("Se han añadido " + cryptoCount + " cryptos.\n");
        if (!error.isEmpty()) {
            log.append(error);
        }
        return log.toString();
    }

}
