package Presentation.View;
import javax.swing.*;
import java.awt.*;

public class LoadFrame extends JFrame {
    private static final String logoImgURL = "imgs/CryptoBro.png";
    private static final String iconImgURL = "imgs/icono.png";

    private final JProgressBar progressBar;

    public LoadFrame() {
        setLayout(new BorderLayout());

        // Agregar la imagen del logo
        JLabel logo = new JLabel(new ImageIcon(logoImgURL));
        add(logo, BorderLayout.CENTER);

        // Crear la barra de progreso
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true); // Business.Entities.Muestra el porcentaje en la barra
        add(progressBar, BorderLayout.SOUTH);

        // Configurar tamaño y posición de la ventana
        setIconImage(new ImageIcon(iconImgURL).getImage());
        setSize(560, 490);
        setLocationRelativeTo(null);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }
}