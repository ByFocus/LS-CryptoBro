package Presentation.Controllers;

import Business.AccountManager;
import Business.BusinessExceptions.BusinessExeption;
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
                if(startView.getNameInput().isEmpty() || startView.getPasswordInput().isEmpty()) {
                    ErrorDisplayer.displayError(ERROR_EMPTY_FIELD);
                } else {
                    String userName = startView.getNameInput();
                    String password = startView.getPasswordInput();
                    if (userName.equalsIgnoreCase("admin")) {
                        try {
                            startView.reset();
                            AccountManager.getInstance().adminAccess(password);
                            ApplicationController.getInstance().userConfirmed(true);
                        } catch (BusinessExeption e2) {
                            ErrorDisplayer.displayError(e2.getMessage());
                        }
                    }
                    else {
                        try  {
                            User user =AccountManager.getInstance().loginUser(userName, password);
                            //TODO: SE TIENE QUE HACER ALGO CON ESTE USUARIO PARA PRINTAR SU INFO
                            startView.reset();
                            ApplicationController.getInstance().userConfirmed(false);
                        } catch (BusinessExeption e1) {
                            ErrorDisplayer.displayError(e1.getMessage());
                        }
                    }
                }
                break;

            case StartFrame.USER_REGISTER:
                String userName = startView.getNameInput();
                String password = startView.getPasswordInput();
                String email = startView.getEmailInput();

                if(userName.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    ErrorDisplayer.displayError(ERROR_EMPTY_FIELD);
                } else {
                    try {
                        AccountManager am = AccountManager.getInstance();
                        am.registerUser(userName, email, password);
                        User user =AccountManager.getInstance().loginUser(userName, password);
                        //TODO: SE TIENE QUE USAR EL USUARIO
                        startView.reset();
                        startView.dispose();
                        ApplicationController.getInstance().userConfirmed(false);
                    } catch (BusinessExeption e1) {
                        ErrorDisplayer.displayError(e1.getMessage());
                    }
                }
                break;

            case StartFrame.SWITCH_LOGIN:
                startView.switchView(StartFrame.LOGIN_VIEW);
                startView.reset();
                break;

            case StartFrame.SWITCH_REGISTER:
                startView.switchView(StartFrame.REGISTER_VIEW);
                startView.reset();
                break;

            case UserPopUp.USER_LOGOUT:
                userView.dispose();
                ApplicationController.getInstance().close();
                startView.setVisible(true);
                break;
        }
    }

    public void update(EventType event) {

    }
}