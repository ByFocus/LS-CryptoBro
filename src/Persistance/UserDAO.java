package Persistance;

import Business.Entities.User;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

public interface UserDAO {
    void registerUser(User user) throws PersistanceException;
    User getUserByUsernameOrEmail(String value)  throws PersistanceException;
    boolean validateUser(String identifier, String password) throws PersistanceException;
    void removeUser(String identifier) throws PersistanceException;
    void updateCryptoDeletedFlag(String username, boolean flagValue) throws PersistanceException;
}