package Business;
import Business.BusinessExceptions.*;
import Business.Entities.User;
import Persistance.*;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;
import Persistance.SQL.UserSQLDAO;

/**
 * The type AccountManager.
 * Manages user registration, login, password updates, and administrative access.
 * Implements singleton pattern to maintain a unique user session.
 */
public class AccountManager {
    private final String EXISTENT_USER_ERROR = "Bro, este usuario ya está registrado!";
    private final String EXISTENT_MAIL_ERROR = "Bro, este correo ya está en uso!";
    private final String INEXISTENT_USER_ERROR = "Bro, el usuario no existe!";
    private final String INCORRECT_PASSWORD_ERROR = "Contraseña incorrecta, echale un vistazo bro!";
    private final String CRYPTO_DELETED_ERROR = "Brother vaya paranoia, una o más de las criptomonedas que habías\ncomprado han sido eliminadas del sistema, se te ha actualizado el saldo!";
    private final String INCORRECT_ADMIN_PASSWORD_ERROR = "Contraseña de administrador incorrecta, echale un vistazo bro!";
    private final String PASSWORD_CAPTIAL_LETTERS = "Hermano, el Block Mayus se puede activar también. Debes utilizar una mayúscula!";
    private final String PASSWORD_LOWE_CASE = "Bro, no todo es gritar, pon alguna minúscula también.";
    private final String PASSWORD_NUMBERS_ERROR = "Compadre, sé que no has tocado un número en tu vida, pero tu contraseña debe contener números";
    private final String PASSWORD_LENGHT_INVALID = "La contraseña debe ser de al menos 8 carácteres de longitud. Ánimo, escribe un poco más, bro!";
    private final String EMAIL_FORMAT_NOT_VALID = "Brother, ni de fly eso es un mail, recuerda que luce así: corrige.tu@email.bro";
    private final String TRYING_TO_REGISTER_ADMIN = "Hermano, ya sabes que ese nombre de usario está pillado ;)";

    private User currentUser;
    private static AccountManager instance;

    private AccountManager() {}

    /**
     * Gets the singleton instance of AccountManager.
     *
     * @return the instance
     */
    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    /**
     * Registers a new user in the system.
     *
     * @param username the desired username
     * @param mail     the email address
     * @param password the chosen password
     * @throws BusinessExeption if validation or persistence fails
     */
    public void registerUser(String username, String mail, String password) throws BusinessExeption{
        if (username.equalsIgnoreCase("admin")) {
            throw new UserAuthentificationError(TRYING_TO_REGISTER_ADMIN);
        }
        try{
            UserDAO userDAO = new UserSQLDAO();
            userDAO.getUserByUsernameOrEmail(username);
            throw new UserAuthentificationError(EXISTENT_USER_ERROR);
        } catch (DBDataNotFound _) {
            try {
                UserDAO userDAO = new UserSQLDAO();
                userDAO.getUserByUsernameOrEmail(mail);
                throw new UserAuthentificationError(EXISTENT_MAIL_ERROR);
            } catch (DBDataNotFound _) {
                checkPasswordIsValid(password);
                checkEmailIsValid(mail);
                UserDAO userDAO = new UserSQLDAO();
                User newUser = new User(username, password, mail, 1000, false);
                try {
                    userDAO.registerUser(newUser);
                } catch (PersistanceException ex) {
                    throw new DataPersistanceError(ex.getMessage());
                }
            }
            catch (PersistanceException e) {
                throw new DataPersistanceError(e.getMessage());
            }
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }

    }

    /**
     * Logs in a user by username and password.
     *
     * @param username the username or email
     * @param password the password
     * @return the logged-in User object
     * @throws BusinessExeption if credentials are incorrect or user not found
     */
    public User loginUser (String username, String password) throws BusinessExeption {
        try {
            UserDAO userDAO = new UserSQLDAO();
            currentUser = userDAO.getUserByUsernameOrEmail(username);
            if (userDAO.validateUser(username, password)){
                return currentUser;
            }
            else{
                throw new UserAuthentificationError(INCORRECT_PASSWORD_ERROR);
            }
        } catch(DBDataNotFound e) {
            throw new UserAuthentificationError(INEXISTENT_USER_ERROR);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Changes the password of the currently logged-in user or the admin.
     *
     * @param newPwd the new password
     * @param oldPwd the old password
     */
    public void changePassword(String newPwd, String oldPwd) throws  BusinessExeption{
        if(currentUser != null){
            try{
                UserDAO userDAO = new UserSQLDAO();
                try{
                    checkPasswordIsValid(newPwd);
                }catch (UserAuthentificationError ex){
                    throw new UserAuthentificationError(ex.getMessage());
                }
                if(userDAO.validateUser(currentUser.getUsername(), oldPwd)){
                    try{
                        userDAO.updatePassword(currentUser.getUsername(), newPwd);
                    }catch (PersistanceException e){
                        throw new DataPersistanceError(e.getMessage());
                    }
                }else{
                    throw new UserAuthentificationError(INCORRECT_PASSWORD_ERROR);
                }
            } catch(DBDataNotFound e) {
                throw new UserAuthentificationError(INEXISTENT_USER_ERROR);
            } catch (PersistanceException e) {
                throw new DataPersistanceError(e.getMessage());
            }
        }else{
            adminAccess(oldPwd);
            try {
                ConfigurationDAO confDAO = new ConfigurationJSONDAO();
                confDAO.setAdminPass(newPwd);
            } catch (PersistanceException e) {
                throw new DataPersistanceError(e.getMessage());
            }
        }
    }

    /**
     * Deletes the account of the currently logged-in user.
     *
     * @throws BusinessExeption if deletion fails
     */
    public void deleteCurrentUser() throws BusinessExeption{
        try {
            WalletManager.getInstance().deleteWalletFromUser(currentUser.getUsername());
            UserDAO userDAO = new UserSQLDAO();
            userDAO.removeUser(currentUser.getUsername());
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Checks whether the current user has a pending crypto deletion warning.
     * Throws a warning if true and resets the flag.
     */
    public void checkCurrentUserWarning() throws BusinessExeption{
           try {
               if (currentUser.getCryptoDeletedFlag()) {
                   //modificamos el valor de la base de datos (ya se le ha avisado)
                   UserDAO userDAO = new UserSQLDAO();
                   userDAO.updateCryptoDeletedFlag(currentUser.getUsername(), false);
                   throw new CryptoDeleted(CRYPTO_DELETED_ERROR);
               }
           } catch (PersistanceException e) {
               throw new DataPersistanceError(e.getMessage());
           }
    }

    /**
     * Performs admin authentication.
     *
     * @param password the admin password
     * @throws BusinessExeption if password is incorrect
     */
    public void adminAccess(String password) throws BusinessExeption {
        try {
            ConfigurationDAO conDAO = new ConfigurationJSONDAO();
            if(!(conDAO.getAdminPass().equals(password))){
                throw new UserAuthentificationError(INCORRECT_ADMIN_PASSWORD_ERROR);
            }
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Validates a given password according to security rules.
     *
     * @param password the password to validate
     * @throws BusinessExeption if the password is invalid
     */
    private void checkPasswordIsValid (String password) throws BusinessExeption{

        if (password == null || password.length() < 8) {
            throw new UserAuthentificationError(PASSWORD_LENGHT_INVALID);
        }

        boolean hasLowercase = false;
        boolean hasUppercase = false;
        boolean hasDigit = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        if (!hasUppercase) {
            throw new UserAuthentificationError(PASSWORD_CAPTIAL_LETTERS);
        }

        if (!hasDigit) {
            throw new UserAuthentificationError(PASSWORD_NUMBERS_ERROR);
        }

        if (!hasLowercase) {
            throw new UserAuthentificationError(PASSWORD_LOWE_CASE);
        }
    }

    /**
     * Validates the format of an email address.
     *
     * @param email the email to validate
     * @throws BusinessExeption if the email format is invalid
     */
    private void checkEmailIsValid(String email) throws BusinessExeption {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if(!email.matches(emailRegex)) {
            throw new UserAuthentificationError(EMAIL_FORMAT_NOT_VALID);
        }
    }

    /**
     * Gets the username of the current user.
     *
     * @return the current user's username
     */
    public String getCurrentUserName() throws BusinessExeption{
        if (currentUser == null) {
            throw new NoCurrentUser("There is no current user");
        }
        else{
            return currentUser.getUsername();
        }
    }

    /**
     * Gets the current logged-in user object.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }


    /**
     * Issues a crypto deletion warning to the user and updates their balance.
     *
     * @param benefits the amount to credit
     * @param username the user affected
     */
    public void warnCryptoDeleted(double benefits, String username) throws BusinessExeption {
        try {
            UserDAO userDAO = new UserSQLDAO();
            User user = userDAO.getUserByUsernameOrEmail(username);
            userDAO.updateBalance(benefits, username);
            userDAO.updateCryptoDeletedFlag(user.getUsername(), true);
        } catch (PersistanceException e){
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Updates the user's balance after a transaction.
     *
     * @param change the change in balance (positive or negative)
     */
    public void updateUserBalance(double change) throws BusinessExeption {
        try {
            UserDAO userDAO = new UserSQLDAO();
            userDAO.updateBalance(change, currentUser.getUsername());
            currentUser.setBalance(currentUser.getBalance() + change);
            MarketManager.getMarketManager().notify(EventType.USER_BALANCE_CHANGED);
        } catch (PersistanceException e) {
            throw new DataPersistanceError(e.getMessage());
        }
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        currentUser = null;
    }
}
