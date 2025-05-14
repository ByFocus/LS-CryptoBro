package Persistance;

import Persistance.PersistanceExceptions.ConfigurationFileError;

public interface ConfigurationDAO {

    public String getAdminPass() throws ConfigurationFileError;

    public int getDBPort() throws ConfigurationFileError;

    public String getDBName() throws ConfigurationFileError;

    public String getDBUser() throws ConfigurationFileError;

    public String getDBPass() throws ConfigurationFileError;

    public String getDBIP() throws ConfigurationFileError;
}
