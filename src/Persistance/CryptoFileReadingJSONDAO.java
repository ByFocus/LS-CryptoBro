package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.FileTypeException;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO implementation for reading cryptocurrency data from a JSON file.
 * This class parses .json files and converts them into a list of {@link Crypto} objects.
 */
public class CryptoFileReadingJSONDAO implements CryptoFileReadingDAO {

    /**
     * Default constructor.
     */
    public CryptoFileReadingJSONDAO() {}

    final static String NAME_FIELD = "name";
    final static String CATEGORY_FIELD = "category";
    final static String INITIAL_PRICE_FIELD = "initialPrice";
    final static String CURRENT_PRICE_FIELD = "currentPrice";
    final static String VOLATILITY_FIELD = "volatility";

    final static String ERROR_NEGATIVE_PRICES = "Los precios tienen que ser mayores a 0, melón";
    final static String EMPTY_FILE = "Este fichero está vacío :0";
    final static String ERROR_OPENING = "No puedo abrir esto, tio: ";
    final static String ERROR_NOT_A_JSON = "El fichero debe ser JSON, ¡esfuérzate más BRO!";
    final static String ERROR_DATA_TYPE = "El formato del JSON es erróneo. Revisa que los campos obligatorios no sean nulos,\ny aseguráte de seguir formato del fichero de ejemplo";

    /**
     * Reads cryptocurrency data from a given JSON file and parses it into a list of {@link Crypto} objects.
     *
     * @param file the JSON file to read from
     * @return a list of parsed {@link Crypto} instances
     * @throws FileTypeException if the file is not a valid JSON, has syntax errors, is empty, or contains invalid data
     */
    public List<Crypto> readCryptoFromFile(File file) throws FileTypeException {
        List<Crypto> cryptoList = new ArrayList<>();
        if (file.getName().toLowerCase().endsWith(".json")) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(file)) {
                JsonElement root;
                try {
                    root = gson.fromJson(reader, JsonElement.class);
                } catch (JsonSyntaxException e) {
                    throw new FileTypeException(e.getMessage());
                }

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
                    throw new FileTypeException(EMPTY_FILE);
                }

            } catch (IOException e) {
                throw new FileTypeException(ERROR_OPENING + file.getName());
            }
        } else {
            throw new FileTypeException(ERROR_NOT_A_JSON);
        }
        return cryptoList;
    }

    /**
     * Parses a single {@link JsonObject} into a {@link Crypto} object.
     *
     * @param obj the JSON object containing the cryptocurrency data
     * @return a {@link Crypto} instance
     * @throws FileTypeException if required fields are missing or contain invalid values
     */
    private Crypto parseCryptoFromJson(JsonObject obj) throws FileTypeException {
        try {
            String name = obj.has(NAME_FIELD) && !obj.get(NAME_FIELD).isJsonNull() ? obj.get(NAME_FIELD).getAsString() : null;
            String category = obj.has(CATEGORY_FIELD) && !obj.get(CATEGORY_FIELD).isJsonNull() ? obj.get(CATEGORY_FIELD).getAsString() : null;
            Double initialPrice = obj.has(INITIAL_PRICE_FIELD) && !obj.get(INITIAL_PRICE_FIELD).isJsonNull() ? obj.get(INITIAL_PRICE_FIELD).getAsDouble() : null;
            Integer volatility = obj.has(VOLATILITY_FIELD) && !obj.get(VOLATILITY_FIELD).isJsonNull() ? obj.get(VOLATILITY_FIELD).getAsInt() : null;

            if (name == null || category == null || initialPrice == null || volatility == null) {
                throw new FileTypeException(ERROR_DATA_TYPE);
            }

            double currentPrice = initialPrice;
            if (obj.has(CURRENT_PRICE_FIELD) && !obj.get(CURRENT_PRICE_FIELD).isJsonNull()) {
                String currentPriceStr = obj.get(CURRENT_PRICE_FIELD).getAsString();
                if (!currentPriceStr.isEmpty()) {
                    currentPrice = Double.parseDouble(currentPriceStr);
                }
            }

            if (currentPrice <= 0 || initialPrice <= 0) {
                throw new FileTypeException(ERROR_NEGATIVE_PRICES);
            }

            return new Crypto(name, category, currentPrice, initialPrice, volatility);

        } catch (Exception e) {
            throw new FileTypeException(ERROR_DATA_TYPE);
        }
    }

}
