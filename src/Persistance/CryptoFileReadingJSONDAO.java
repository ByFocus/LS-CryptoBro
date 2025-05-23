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

/**
 * The type Crypto file reading jsondao.
 */
public class CryptoFileReadingJSONDAO implements CryptoFileReadingDAO{
    /**
     * Instantiates a new Crypto file reading jsondao.
     */
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
                        Crypto crypto = parseCryptoFromJson(element.getAsJsonObject());
                        cryptoList.add(crypto);
                    }
                } else if (root.isJsonObject()) {
                    Crypto crypto = parseCryptoFromJson(root.getAsJsonObject());
                    cryptoList.add(crypto);
                } else {
                    throw new FileTypeException("Este fichero está vacío :0");
                }

            } catch (IOException e) {
                throw new FileTypeException("No puedo abrir esto, tio: " + file.getName());
            }
        } else {
            throw new FileTypeException("El fichero debe ser JSON, ¡esfuerzáte más BRO!");
        }
        return cryptoList;
    }

    private Crypto parseCryptoFromJson(JsonObject obj) throws FileTypeException {
        String name = obj.has("name") ? obj.get("name").getAsString() : null;
        String category = obj.has("category") ? obj.get("category").getAsString() : null;

        if (name == null || category == null) {
            throw new FileTypeException("Los tipos del dato no son correctos");
        }

        // Parse initialPrice
        double initialPrice = 0;
        if (obj.has("initialPrice") && !obj.get("initialPrice").isJsonNull()) {
            try {
                initialPrice = obj.get("initialPrice").getAsDouble();
            } catch (Exception e) {
                throw new FileTypeException("Los tipos del dato no son correctos");
            }
        }

        // Parse currentPrice (Puede estar vacío)
        double currentPrice;
        if (obj.has("currentPrice") && !obj.get("currentPrice").isJsonNull() && !obj.get("current-price").getAsString().isEmpty()) {
            try {
                currentPrice = obj.get("currentPrice").getAsDouble();
            } catch (Exception e) {
                throw new FileTypeException("Los tipos del dato no son correctos");
            }
        } else {
            currentPrice = initialPrice;
        }

        // Parse volatility
        int volatility = 0;
        if (obj.has("volatility") && !obj.get("volatility").isJsonNull()) {
            try {
                volatility = obj.get("volatility").getAsInt();
            } catch (Exception e) {
                throw new FileTypeException("Los tipos del dato no son correctos");
            }
        }

        // Validation
        if (currentPrice <= 0 || initialPrice <= 0) {
            throw new FileTypeException("Los precios tienen que ser mayores a 0, melonª");
        }

        return new Crypto(name, category, currentPrice, initialPrice, volatility);
    }

}
