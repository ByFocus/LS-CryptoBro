package Presentation.View;
import Presentation.View.Popups.*;

public class ViewController {
    private LoadFrame loadFrame;
    private StartFrame startFrame;
    private MainFrame mainFrame;

    private UserPopUp userProfile;

    public ViewController() {
        loadFrame = new LoadFrame();
        startFrame = new StartFrame(this);
        mainFrame = new MainFrame(this);

        userProfile = new UserPopUp(this);
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

    public void userConfirmed() {
        startFrame.dispose();

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
}
