package Persistance;

import Business.Entities.User;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

/**
 * The interface User dao.
 */
public interface UserDAO {
    /**
     * Register user.
     *
     * @param user the user
     * @throws PersistanceException the persistance exception
     */
    void registerUser(User user) throws PersistanceException;

    /**
     * Gets user by username or email.
     *
     * @param value the value
     * @return the user by username or email
     * @throws PersistanceException the persistance exception
     */
    User getUserByUsernameOrEmail(String value)  throws PersistanceException;

    /**
     * Validate user boolean.
     *
     * @param identifier the identifier
     * @param password   the password
     * @return the boolean
     * @throws PersistanceException the persistance exception
     */
    boolean validateUser(String identifier, String password) throws PersistanceException;

    /**
     * Remove user.
     *
     * @param identifier the identifier
     * @throws PersistanceException the persistance exception
     */
    void removeUser(String identifier) throws PersistanceException;

    /**
     * Update crypto deleted flag.
     *
     * @param username  the username
     * @param flagValue the flag value
     * @throws PersistanceException the persistance exception
     */
    void updateCryptoDeletedFlag(String username, boolean flagValue) throws PersistanceException;

    /**
     * Update balance.
     *
     * @param newPurchaseValue the new purchase value
     * @param identifier       the identifier
     * @throws PersistanceException the persistance exception
     */
    void updateBalance(double newPurchaseValue, String identifier) throws PersistanceException;

    /**
     * Update password.
     *
     * @param identifier the identifier
     * @param password   the password
     * @throws PersistanceException the persistance exception
     */
    void updatePassword(String identifier, String password) throws PersistanceException;
}