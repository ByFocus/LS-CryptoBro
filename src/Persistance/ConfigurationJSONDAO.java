package Persistance;

import java.io.FileNotFoundException;
import java.io.IOException;

import Persistance.PersistanceExceptions.ConfigurationFileError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;

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
            FileReader reader = new FileReader(FILE_NAME);
        }catch (IOException e) {
            //throw new ConfigurationFileError(CONFIG_ERROR);
        }
     }

    /*private void loadConfiguration() throws ConfigurationFileError {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            JsonObject jsonObject = parser.parse(reader).getAsJsonObject();

            this.dbPort = jsonObject.get("db_port").getAsString();
            this.dbIP = jsonObject.get("db_ip").getAsString();
            this.dbName = jsonObject.get("db_name").getAsString();
            this.dbUser = jsonObject.get("db_user").getAsString();
            this.dbPass = jsonObject.get("db_pass").getAsString();
            this.adminPass = jsonObject.get("admin_pass").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }*/

    public String getAdminPass() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("admin_pass").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public int getDBPort() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_port").getAsInt();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public String getDBName() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_name").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public String getDBUser() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_user").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public String getDBPass() throws ConfigurationFileError {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_pass").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }

    public String getDBIP() throws ConfigurationFileError {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            return gson.fromJson(reader, JsonObject.class).get("db_ip").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
    }
}


