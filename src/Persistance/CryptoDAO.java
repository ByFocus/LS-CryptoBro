package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.ConfigurationFileError;
import Persistance.PersistanceExceptions.PersistanceException;

import java.io.File;
import java.util.List;

/**
 * The interface Crypto dao.
 */
public interface CryptoDAO {
    /**
     * Create crypto.
     *
     * @param crypto the crypto
     * @throws PersistanceException the persistance exception
     */
    void createCrypto(Crypto crypto) throws PersistanceException;

    /**
     * Update crypto.
     *
     * @param crypto the crypto
     * @throws PersistanceException the persistance exception
     */
    void updateCrypto(Crypto crypto) throws PersistanceException;

    /**
     * Gets all cryptos.
     *
     * @return the all cryptos
     * @throws PersistanceException the persistance exception
     */
    List<Crypto> getAllCryptos() throws PersistanceException;

    /**
     * Gets crypto by name.
     *
     * @param name the name
     * @return the crypto by name
     * @throws PersistanceException the persistance exception
     */
    Crypto getCryptoByName(String name) throws PersistanceException;

    /**
     * Delete crypto.
     *
     * @param cryptoname the cryptoname
     * @throws PersistanceException the persistance exception
     */
    void deleteCrypto(String cryptoname) throws PersistanceException;

    /**
     * Gets crypto current price.
     *
     * @param cryptoName the crypto name
     * @return the crypto current price
     * @throws PersistanceException the persistance exception
     */
    double getCryptoCurrentPrice(String cryptoName) throws PersistanceException;

    /**
     * Gets crypto by category.
     *
     * @param category the category
     * @return the crypto by category
     * @throws PersistanceException the persistance exception
     */
    List<Crypto> getCryptoByCategory (String category) throws PersistanceException;

    /**
     * Gets categories.
     *
     * @return the categories
     * @throws PersistanceException the persistance exception
     */
    List<String> getCategories () throws PersistanceException;

    /**
     * Update crypto price.
     *
     * @param cryptoName the crypto name
     * @param price      the price
     * @throws PersistanceException the persistance exception
     */
    void updateCryptoPrice(String cryptoName, Double price)  throws PersistanceException;

    /**
     * Add cryptos from file string.
     *
     * @param file the file
     * @return the string
     * @throws PersistanceException the persistance exception
     */
    String addCryptosFromFile(File file) throws PersistanceException;
}