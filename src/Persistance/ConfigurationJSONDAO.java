package Persistance;

import java.io.*;

import Persistance.PersistanceExceptions.ConfigurationFileError;
import com.google.gson.*;

/**
 * The type Configuration jsondao.
 */
public class ConfigurationJSONDAO implements ConfigurationDAO {

    private final String FILE_NAME = "data/config.json";
    private final String CONFIG_ERROR = "Brother, hay un error con el fichero de configuracion.";

    /**
     * Instantiates a new Configuration jsondao.
     *
     * @throws ConfigurationFileError the configuration file error
     */
    public ConfigurationJSONDAO() throws ConfigurationFileError {
        try {
            new FileReader(FILE_NAME);
        }catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
     }

    public String getAdminPass() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("admin_pass").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

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

    public int getDBPort() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_port").getAsInt();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public String getDBName() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_name").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public String getDBUser() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_user").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public String getDBPass() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_pass").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public String getDBIP() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_ip").getAsString();
        }
        catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public double getPollingInterval() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("polling_interval").getAsDouble();
        } catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public int getMaximumDataPoints() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("maximum_data_points").getAsInt();
        } catch (Exception e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }
}


