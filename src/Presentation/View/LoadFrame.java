package Presentation.View;
import javax.swing.*;
import java.awt.*;

public class LoadFrame extends JFrame {
    private static final String logoImgURL = "imgs/CryptoBro.png";

    private final JProgressBar progressBar;

    public LoadFrame() {
        setLayout(new BorderLayout());

        // Agregar la imagen del logo
        ImageIcon icono = new ImageIcon(logoImgURL);
        JLabel logo = new JLabel(icono);
        add(logo, BorderLayout.CENTER);

        // Crear la barra de progreso
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true); // Muestra el porcentaje en la barra
        add(progressBar, BorderLayout.SOUTH);

        // Configurar tamaño y posición de la ventana
        setIconImage(icono.getImage());
        setSize(560, 490);
        setLocationRelativeTo(null);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }
}