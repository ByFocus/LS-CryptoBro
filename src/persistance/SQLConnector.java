package persistance;

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

    public static SQLConnector getInstance() {
        if (instance == null) {
            instance = new SQLConnector("root", "", "localhost", 3308, "cryptobro_db");
            instance.connect();
        }
        return instance;
    }
    public ResultSet selectQuery(String query) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = getConnection();  // Ensure valid connection
            if (connection == null) {
                throw new SQLException("Database connection is null");
            }
            stmt = connection.createStatement();
            rs = stmt.executeQuery(query);
            return rs;  // Caller must close ResultSet and Statement
        } catch (SQLException e) {
            System.err.println("Query: " + query);
            System.err.println("Error selecting data --> " + e.getSQLState() + " (" + e.getMessage() + ")");
            throw new RuntimeException("Failed to execute select query", e);
        }
        // Note: No finally block to close stmt here, as it would close the ResultSet prematurely
    }

    public void connect() {
        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Successfully connected to database: " + url);
        } catch (SQLException e) {
            System.err.println("Couldn't connect to --> " + url + " (" + e.getMessage() + ")");
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    public Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                connect();  // Reconnect if connection is null or closed
            }
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to validate database connection", e);
        }
    }

    public void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Problem when closing the connection --> " + e.getSQLState() + " (" + e.getMessage() + ")");
        }
    }
}