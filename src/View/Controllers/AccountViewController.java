package View.Controllers;

import View.Popups.UserPopUp;
import View.StartFrame;
import View.ViewController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountViewController implements ActionListener {
    private final ViewController controller;

    private final StartFrame startView;
    private final UserPopUp userView;


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
                    controller.errorEmptyInput();
                } else {
                    String userName = startView.getNameInput();
                    String password = startView.getPasswordInput();
                    if (controller.searchAdmin(userName, password)) {
                        startView.reset();
                        controller.userConfirmed(true);
                    }
                    else if (controller.searchUser(userName, password)) {
                        startView.reset();
                        controller.userConfirmed(false);
                    }
                    else controller.errorUserMismatch();
                }
                break;

            case StartFrame.USER_REGISTER:
                if(startView.getNameInput().isEmpty() || startView.getPasswordInput().isEmpty() || startView.getEmailInput().isEmpty()) {
                    controller.errorEmptyInput();
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
}