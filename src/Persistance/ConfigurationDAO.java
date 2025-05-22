package Persistance;

import Persistance.PersistanceExceptions.ConfigurationFileError;

/**
 * The interface Configuration dao.
 */
public interface ConfigurationDAO {

    /**
     * Gets admin pass.
     *
     * @return the admin pass
     * @throws ConfigurationFileError the configuration file error
     */
    String getAdminPass() throws ConfigurationFileError;

    /**
     * Gets db port.
     *
     * @return the db port
     * @throws ConfigurationFileError the configuration file error
     */
    int getDBPort() throws ConfigurationFileError;

    /**
     * Gets db name.
     *
     * @return the db name
     * @throws ConfigurationFileError the configuration file error
     */
    String getDBName() throws ConfigurationFileError;

    /**
     * Gets db user.
     *
     * @return the db user
     * @throws ConfigurationFileError the configuration file error
     */
    String getDBUser() throws ConfigurationFileError;

    /**
     * Gets db pass.
     *
     * @return the db pass
     * @throws ConfigurationFileError the configuration file error
     */
    String getDBPass() throws ConfigurationFileError;

    /**
     * Gets dbip.
     *
     * @return the dbip
     * @throws ConfigurationFileError the configuration file error
     */
    String getDBIP() throws ConfigurationFileError;

    double getPollingInterval() throws ConfigurationFileError;

    int getMaximumDataPoints() throws ConfigurationFileError;

    void setAdminPass(String adminPass) throws ConfigurationFileError;
}
