package Persistance;

import java.io.*;

import Persistance.PersistanceExceptions.ConfigurationFileError;
import com.google.gson.*;

/**
 * Data Access Object (DAO) for managing the application's configuration using a JSON file.
 * This class handles reading from and writing to the "config.json" file that contains configuration parameters
 * such as database connection details and administrator password.
 */
public class ConfigurationJSONDAO implements ConfigurationDAO {

    private final String FILE_NAME = "data/config.json";
    private final String CONFIG_ERROR = "Brother, hay un error con el fichero de configuracion.";

    /**
     * Constructs a new ConfigurationJSONDAO and validates the existence of the configuration file.
     *
     * @throws ConfigurationFileError if the configuration file cannot be accessed or does not exist.
     */
    public ConfigurationJSONDAO() throws ConfigurationFileError {
        try {
            new FileReader(FILE_NAME);
        }catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    /**
     * Retrieves the administrator password from the configuration file.
     *
     * @return the administrator password as a String.
     * @throws ConfigurationFileError if the file cannot be read or parsed.
     */
    public String getAdminPass() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("admin_pass").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    /**
     * Updates the administrator password in the configuration file, preserving other configuration values.
     *
     * @param adminPass the new administrator password to be saved.
     * @throws ConfigurationFileError if the file cannot be written or there is a formatting issue.
     */
    public void setAdminPass(String adminPass) throws ConfigurationFileError {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("db_port", String.valueOf(getDBPort()));
        jsonObject.addProperty("db_ip", getDBIP());
        jsonObject.addProperty("db_name", getDBName());
        jsonObject.addProperty("db_user", getDBUser());
        jsonObject.addProperty("db_pass", getDBPass());
        jsonObject.addProperty("admin_pass", adminPass);
        jsonObject.addProperty("polling_interval", String.valueOf(getPollingInterval()));
        jsonObject.addProperty("maximum_data_points", String.valueOf(getMaximumDataPoints()));

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(jsonObject, writer);
        } catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR + e.getMessage());
        }
    }

    /**
     * Retrieves the database port number from the configuration file.
     *
     * @return the database port as an integer.
     * @throws ConfigurationFileError if the file cannot be read or parsed.
     */
    public int getDBPort() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_port").getAsInt();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    /**
     * Retrieves the database name from the configuration file.
     *
     * @return the database name as a String.
     * @throws ConfigurationFileError if the file cannot be read or parsed.
     */
    public String getDBName() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_name").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    /**
     * Retrieves the database username from the configuration file.
     *
     * @return the database username as a String.
     * @throws ConfigurationFileError if the file cannot be read or parsed.
     */
    public String getDBUser() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_user").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    /**
     * Retrieves the database password from the configuration file.
     *
     * @return the database password as a String.
     * @throws ConfigurationFileError if the file cannot be read or parsed.
     */
    public String getDBPass() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_pass").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    /**
     * Retrieves the IP address of the database server from the configuration file.
     *
     * @return the IP address as a String.
     * @throws ConfigurationFileError if the file cannot be read or parsed.
     */
    public String getDBIP() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_ip").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    /**
     * Retrieves the polling interval used for chart updates from the configuration file.
     *
     * @return the polling interval in seconds as a double.
     * @throws ConfigurationFileError if the file cannot be read or parsed.
     */
    public double getPollingInterval() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("polling_interval").getAsDouble();
        } catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    /**
     * Retrieves the maximum number of data points used in chart plotting from the configuration file.
     *
     * @return the maximum number of data points as an integer.
     * @throws ConfigurationFileError if the file cannot be read or parsed.
     */
    public int getMaximumDataPoints() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("maximum_data_points").getAsInt();
        } catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }
}
