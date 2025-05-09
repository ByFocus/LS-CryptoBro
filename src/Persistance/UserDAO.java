package Persistance;

import Business.Entities.User;
import Persistance.PersistanceExceptions.DBDataNotFound;

public interface UserDAO {
    boolean registerUser(User user);
    User getUserByUsernameOrEmail(String value) throws DBDataNotFound;
    boolean validateAdmin(String password);
    boolean validateUser(String identifier, String password);
    boolean removeUser(String identifier);
}