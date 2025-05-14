package Persistance;

import Business.Entities.User;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

public interface UserDAO {
    boolean registerUser(User user) throws PersistanceException;
    User getUserByUsernameOrEmail(String value)  throws PersistanceException;
    boolean validateAdmin(String password) throws PersistanceException;
    boolean validateUser(String identifier, String password) throws PersistanceException;
    boolean removeUser(String identifier) throws PersistanceException;
    void updateCryptoDeletedFlag(String username, boolean flagValue) throws PersistanceException;
}