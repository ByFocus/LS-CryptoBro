import View.LoadFrame;
import View.MainFrame;
import View.StartFrame;

public class Main {
    public static void main(String[] args) {
        StartFrame frame = new StartFrame();
        LoadFrame load = new LoadFrame();

        load.setVisible(true);
        for (int i = 0; i <= 100; i++) {
            try {
                Thread.sleep(20); // Simula tiempo de carga (ajusta según lo necesario)
                load.uploadProgress(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        load.dispose();
        frame.setVisible(true);

    }
}

