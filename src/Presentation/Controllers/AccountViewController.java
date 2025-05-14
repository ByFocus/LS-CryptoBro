package Presentation.Controllers;

import Business.AccountManager;
import Business.BusinessExceptions.BusinessExeption;
import Business.BusinessExceptions.CryptoDeleted;
import Business.Entities.User;
import Business.EventType;
import Presentation.View.Popups.*;
import Presentation.View.StartFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountViewController implements ActionListener, EventListener {
    private static AccountViewController instance;

    private final StartFrame startView;
    private final UserPopUp userView;

    private final String ERROR_EMPTY_FIELD = "Bro, te has dejado campos obligatorios sin rellenar";
    private final String ERROR_NO_EXISTENT_USER = "Bro no existente en nuestra BroBase";
    private final String ERROR_CONTRASEÑA_ERRONEA = "Bro te equivocaste de contraseña";

    private AccountViewController() {
        startView = new StartFrame();
        userView = new UserPopUp();

        startView.registerController(this);
        userView.registerController(this);
    }

    public static AccountViewController getInstance() {
        if (instance == null) instance = new AccountViewController();

        return instance;
    }

    public void start() {
        startView.setVisible(true);
    }

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
                //User user =AccountManager.getInstance().loginUser(userName, password);
                //TODO: SE TIENE QUE USAR EL USUARIO o igual que se logee
                //startView.reset();
                //startView.dispose();
                //ApplicationController.getInstance().userConfirmed(false);
                switchToRegisterView();
            } catch (BusinessExeption e1) {
                MessageDisplayer.displayError(e1.getMessage());
            }
        }
    }

    private void loginUser() {
        if(startView.getNameInput().isEmpty() || startView.getPasswordInput().isEmpty()) {
            MessageDisplayer.displayError(ERROR_EMPTY_FIELD);
        } else {
            String userName = startView.getNameInput();
            String password = startView.getPasswordInput();
            if (userName.equalsIgnoreCase("admin")) {
                try {
                    AccountManager.getInstance().adminAccess(password);
                    ApplicationController.getInstance().newAdminApplication();
                    startView.reset();
                    startView.dispose();
                } catch (BusinessExeption e2) {
                    MessageDisplayer.displayError(e2.getMessage());
                }
            }
            else {
                try  {
                    User user =AccountManager.getInstance().loginUser(userName, password);
                    try {
                        AccountManager.getInstance().checkCurrentUserWarning();
                    } catch (CryptoDeleted e){
                        MessageDisplayer.displayWarning(e.getMessage());
                    }
                    startView.reset();
                    startView.dispose();
                    ApplicationController.getInstance().newUserApplication(user);
                } catch (BusinessExeption e1) {
                    MessageDisplayer.displayError(e1.getMessage());
                }
            }
        }
    }

    private void userLogOut() {
        userView.dispose();
        ApplicationController.getInstance().close();
        startView.setVisible(true);
    }
    public void update(EventType event) {

    }
}