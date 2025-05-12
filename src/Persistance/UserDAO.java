package Persistance;

import Business.Entities.User;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;

public interface UserDAO {
    public boolean registerUser(User user) throws PersistanceException;
    User getUserByUsernameOrEmail(String value) throws DBDataNotFound;
    boolean validateAdmin(String password);
    boolean validateUser(String identifier, String password);
    boolean removeUser(String identifier);
}