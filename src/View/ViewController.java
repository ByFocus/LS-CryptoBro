package View;
import View.Controllers.AccountViewController;
import View.Popups.*;

import javax.swing.*;
import java.util.Objects;

public class ViewController {
    private final LoadFrame loadFrame;
    private final StartFrame startFrame;
    private final MainFrame mainFrame;

    private final UserPopUp userProfile;

    private final AccountViewController accountController;

    public ViewController() {
        loadFrame = new LoadFrame();
        startFrame = new StartFrame();
        mainFrame = new MainFrame(this);

        userProfile = new UserPopUp(this);

        accountController = new AccountViewController(this, startFrame, userProfile);

        startFrame.registerController(accountController);
        userProfile.registerController(accountController);
    }

    public void start() {
        loadFrame.setVisible(true);
        load();
        startFrame.setVisible(true);
    }

    public void load() {
        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(30); // Simula tiempo de carga (ajusta según lo necesario)
                loadFrame.setProgress(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        loadFrame.dispose();
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

    public void userConfirmed(boolean admin) {
        startFrame.dispose();
        mainFrame.configureTabs(admin);
        mainFrame.setVisible(true);
    }

    public void checkUserProfile() {
        userProfile.setVisible(true);
    }

    public void logOut() {
        userProfile.dispose();
        mainFrame.dispose();
        startFrame.setVisible(true);
    }

    public void errorEmptyInput() {
        JOptionPane.showMessageDialog(null, "Bro, te has dejado campos obligatorios sin rellenar", "CryptoBro Error MSG", JOptionPane.ERROR_MESSAGE);
    }

    public void errorUserMismatch() {
        JOptionPane.showMessageDialog(null, "Bro no existente en nuestra BroBase", "CryptoBro Error MSG", JOptionPane.ERROR_MESSAGE);
    }

    public void errorPasswordMismatch() {
        JOptionPane.showMessageDialog(null, "Bro te equivocaste de contraseña", "CryptoBro Error MSG", JOptionPane.ERROR_MESSAGE);
    }
}
