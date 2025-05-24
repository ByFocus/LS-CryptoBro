package Business;

import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.DataPersistanceError;
import Business.Entities.Crypto;
import Persistance.CryptoDAO;
import Persistance.SQL.CryptoSQLDAO;
import Persistance.PersistanceExceptions.PersistanceException;

import java.io.File;
import java.util.List;

/**
 * The type Crypto manager.
 * Handles all business logic related to cryptocurrency operations,
 * including price updates, transactions, and file imports.
 */
public class CryptoManager {
    private static CryptoManager instance;

    /**
     * Instantiates a new Crypto manager.
     */
    public CryptoManager() {}

    /**
     * Gets the singleton instance of CryptoManager.
     *
     * @return the crypto manager
     */
    public static CryptoManager getCryptoManager() {
        if (instance == null) {
            instance = new CryptoManager();
        }
        return instance;
    }

    /**
     * Retrieves all cryptocurrencies from the persistence layer.
     *
     * @return the list of all cryptocurrencies
     * @throws BusinessExeption if a data access error occurs
     */
    public List<Crypto> getAllCryptos() throws BusinessExeption {
        try {
            CryptoDAO cryptoDA0 = new CryptoSQLDAO();
            return cryptoDA0.getAllCryptos();
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Retrieves a cryptocurrency by name.
     *
     * @param cryptoName the crypto name
     * @return the crypto object
     * @throws BusinessExeption if the crypto is not found or access fails
     */
    public Crypto getCryptoByName(String cryptoName) throws BusinessExeption {
        try {
            CryptoDAO cryptoDA0 = new CryptoSQLDAO();
            return cryptoDA0.getCryptoByName(cryptoName);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Performs a bot transaction with a unit change of +1 or -1.
     *
     * @param cryptoName the crypto name
     * @param buy        whether to buy (true) or sell (false)
     */
    public synchronized void botMakeTransaction(String cryptoName, boolean buy) {
        makeTransaction(cryptoName, buy ? 1 : -1);
        new WalletManager().notifyChangeInCryptoValue(cryptoName);
    }

    /**
     * Performs a transaction affecting the crypto's current price.
     *
     * @param cryptoName the crypto name
     * @param units      number of units (positive for buy, negative for sell)
     * @throws BusinessExeption if data access fails
     */
    public synchronized void makeTransaction(String cryptoName, int units) throws BusinessExeption {
        try {
            double priceMultiplier = (units > 0 ? 1.01 : 0.99);
            CryptoDAO cryptoDA0 = new CryptoSQLDAO();
            double newPrice = cryptoDA0.getCryptoCurrentPrice(cryptoName) * priceMultiplier;
            cryptoDA0.updateCryptoPrice(cryptoName, newPrice);
            MarketManager.getMarketManager().notify(EventType.CRYPTO_VALUES_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Gets the current price of a given cryptocurrency.
     *
     * @param cryptoName the crypto name
     * @return the current price
     * @throws BusinessExeption if retrieval fails
     */
    public double getCryptoCurrentPrice(String cryptoName) throws BusinessExeption {
        try {
            return new CryptoSQLDAO().getCryptoCurrentPrice(cryptoName);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Deletes a cryptocurrency and notifies affected users.
     *
     * @param cryptoName the crypto name
     * @throws BusinessExeption if deletion fails
     */
    public void deleteCrypto(String cryptoName) throws BusinessExeption {
        try {
            new WalletManager().warnUsersCryptoDeleted(cryptoName);
            CryptoDAO cryptoDA0 = new CryptoSQLDAO();
            cryptoDA0.deleteCrypto(cryptoName);
            MarketManager.getMarketManager().notify(EventType.CRYPTO_VALUES_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Adds cryptocurrencies from a list of JSON files.
     *
     * @param files the list of files
     * @return a log string describing the import process
     * @throws BusinessExeption if reading or importing fails
     */
    public String addCryptoFromFiles(List<File> files) throws BusinessExeption {
        try {
            CryptoDAO cryptoDAO = new CryptoSQLDAO();
            StringBuilder log = new StringBuilder();
            for (int i = 0; i < files.size(); i++) {
                log.append("\nFile #" + (i + 1) + ": " + files.get(i).getName() + "\n");
                try {
                    log.append(cryptoDAO.addCryptosFromFile(files.get(i)));
                } catch (PersistanceException err) {
                    log.append(err.getMessage());
                }
                log.append("\n");
            }
            MarketManager.getMarketManager().notify(EventType.CRYPTO_VALUES_CHANGED);
            return log.toString();
        } catch (Exception e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Retrieves all crypto names as a string array.
     *
     * @return the array of crypto names
     * @throws BusinessExeption if data retrieval fails
     */
    public String[] getAllCryptoNames() throws BusinessExeption {
        List<Crypto> cryptos = getAllCryptos();
        String[] cryptoNames = new String[cryptos.size()];
        for (int i = 0; i < cryptos.size(); i++) {
            cryptoNames[i] = cryptos.get(i).getName();
        }
        return cryptoNames;
    }

    /**
     * Updates the price of a given cryptocurrency.
     *
     * @param cryptoName the crypto name
     * @param price      the new price
     */
    public void updateCryptoPrice(String cryptoName, double price) {
        try {
            CryptoDAO cryptoDA0 = new CryptoSQLDAO();
            cryptoDA0.updateCryptoPrice(cryptoName, price);
            MarketManager.getMarketManager().notify(EventType.CRYPTO_VALUES_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }
}
