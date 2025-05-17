package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.FileTypeException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CryptoFileReadingJSONDAO implements CryptoFileReadingDAO{
    public CryptoFileReadingJSONDAO() {}

    public List<Crypto> readCryptoFromFile(File file) throws FileTypeException {
        List<Crypto> cryptoList = new ArrayList<>();
        if (file.getName().toLowerCase().endsWith(".json")) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(file)) {
                JsonElement root = gson.fromJson(reader, JsonElement.class);

                if (root.isJsonArray()) {
                    JsonArray objects = root.getAsJsonArray();
                    for (JsonElement element : objects) {
                        cryptoList.add(gson.fromJson(element, Crypto.class));
                    }
                } else if (root.isJsonObject()) {
                    cryptoList.add(gson.fromJson(root, Crypto.class));
                } else {
                    throw new FileTypeException("JSON file must contain an object or an array of objects.");
                }

            } catch (IOException e) {
                throw new FileTypeException("No puedo abrir esto, tio: " + file.getName());
            }
        } else {
            throw new FileTypeException("El fichero debe ser JSON, ¡esfuerzáte más BRO!");
        }
        return cryptoList;
    }

}
