package Persistance;

import java.io.IOException;

import Persistance.PersistanceExceptions.ConfigurationFileError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;

public class ConfigurationJSONDAO implements ConfigurationDAO {

    private final String FILE_NAME = "config.json";
    private final String CONFIG_ERROR = "Brother, hay un error con el fichero de configuracion.";

    private String adminPass;
    private String dbPort;
    private String dbName;
    private String dbUser;
    private String dbPass;
    private String dbIP;

    public ConfigurationJSONDAO() throws ConfigurationFileError {
        //loadConfiguration();
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
        JsonParser parser = new JsonParser();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            JsonObject jsonObject = parser.parse(reader).getAsJsonObject();
            this.adminPass = jsonObject.get("admin_pass").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }

        return adminPass;
    }

    public String getDBPort() throws ConfigurationFileError {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            JsonObject jsonObject = parser.parse(reader).getAsJsonObject();
            this.dbPort = jsonObject.get("admin_pass").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
        return dbPort;
    }

    public String getDBName() throws ConfigurationFileError {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            JsonObject jsonObject = parser.parse(reader).getAsJsonObject();
            this.dbName = jsonObject.get("admin_pass").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }

        return dbName;
    }

    public String getDBUser() throws ConfigurationFileError {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            JsonObject jsonObject = parser.parse(reader).getAsJsonObject();
            this.dbUser = jsonObject.get("admin_pass").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
        return dbUser;
    }

    public String getDBPass() throws ConfigurationFileError {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            JsonObject jsonObject = parser.parse(reader).getAsJsonObject();
            this.dbPass = jsonObject.get("admin_pass").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }
        return dbPass;
    }

    public String getDBIP() throws ConfigurationFileError {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            JsonObject jsonObject = parser.parse(reader).getAsJsonObject();
            this.dbIP = jsonObject.get("admin_pass").getAsString();
        }
        catch (IOException e) {
            throw new ConfigurationFileError(CONFIG_ERROR);
        }

        return dbIP;
    }
}


