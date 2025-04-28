package Persistance;

import Business.Entities.User;

public interface UserDAO {
    boolean createUser(User user);
    User getUserByUsername(String username);
    User getUserByMail(String mail);
    boolean validateAdmin(String password);
    boolean validateCredentials(String identifier, String password);
    boolean deleteUser(String identifier);
}