package Persistance;

import Business.Entities.User;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

/**
 * The interface User dao.
 */
public interface UserDAO {
    /**
     * Registers the user in the database
     *
     * @param user the user that is registered
     * @throws PersistanceException when failed to acces or modify the database
     */
    void registerUser(User user) throws PersistanceException;

    /**
     * Gets user by username or email.
     *
     * @param value the value
     * @return the user by username or email
     * @throws PersistanceException when failed to acces or modify the database
     */
    User getUserByUsernameOrEmail(String value)  throws PersistanceException;

    /**
     * Validates the credentials of a user
     *
     * @param identifier the identifier, either the mail or username
     * @param password   the password
     * @return true if the credentials are correct, false if not
     * @throws PersistanceException when failed to acces or modify the database
     */
    boolean validateUser(String identifier, String password) throws PersistanceException;

    /**
     * Deletes a user from the database.
     *
     * @param identifier the identifier of the user
     * @throws PersistanceException when failed to acces or modify the database
     */
    void removeUser(String identifier) throws PersistanceException;

    /**
     * Updates the  crypto-deleted flag from a user.
     *
     * @param username  the username of the user
     * @param flagValue the new value of the crypto-deleted flag
     * @throws PersistanceException when failed to acces or modify the database
     */
    void updateCryptoDeletedFlag(String username, boolean flagValue) throws PersistanceException;

    /**
     * Updates the balance of the user.
     *
     * @param newPurchaseValue the amount to add to the current user balance
     * @param identifier       the identifier of the user
     * @throws PersistanceException when failed to acces or modify the database
     */
    void updateBalance(double newPurchaseValue, String identifier) throws PersistanceException;

    /**
     * Updates the current password of a user
     *
     * @param identifier the identifier of the uesr
     * @param password   the new password
     * @throws PersistanceException when failed to acces or modify the database
     */
    void updatePassword(String identifier, String password) throws PersistanceException;
}