package Business;
import Business.BusinessExceptions.CryptoDelated;
import Business.BusinessExceptions.UserAuthentificationError;
import Business.Entities.User;

public class AccountManager {
    private String EXISTENT_USER_ERROR = "User already exists";
    private String INEXISTENT_USER_ERROR = "User does not exist";
    private String INCORRECT_PASSWORD_ERROR = "Incorrect password";
    private String CRYPTO_DELATED_ERROR = " One or more of the cryptocurrencies you had purchased have been removed from the system.";
    private String INCORRECT_ADMIN_PASSWORD_ERROR = "Incorrect admin password";

    public void registerUser(String username, String mail, String password) {
        try{
            User newUser = UserDAO.getUser(username);
            throw new UserAuthentificationError(EXISTENT_USER_ERROR);
        } catch (DBDataNotFound e) {
            User newUser = new User(username, password, mail, 1000, false);
            UserDAO.registerUser(newUser);
        }
    }

    public User loginUser (String username, String password) {
        try {
            User newUser = UserDAO.getUser(username);
            if (UserDAO.validateUser(password)){
                return newUser;
            }
            else{
                throw new UserAuthentificationError(INCORRECT_PASSWORD_ERROR);
            }
        } catch(DBDataNotFound e) {
            throw new UserAuthentificationError(INEXISTENT_USER_ERROR);
        }
    }

    public void delateCurrentUser(User user) {
        UserDAO.removeUser(user);
    }

    public void warnUserByUserName(User user) {
        if (user.getCryptoDeletedFlag()) {
            user.setCryptoDeletedFlag(false);
            throw new CryptoDelated(CRYPTO_DELATED_ERROR);
        }
    }

    public void adminAccess(String password) {
        if (!UserDAO.validateAdmin(password)){
            throw new UserAuthentificationError(INCORRECT_ADMIN_PASSWORD_ERROR);
        }
    }
}
