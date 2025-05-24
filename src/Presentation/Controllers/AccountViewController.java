package Presentation.Controllers;

import Business.AccountManager;
import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.CryptoDeleted;
import Business.BusinessExceptions.UserAuthentificationError;
import Business.Entities.User;
import Business.EventType;
import Business.WalletManager;
import Persistance.PersistanceExceptions.PersistanceException;
import Presentation.View.Popups.*;
import Presentation.View.StartFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * The type Account view controller.
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

    private AccountViewController() {
        startView = new StartFrame();
        startView.registerController(this);
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static AccountViewController getInstance() {
        if (instance == null) instance = new AccountViewController();

        return instance;
    }

    /**
     * Start.
     */
    public void start() {
        startView.setVisible(true);
    }

    /**
     * Check user profile.
     */
    public void checkUserProfile() {
        userView.setVisible(true);
    }

    /**
     * Sets balance.
     *
     * @param balance the balance
     */
    public void setBalance(String balance) {
        userView.setBalance(balance);
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
                    JOptionPane.showMessageDialog(userView, "Revisa bro, que te has equivocado escribiendo las nuevas contraseñas!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try{
                        AccountManager.getInstance().changePassword(newPwd, oldPwd);
                        MessageDisplayer.displayInformativeMessage("Bro se te ha cambiado la contraseña, más te vale recordarla!");
                        userView.getChangePwdDialog().dispose();

                    } catch (UserAuthentificationError ex) {
                        MessageDisplayer.displayError(ex.getMessage());
                    }

                }
                break;

        }
    }

    private void switchToRegisterView() {
        startView.switchView(StartFrame.REGISTER_VIEW);
        startView.reset();
    }

    private void switchToLoginView() {
        startView.switchView(StartFrame.LOGIN_VIEW);
        startView.reset();
    }

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
                switchToLoginView();
                MessageDisplayer.displayInformativeMessage(REGISTER_SUCCESFUL);
            } catch (BusinessExeption e1) {
                MessageDisplayer.displayError(e1.getMessage());
            }
        }
    }

    private void loginUser() {
        String userName = startView.getNameInput();
        String password = startView.getPasswordInput();

        if(userName.isEmpty() || password.isEmpty()) {
            MessageDisplayer.displayError(ERROR_EMPTY_FIELD);
        } else {


            if (userName.equalsIgnoreCase("admin")) {
                try {
                    startView.reset();
                    AccountManager.getInstance().adminAccess(password);

                    startView.dispose();
                    ApplicationController.getInstance().newApplication("admin", "UNLIMITED", "INFINITE", true);
                    userView = new UserPopUp("Admin", "Admin@gmail.com", "UNDEFINED", true);
                    userView.registerController(this);
                } catch (BusinessExeption e2) {
                    MessageDisplayer.displayError(e2.getMessage());
                }
            }
            else {
                try  {
                    startView.reset();
                    User user =AccountManager.getInstance().loginUser(userName, password);
                    startView.dispose();

                    DecimalFormat priceFormat = new DecimalFormat("#.#####");

                    String balance = priceFormat.format(user.getBalance());
                    double gains = WalletManager.getInstance().calculateEstimatedGainsByUserName(user.getUsername());
                    String profit = priceFormat.format(gains);

                    ApplicationController.getInstance().newApplication(user.getUsername(), balance , profit ,false);
                    userView = new UserPopUp(userName, user.getMail(), String.format("%.2f",user.getBalance()),false);
                    userView.registerController(this);

                    try {
                        AccountManager.getInstance().checkCurrentUserWarning();
                    } catch (CryptoDeleted e){
                        MessageDisplayer.displayWarning(e.getMessage());
                    }

                } catch (BusinessExeption e1) {
                    MessageDisplayer.displayError(e1.getMessage());
                }
            }
        }
    }

    private void userLogOut() {
        userView.dispose();
        ApplicationController.getInstance().close();
        CryptoInfoTabController.getInstance().close();
        MarketTabController.getInstance().close();
        AccountManager.getInstance().logout();
        startView.setVisible(true);
    }

    private void addBalance() {
        MessageDisplayer.displayInformativeMessage(String.valueOf(userView.getBalance()*10));
        //AccountManager.getInstance().
    }
    private void userEraseAccount() {
        int option = MessageDisplayer.askConfirmation(ERASE_CONFIRMATION);

        if (option == JOptionPane.YES_OPTION) {
            AccountManager.getInstance().deleteCurrentUser();
            userLogOut();
        }
        else {
            MessageDisplayer.displayInformativeMessage(ERASE_CANCELATION);
        }
    }

    public void update(EventType event) {

    }
}