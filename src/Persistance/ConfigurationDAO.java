package Persistance;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;

public class ConfigurationDAO {

    private static final String FILE_NAME = "config.json"; // atributo privado, como en el diagrama

    private String adminPass;
    private String dbPort;
    private String dbName;
    private String dbUser;
    private String dbPass;
    private String dbIP;

    public ConfigurationDAO() throws IOException {
        loadConfiguration();
    }

    private void loadConfiguration() throws IOException {
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
    }

    // Métodos públicos exactos del diagrama:

    public String getAdminPass() {
        return adminPass;
    }

    public String getDBPort() {
        return dbPort;
    }

    public String getDBName() {
        return dbName;
    }

    public String getDBUser() {
        return dbUser;
    }

    public String getDBPass() {
        return dbPass;
    }

    public String getDBIP() {
        return dbIP;
    }
}


