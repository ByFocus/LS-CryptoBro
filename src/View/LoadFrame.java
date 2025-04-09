package View;
import javax.swing.*;
import java.awt.*;

public class LoadFrame extends JFrame {
    private JProgressBar progressBar;
    private static String logoImgURL = "imgs/CryptoBro.png";

    public LoadFrame() {
        setLayout(new BorderLayout());

        // Agregar la imagen del logo
        JLabel logo = new JLabel(new ImageIcon(logoImgURL));
        add(logo, BorderLayout.CENTER);

        // Crear la barra de progreso
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true); // Muestra el porcentaje en la barra
        add(progressBar, BorderLayout.SOUTH);

        // Configurar tamaño y posición de la ventana
        setSize(560, 490);
        setLocationRelativeTo(null);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }
}