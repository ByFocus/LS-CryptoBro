package Presentation.Controllers;

import Business.AccountManager;
import Business.BusinessExceptions.BusinessExeption;
import Business.Entities.User;
import Business.EventType;
import Presentation.View.Popups.*;
import Presentation.View.StartFrame;
import Presentation.View.ViewController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountViewController implements ActionListener, EventListener {
    private final ViewController controller;

    private final StartFrame startView;
    private final UserPopUp userView;

    private final String ERROR_EMPTY_FIELD = "Bro, te has dejado campos obligatorios sin rellenar";
    private final String ERROR_NO_EXISTENT_USER = "Bro no existente en nuestra BroBase";
    private final String ERROR_CONTRASEÑA_ERRONEA = "Bro te equivocaste de contraseña";

    public AccountViewController(ViewController controller, StartFrame startView, UserPopUp userView) {
        this.controller = controller;

        this.startView = startView;
        this.userView = userView;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case StartFrame.USER_LOGIN:
                if(startView.getNameInput().isEmpty() || startView.getPasswordInput().isEmpty()) {
                    controller.displayError(ERROR_EMPTY_FIELD);
                } else {
                    String userName = startView.getNameInput();
                    String password = startView.getPasswordInput();
                    if (userName.equalsIgnoreCase("admin")) {
                        try {
                            startView.reset();
                            AccountManager.getInstance().adminAccess(password);
                            controller.userConfirmed(true);
                        } catch (BusinessExeption e2) {
                            controller.displayError(e2.getMessage());
                        }
                    }
                    else {
                        try  {
                            User user =AccountManager.getInstance().loginUser(userName, password);
                            //TODO: SE TIENE QUE HACER ALGO CON ESTE USUARIO PARA PRINTAR SU INFO
                            startView.reset();
                            controller.userConfirmed(false);
                        } catch (BusinessExeption e2) {
                            controller.displayError(e2.getMessage());
                        }
                    }
                }
                break;

            case StartFrame.USER_REGISTER:
                if(startView.getNameInput().isEmpty() || startView.getPasswordInput().isEmpty() || startView.getEmailInput().isEmpty()) {
                    controller.displayError(ERROR_EMPTY_FIELD);
                } else {
                    //controller.newUser();
                    startView.reset();
                    controller.userConfirmed(false);
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
                controller.logOut();
                break;
        }
    }

    public void update(EventType event) {

    }
}