package Persistance;

import Persistance.PersistanceExceptions.ConfigurationFileError;

/**
 * Is an interface responsible for accessing and managing
 * application configuration data, such as database connection details and system settings.
 */
public interface ConfigurationDAO {

    /**
     * Retrieves the administrator password from the configuration.
     *
     * @return the admin password as a {@code String}
     * @throws ConfigurationFileError if there is an issue reading the configuration file
     */
    String getAdminPass() throws ConfigurationFileError;

    /**
     * Retrieves the database port defined in the configuration.
     *
     * @return the port number as an {@code int}
     * @throws ConfigurationFileError if the configuration file cannot be read
     */
    int getDBPort() throws ConfigurationFileError;

    /**
     * Retrieves the name of the database from the configuration.
     *
     * @return the database name as a {@code String}
     * @throws ConfigurationFileError if the configuration cannot be accessed
     */
    String getDBName() throws ConfigurationFileError;

    /**
     * Retrieves the username for the database connection.
     *
     * @return the database username as a {@code String}
     * @throws ConfigurationFileError if an error occurs while accessing the configuration
     */
    String getDBUser() throws ConfigurationFileError;

    /**
     * Retrieves the password for the database connection.
     *
     * @return the database password as a {@code String}
     * @throws ConfigurationFileError if the password cannot be read from the configuration
     */
    String getDBPass() throws ConfigurationFileError;

    /**
     * Retrieves the IP address of the database server.
     *
     * @return the database IP address as a {@code String}
     * @throws ConfigurationFileError if the configuration file is unreadable or invalid
     */
    String getDBIP() throws ConfigurationFileError;

    /**
     * Retrieves the polling interval used for tasks like refreshing crypto prices.
     *
     * @return the polling interval in seconds as a {@code double}
     * @throws ConfigurationFileError if the interval cannot be parsed or retrieved
     */
    double getPollingInterval() throws ConfigurationFileError;

    /**
     * Retrieves the maximum number of data points to retain (e.g., for graph history).
     *
     * @return the maximum number of data points as an {@code int}
     * @throws ConfigurationFileError if the configuration is corrupted or unreadable
     */
    int getMaximumDataPoints() throws ConfigurationFileError;

    /**
     * Updates the administrator password in the configuration file.
     *
     * @param adminPass the new admin password to be set
     * @throws ConfigurationFileError if the new password cannot be saved
     */
    void setAdminPass(String adminPass) throws ConfigurationFileError;
}
