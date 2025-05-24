package Persistance.SQL;

import Persistance.ConfigurationDAO;
import Persistance.ConfigurationJSONDAO;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.*;

/**
 * Manages the connection to the SQL database.
 * Implements a singleton pattern to provide a shared connection instance throughout the application.
 */
public class SQLConnector {
    private static SQLConnector instance = null;
    private Connection conn;  // Instance field

    private final String username;
    private final String password;
    private final String url;

    /**
     * Private constructor to initialize connection parameters.
     *
     * @param username the database username
     * @param password the database password
     * @param ip the IP address of the database server
     * @param port the port number
     * @param database the database name
     */
    private SQLConnector(String username, String password, String ip, int port, String database) {
        this.username = username;
        this.password = password;
        this.url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
    }

    /**
     * Returns the singleton instance of the SQLConnector.
     * Initializes and connects if not already done.
     *
     * @return the instance
     * @throws PersistanceException if configuration cannot be read or connection fails
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
     * Executes a SELECT SQL query and returns the ResultSet.
     * Caller is responsible for closing the ResultSet and Statement.
     *
     * @param query the SQL SELECT query
     * @return the result set of the query
     * @throws DBConnectionNotReached if query execution or connection fails
     */
    public ResultSet selectQuery(String query) throws DBConnectionNotReached {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = getConnection();
            if (connection == null) {
                throw new DBConnectionNotReached("Database connection is null");
            }
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to execute select query " + e.getMessage());
        }
    }

    /**
     * Establishes a connection to the database using the configured URL, username, and password.
     *
     * @throws DBConnectionNotReached if the connection cannot be established
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
     * Returns the current active connection.
     * Automatically reconnects if the connection is null or closed.
     *
     * @return the connection
     * @throws DBConnectionNotReached if reconnection fails
     */
    public synchronized Connection getConnection() throws DBConnectionNotReached {
        try {
            if (conn == null || conn.isClosed()) {
                connect();
            }
            return conn;
        } catch (SQLException e) {
            throw new DBConnectionNotReached("Failed to establish database connection");
        }
    }

    /**
     * Closes the current database connection, if open.
     *
     * @throws DBConnectionNotReached if closing the connection fails
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
