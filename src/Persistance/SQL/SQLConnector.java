package Persistance.SQL;

import Persistance.ConfigurationDAO;
import Persistance.ConfigurationJSONDAO;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.*;

/**
 * The type Sql connector.
 */
public class SQLConnector {
    private static SQLConnector instance = null;
    private Connection conn;  // Instance field (removed static 'connection')

    private final String username;
    private final String password;
    private final String url;

    private SQLConnector(String username, String password, String ip, int port, String database) {
        this.username = username;
        this.password = password;
        this.url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
    }

    /**
     * Gets instance.
     *
     * @return the instance
     * @throws PersistanceException the persistance exception
     */
    public static SQLConnector getInstance() throws PersistanceException {
        if (instance == null) {
            ConfigurationDAO cDao = new ConfigurationJSONDAO();
            instance = new SQLConnector(cDao.getDBUser(), cDao.getDBPass(), cDao.getDBIP(), cDao.getDBPort(), cDao.getDBName());
            instance.connect();

        }
        return instance;
    }

    /**
     * Select query result set.
     *
     * @param query the query
     * @return the result set
     * @throws DBConnectionNotReached the db connection not reached
     */
    public ResultSet selectQuery(String query) throws DBConnectionNotReached{
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = getConnection();  // Ensure valid connection
            if (connection == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            return rs;  // Caller must close ResultSet and Statement
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to execute select query " + e.getMessage());
        }
    }

    /**
     * Connect.
     *
     * @throws DBConnectionNotReached the db connection not reached
     */
    public void connect() throws DBConnectionNotReached {
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            String message = "Couldn't connect to --> " + url + " (" + e.getMessage() + ")";
            throw new DBConnectionNotReached(message);
        }
    }

    /**
     * Gets connection.
     *
     * @return the connection
     * @throws DBConnectionNotReached the db connection not reached
     */
    public synchronized Connection getConnection() throws DBConnectionNotReached {
        try {
            if (conn == null || conn.isClosed()) {
                connect();  // Reconnect if connection is null or closed
            }
            return conn;
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to establish database connection");
        }
    }

    /**
     * Disconnect.
     *
     * @throws DBConnectionNotReached the db connection not reached
     */
    public void disconnect() throws DBConnectionNotReached {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Problem when closing the connection.");
        }
    }
}