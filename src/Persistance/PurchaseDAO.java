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
     * Add purchase.
     *
     * @param user     the user
     * @param purchase the purchase
     * @throws PersistanceException the persistance exception
     */
    void addPurchase(User user, Purchase purchase)  throws PersistanceException;

    /**
     * Gets usernames by crypto name.
     *
     * @param cryptoName the crypto name
     * @return the usernames by crypto name
     * @throws PersistanceException the persistance exception
     */
    List<String> getUsernamesByCryptoName(String cryptoName) throws PersistanceException;

    /**
     * Gets purchases by user name.
     *
     * @param user the user
     * @return the purchases by user name
     * @throws PersistanceException the persistance exception
     */
    List<Purchase> getPurchasesByUserName(String user) throws PersistanceException;

    /**
     * Substract units.
     *
     * @param purchase         the purchase
     * @param username         the username
     * @param unitsToSubtract the units to substract
     * @throws PersistanceException the persistance exception
     */
    void subtractUnits(Purchase purchase, String username, int unitsToSubtract) throws PersistanceException;

    /**
     * Sell all purchases from crypto double.
     *
     * @param cryptoName the crypto name
     * @param userName   the user name
     * @return the double
     * @throws PersistanceException the persistance exception
     */
    double sellAllPurchasesFromCrypto(String cryptoName, String userName) throws PersistanceException;

    void deletePurchasesFromUser(String identifier) throws PersistanceException;
}