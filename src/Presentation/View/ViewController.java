package Presentation.View;
import Business.AccountManager;
import Business.BusinessExceptions.BusinessExeption;
import Business.MarketManager;
import Presentation.Controllers.*;
import Presentation.View.Popups.*;


import javax.swing.*;
import java.util.Objects;

public class ViewController {
    private final LoadFrame loadFrame;
    private final StartFrame startFrame;
    private final MainFrame mainFrame;

    private final UserPopUp userProfile;


    public ViewController() {
        loadFrame = new LoadFrame();
        startFrame = new StartFrame();
        mainFrame = new MainFrame();

        userProfile = new UserPopUp();
    }

    public boolean searchAdmin(String adminName, String password) {
        boolean confirmated = false;

        if (Objects.equals(adminName, "PolAdmin") && Objects.equals(password, "1234")) confirmated = true;

        return confirmated;
    }

    public boolean searchUser(String username, String password) {
        boolean confirmated = false;

        if (username.equals("Pol") && password.equals("1234")) confirmated = true;

        return confirmated;
    }





    public void logOut() {
        userProfile.dispose();
        mainFrame.dispose();
        startFrame.setVisible(true);
    }
    private final String ERROR_EMPTY_FIELD = "Bro, te has dejado campos obligatorios sin rellenar";
    private final String ERROR_NO_EXISTENT_USER = "Bro no existente en nuestra BroBase";
    private final String ERROR_CONTRASEÑA_ERRONEA = "Bro te equivocaste de contraseña";

    public void displayError(String message) {
        JOptionPane.showMessageDialog(null, message ,"CryptoBro Error MSG" , JOptionPane.ERROR_MESSAGE);
    }
}