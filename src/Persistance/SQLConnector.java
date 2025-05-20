package Persistance;

import Business.BusinessExceptions.DataPersistanceError;
import Persistance.PersistanceExceptions.DBConnectionNotReached;
import Persistance.PersistanceExceptions.PersistanceException;

import java.sql.*;

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

    public static SQLConnector getInstance() throws PersistanceException {
        if (instance == null) {
            ConfigurationDAO cDao = new ConfigurationJSONDAO();
            instance = new SQLConnector(cDao.getDBUser(), cDao.getDBPass(), cDao.getDBIP(), cDao.getDBPort(), cDao.getDBName());
            instance.connect();
        }
        return instance;
    }
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

    public void connect() throws DBConnectionNotReached {
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            String message = "Couldn't connect to --> " + url + " (" + e.getMessage() + ")";
            throw new DBConnectionNotReached(message);
        }
    }

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