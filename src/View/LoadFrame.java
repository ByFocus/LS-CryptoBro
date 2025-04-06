package View;
import javax.swing.*;
import java.awt.*;

public class LoadFrame extends JFrame {
    private JProgressBar progressBar;
    private static String logoImgURL = "imgs/CryptoBro.png";

    public LoadFrame() {
        setLayout(new BorderLayout());

        // Agregar la imagen del logo
        JLabel logo = new JLabel(new ImageIcon(logoImgURL)); // Cambia "ruta/a/tu/logo.png" por la ruta real de tu logo
        add(logo, BorderLayout.CENTER);

        // Crear la barra de progreso
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true); // Muestra el porcentaje en la barra
        add(progressBar, BorderLayout.SOUTH);

        // Configurar tamaño y posición de la ventana
        setSize(560, 480); // Ajusta el tamaño según tu imagen y barra
        setLocationRelativeTo(null); // Centrar en la pantalla
    }

    public void uploadProgress(int progress) {
        progressBar.setValue(progress);
    }
}