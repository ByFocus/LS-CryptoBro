package Business;
import Business.BusinessExceptions.*;
import Business.Entities.User;
import Persistance.PersistanceExceptions.DBDataNotFound;
import Persistance.PersistanceExceptions.PersistanceException;
import Persistance.UserDAO;
import Persistance.UserSQLDAO;

public class AccountManager {
    private final String EXISTENT_USER_ERROR = "Bro, este usuario ya está registrado!";
    private final String INEXISTENT_USER_ERROR = "Bro, el usuario no existe!";
    private final String INCORRECT_PASSWORD_ERROR = "Contraseña incorrecta, echale un vistazo bro!";
    private final String CRYPTO_DELATED_ERROR = "Brother vaya paranoia, una o más de las criptomonedas que habías comprado han sido eliminadas del sistema, se te ha actualizado el saldo!";
    private final String INCORRECT_ADMIN_PASSWORD_ERROR = "Contraseña de administrador incorrecta, echale un vistazo bro!";
    private final String PASSWORD_CAPTIAL_LETTERS = "Hermano, el Block Mayus se puede activar también. Debes utilizar una mayúscula!";
    private final String PASSWORD_LOWE_CASE = "Bro, no todo es gritar, pon alguna minúscula también.";
    private final String PASSWORD_NUMBERS_ERROR = "Compadre, sé que no has tocado un número en tu vida, pero tu contraseña debe contener números";
    private final String PASSWORD_LENGHT_INVALID = "La contraseña debe ser de al menos 8 carácteres de longitud. Ánimo, escribe un poco más, bro!";

    private User currentUser;
    private static AccountManager instance;

    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    void AccountManager() {

    }

    public void registerUser(String username, String mail, String password) {
        try{
            UserDAO userDAO = new UserSQLDAO();
            User newUser = userDAO.getUserByUsernameOrEmail(username); //donde se crea esto?
            throw new UserAuthentificationError(EXISTENT_USER_ERROR);
        } catch (DBDataNotFound e) {
            if (passwordIsValid(password) == 1) {
                UserDAO userDAO = new UserSQLDAO();
                User newUser = new User(username, password, mail, 1000, false);
                try {
                    userDAO.registerUser(newUser);
                } catch (PersistanceException ex) {
                    throw new DataPersistanceError(ex.getMessage());
                }
                //TODO: Actualmente esto no te logea habría que mirarlo, en el controller se pueden llamar registerUser y loginuser segidas
            }
        }
    }

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
        }
    }

    public void delateCurrentUser() {
        UserDAO userDAO = new UserSQLDAO();
        userDAO.removeUser(currentUser.getUsername());
    }

        public void checkCurrentUserWarning() {
        if (currentUser.getCryptoDeletedFlag()) {
            currentUser.setCryptoDeletedFlag(false);
            //TODO:se tiene que actualizar en la base de datos
            UserDAO userDAO = new UserSQLDAO();
            //userDA0.updateCryptoDeletedFlag(currentUser.getUsername(), false);
            throw new CryptoDelated(CRYPTO_DELATED_ERROR);
        }
    }

    public void adminAccess(String password) {
        UserDAO userDAO = new UserSQLDAO();
        if (!userDAO.validateAdmin(password)){
            throw new UserAuthentificationError(INCORRECT_ADMIN_PASSWORD_ERROR);
        }
    }

    public int passwordIsValid (String password) {
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

        return 1;
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            throw new NoCurrentUser("");
        }
        else{
            return currentUser;
        }
    }
}
