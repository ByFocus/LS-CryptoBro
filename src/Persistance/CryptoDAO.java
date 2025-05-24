package Persistance;

import Business.Entities.Crypto;
import Persistance.PersistanceExceptions.ConfigurationFileError;
import Persistance.PersistanceExceptions.PersistanceException;

import java.io.File;
import java.util.List;

/**
 * The interface Crypto dao is a Data Access Object (DAO) interface that defines methods for
 *  performing persistence operations on {@link Crypto} entities.
 */
public interface CryptoDAO {

    /**
     * Persists a new cryptocurrency into the data store.
     *
     * @param crypto the {@link Crypto} entity to create.
     * @throws PersistanceException if an error occurs while saving the crypto.
     */
    void createCrypto(Crypto crypto) throws PersistanceException;

    /**
     * Retrieves all available cryptocurrencies from the data store.
     *
     * @return a list of {@link Crypto} objects.
     * @throws PersistanceException if the data could not be fetched.
     */
    List<Crypto> getAllCryptos() throws PersistanceException;

    /**
     * Retrieves a specific {@link Crypto} based on its name.
     *
     * @param name the name of the cryptocurrency to fetch.
     * @return the {@link Crypto} object with the given name.
     * @throws PersistanceException if the crypto does not exist or can't be retrieved.
     */
    Crypto getCryptoByName(String name) throws PersistanceException;

    /**
     * Deletes a cryptocurrency entry from the data store.
     *
     * @param cryptoName the name of the crypto to delete.
     * @throws PersistanceException if the deletion fails or the crypto doesn't exist.
     */
    void deleteCrypto(String cryptoName) throws PersistanceException;

    /**
     * Retrieves the current price of a specific cryptocurrency.
     *
     * @param cryptoName the name of the cryptocurrency.
     * @return the current price as a {@code double}.
     * @throws PersistanceException if the price cannot be retrieved.
     */
    double getCryptoCurrentPrice(String cryptoName) throws PersistanceException;


    /**
     * Updates the price of an existing cryptocurrency.
     *
     * @param cryptoName the name of the crypto to update.
     * @param price the new price to set.
     * @throws PersistanceException if the update fails or the crypto is not found.
     */
    void updateCryptoPrice(String cryptoName, Double price)  throws PersistanceException;

    /**
     * Imports and registers multiple cryptocurrencies from a provided file.
     *
     * @param file the file containing crypto data.
     * @return a status {@code String} or summary of the operation.
     * @throws PersistanceException if the file is invalid or the import fails.
     */
    String addCryptosFromFile(File file) throws PersistanceException;
}