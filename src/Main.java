import View.LoadFrame;
import View.MainFrame;
import View.StartFrame;

public class Main {
    public static void main(String[] args) {
        StartFrame menu = new StartFrame();
        LoadFrame frame = new LoadFrame();

        frame.setVisible(true);
        frame.load();
        menu.setVisible(true);

    }
}

