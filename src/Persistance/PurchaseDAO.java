package Persistance;

import Business.Entities.Purchase;
import Business.Entities.User;
import Persistance.PersistanceExceptions.PersistanceException;

import java.util.List;

/**
 * The interface Purchase dao.
 */
public interface PurchaseDAO {
    /**
     * Adds a purchase from a user to the database.
     *
     * @param user     user that did the pruchase
     * @param purchase the purchase to add
     * @throws PersistanceException when failed to acces or modify the database
     */
    void addPurchase(User user, Purchase purchase)  throws PersistanceException;

    /**
     * Gets all the usernames that bought a certain crypt
     *
     * @param cryptoName the crypto name
     * @return the usernames of the users that purchased the crypto
     * @throws PersistanceException when failed to acces or modify the database
     */
    List<String> getUsernamesByCryptoName(String cryptoName) throws PersistanceException;

    /**
     * Gets all purchases by user name.
     *
     * @param user the user
     * @return the purchases by user name
     * @throws PersistanceException when failed to acces or modify the database
     */
    List<Purchase> getPurchasesByUserName(String user) throws PersistanceException;

    /**
     * Substracts a certain amount of units from a purchase, if it reaches 0 it is deleted.
     *
     * @param purchase         the purchase
     * @param username         the username
     * @param unitsToSubtract the units to substract
     * @throws PersistanceException when failed to acces or modify the database
     */
    void subtractUnits(Purchase purchase, String username, int unitsToSubtract) throws PersistanceException;

    /**
     * Deletes all the purchases of a crypto from a user and calculates the benefits of selling them.
     *
     * @param cryptoName the crypto name
     * @param userName   the user name
     * @return the benefits of selling the cryptp
     * @throws PersistanceException when failed to acces or modify the database
     */
    double sellAllPurchasesFromCrypto(String cryptoName, String userName) throws PersistanceException;

    /**
     * Deletes all the purchases from a user, doesn't return any value
     *
     * @param identifier String that contains the username of the user
     * @throws PersistanceException when failed to acces or modify the database
     */
    void deletePurchasesFromUser(String identifier) throws PersistanceException;
}