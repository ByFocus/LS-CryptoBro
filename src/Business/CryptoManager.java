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
 */
public class CryptoManager{
    private static CryptoManager instance;

    /**
     * Instantiates a new Crypto manager.
     */
    public CryptoManager(){
    }

    /**
     * Gets crypto manager.
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
     * Gets all cryptos.
     *
     * @return the all cryptos
     * @throws BusinessExeption the business exeption
     */
    public List<Crypto> getAllCryptos()  throws BusinessExeption{
        try {
            CryptoDAO cryptoDA0 = new CryptoSQLDAO();
            return cryptoDA0.getAllCryptos();
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Gets crypto by name.
     *
     * @param cryptoName the crypto name
     * @return the crypto by name
     * @throws BusinessExeption the business exeption
     */
    public Crypto getCryptoByName(String cryptoName)  throws BusinessExeption{
        try {
            CryptoDAO cryptoDA0 =new CryptoSQLDAO();
            return cryptoDA0.getCryptoByName(cryptoName);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Bot make transaction.
     *
     * @param cryptoName the crypto name
     * @param buy        the buy
     */
    public synchronized void botMakeTransaction(String cryptoName, boolean buy){
        makeTransaction(cryptoName, buy? 1: -1);
        new WalletManager().notifyChangeInCryptoValue(cryptoName);
    }

    /**
     * Make transaction.
     *
     * @param cryptoName the crypto name
     * @param units      the units
     * @throws BusinessExeption the business exeption
     */
    public synchronized void makeTransaction(String cryptoName, int units) throws BusinessExeption {
        try {
            double priceMultiplier = (units>0? 1.01 : 0.99);
            CryptoDAO cryptoDA0 =new CryptoSQLDAO();
            double newPrice = cryptoDA0.getCryptoCurrentPrice(cryptoName)*priceMultiplier;
            cryptoDA0.updateCryptoPrice(cryptoName, newPrice);
            MarketManager.getMarketManager().notify(EventType.CRYPTO_VALUES_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Gets crypto current price.
     *
     * @param cryptoName the crypto name
     * @return the crypto current price
     * @throws BusinessExeption the business exeption
     */
    public double getCryptoCurrentPrice(String cryptoName) throws BusinessExeption {
        try {
            return new CryptoSQLDAO().getCryptoCurrentPrice(cryptoName);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Delete crypto.
     *
     * @param cryptoName the crypto name
     * @throws BusinessExeption the business exeption
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
     * Add crypto from files string.
     *
     * @param files the files
     * @return the string
     * @throws BusinessExeption the business exeption
     */
    public String addCryptoFromFiles(List<File> files)  throws BusinessExeption {
        try {
            CryptoDAO cryptoDAO = new CryptoSQLDAO();
            StringBuilder log = new StringBuilder();
            for (int i = 0; i < files.size(); i++) {
                log.append("\nFile #" + (i + 1) + ": " + files.get(i).getName()+"\n");
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
     * Get all crypto names string [ ].
     *
     * @return the string [ ]
     * @throws BusinessExeption the business exeption
     */
    public String[] getAllCryptoNames() throws BusinessExeption{
        List<Crypto> cryptos = getAllCryptos();
        String[] cryptoNames = new String[cryptos.size()];
        for (int i = 0; i < cryptos.size(); i++) {
            cryptoNames[i] = cryptos.get(i).getName();
        }
        return cryptoNames;
    }


    /**
     * Update crypto price.
     *
     * @param cryptoName the crypto name
     * @param price      the price
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
