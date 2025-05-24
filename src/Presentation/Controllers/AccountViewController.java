package Presentation.Controllers;

import Business.AccountManager;
import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.CryptoDeleted;
import Business.Entities.User;
import Business.EventType;
import Business.WalletManager;
import Presentation.View.Popups.*;
import Presentation.View.StartFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * Controller for managing account-related UI events and interactions.
 * Handles user login, registration, password change, logout, and account deletion.
 */
public class AccountViewController implements ActionListener, EventListener {
    private static AccountViewController instance;

    private final StartFrame startView;
    private UserPopUp userView;

    private final String ERROR_EMPTY_FIELD = "Bro, te has dejado campos obligatorios sin rellenar";
    private final String REGISTER_SUCCESFUL = "Brother, ahora formas parte de la familia!";
    private final String ERASE_CONFIRMATION = "Tete, seguro que te quieres ir? No seas un mileurista!";
    private final String ERASE_CANCELATION = "Nos emocionamos (de forma varonil) que sigas con nosotros!";
    private final String ERROR_NO_EXISTENT_USER = "Bro no existente en nuestra BroBase";
    private final String ERROR_CONTRASEÑA_ERRONEA = "Bro te equivocaste de contraseña";

    /**
     * Private constructor to enforce singleton pattern.
     */
    private AccountViewController() {
        startView = new StartFrame();
        startView.registerController(this);
    }

    /**
     * Returns the singleton instance of the controller.
     *
     * @return the controller instance
     */
    public static AccountViewController getInstance() {
        if (instance == null) instance = new AccountViewController();

        return instance;
    }

    /**
     * Starts the view by making the main frame visible.
     */
    public void start() {
        startView.setVisible(true);
    }

    /**
     * Displays the current user's profile popup.
     */
    public void checkUserProfile() {
        userView.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case StartFrame.USER_LOGIN:
                loginUser();
                break;

            case StartFrame.USER_REGISTER:
                registerUser();
                break;

            case StartFrame.SWITCH_LOGIN:
                switchToLoginView();
                break;

            case StartFrame.SWITCH_REGISTER:
                switchToRegisterView();
                break;

            case UserPopUp.USER_LOGOUT:
                userLogOut();
                break;

            case UserPopUp.ADD_BALANCE:
                addBalance();
                break;

            case UserPopUp.USER_ERASE_ACCOUNT:
                userEraseAccount();
                break;

            case UserPopUp.USER_CHANGE_PASSWORD:
                userView.showChangePasswordDialog(this);
                break;

            case UserPopUp.CHANGE_PASSWORD_OK:
                String oldPwd = new String(userView.getOldPwdField().getPassword());
                String newPwd = new String(userView.getNewPwdField().getPassword());
                String confirmPwd = new String(userView.getConfirmPwdField().getPassword());

                if (!newPwd.equals(confirmPwd)) {
                    MessageDisplayer.displayError("Revisa bro, que te has equivocado escribiendo las nuevas contraseñas!");
                } else if(newPwd.equals(oldPwd)) {
                    MessageDisplayer.displayError("Busca la definición de cambio en internet, la nueva contraseña debe ser distinta a la antigua");
                }else {
                    try{
                        AccountManager.getInstance().changePassword(newPwd, oldPwd);
                        MessageDisplayer.displayInformativeMessage("Bro se te ha cambiado la contraseña, más te vale recordarla!");
                        userView.getChangePwdDialog().dispose();

                    } catch (BusinessExeption ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }

                }
                break;

        }
    }

    /**
     * Switches to the registration view in the UI.
     */
    private void switchToRegisterView() {
        startView.switchView(StartFrame.REGISTER_VIEW);
        startView.reset();
    }

    /**
     * Switches to the login view in the UI.
     */
    private void switchToLoginView() {
        startView.switchView(StartFrame.LOGIN_VIEW);
        startView.reset();
    }

    /**
     * Handles the user registration process.
     */
    private void registerUser() {
        String userName = startView.getNameInput();
        String password = startView.getPasswordInput();
        String email = startView.getEmailInput();

        if(userName.isEmpty() || password.isEmpty() || email.isEmpty()) {
            MessageDisplayer.displayError(ERROR_EMPTY_FIELD);
        } else {
            try {
                AccountManager am = AccountManager.getInstance();
                am.registerUser(userName, email, password);
                logInNormalUser(userName, password);
                MessageDisplayer.displayInformativeMessage(REGISTER_SUCCESFUL);
            } catch (BusinessExeption e1) {
                MessageDisplayer.displayError(e1.getMessage());
            }
        }
    }

    /**
     * Handles the user login process.
     */
    private void loginUser() {
        String identifier = startView.getNameInput();
        String password = startView.getPasswordInput();

        if (identifier.isEmpty() || password.isEmpty()) {
            MessageDisplayer.displayError(ERROR_EMPTY_FIELD);
        } else {
            if (identifier.equalsIgnoreCase("admin")) {
                try {
                    startView.reset();
                    AccountManager.getInstance().adminAccess(password);

                    startView.dispose();
                    ApplicationController.getInstance().newApplication("admin", "ILIMITADO", "INFINITO", true);
                    userView = new UserPopUp("Admin", "Admin@gmail.com", true);
                    userView.registerController(this);
                } catch (BusinessExeption e2) {
                    MessageDisplayer.displayError(e2.getMessage());
                }
            } else {
                try {
                    logInNormalUser(identifier, password);
                } catch (BusinessExeption e1) {
                    MessageDisplayer.displayError(e1.getMessage());
                }
            }
        }
    }

    /**
     * Logs in a regular user and updates the UI accordingly.
     *
     * @param identifier the username or email
     * @param password   the password
     * @throws BusinessExeption if login fails
     */
    private void logInNormalUser(String identifier, String password) throws BusinessExeption {
        startView.reset();
        User user = AccountManager.getInstance().loginUser(identifier, password);
        startView.dispose();

        DecimalFormat priceFormat = new DecimalFormat("#.#####€");

        String balance = priceFormat.format(user.getBalance());
        double gains = WalletManager.getInstance().calculateEstimatedGainsByUserName(user.getUsername());
        String profit = String.valueOf(gains);

        ApplicationController.getInstance().newApplication(user.getUsername(), balance , profit ,false);
        userView = new UserPopUp(identifier, user.getMail(), false);
        userView.registerController(this);

        try {
            AccountManager.getInstance().checkCurrentUserWarning();
        } catch (CryptoDeleted e){
            MessageDisplayer.displayWarning(e.getMessage());
        }
    }

    /**
     * Logs the current user out and resets the application state.
     */
    private void userLogOut() {
        userView.dispose();
        ApplicationController.getInstance().close();
        CryptoInfoTabController.getInstance().close();
        MarketTabController.getInstance().close();
        AccountManager.getInstance().logout();
        switchToLoginView();
        startView.setVisible(true);
    }

    /**
     * Adds balance to the currently logged-in user.
     */
    private void addBalance() {
        double change;

        try {
            change = userView.getBalanceChange();
            AccountManager.getInstance().updateUserBalance(change);
            MessageDisplayer.displayInformativeMessage(String.format("Has añadido " + change + "€\nSeguro que tributados..."));
            userView.resetBalanceChange();
        } catch (Exception e1) {
            MessageDisplayer.displayError("El saldo tiene que ser un real positivo, separado por puntos");
        }
    }

    /**
     * Deletes the current user's account after confirmation.
     */
    private void userEraseAccount() {
        int option = MessageDisplayer.askConfirmation(ERASE_CONFIRMATION);

        if (option == JOptionPane.YES_OPTION) {
            try {
                AccountManager.getInstance().deleteCurrentUser();
            } catch (BusinessExeption ex) {
                MessageDisplayer.displayError(ex.getMessage());
            }
            userLogOut();
        }
        else {
            MessageDisplayer.displayInformativeMessage(ERASE_CANCELATION);
        }
    }

    /**
     * Event notification method. Currently unused.
     *
     * @param event the triggered event
     */
    public void update(EventType event) {

    }
}
