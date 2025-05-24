package Presentation.View;
import javax.swing.*;
import java.awt.*;

/**
 * LoadFrame is a loading screen displayed during application startup.
 * It shows the CryptoBro logo and a progress bar that indicates the loading progress.
 */
public class LoadFrame extends JFrame {
    private static final String logoImgURL = "imgs/CryptoBro.png";
    private static final String iconImgURL = "imgs/icono.png";

    private final JProgressBar progressBar;

    /**
     * Constructs the loading screen frame.
     * Displays the application logo and a progress bar to visually indicate loading progress.
     * Sets up layout, size, and window icon.
     */
    public LoadFrame() {
        setLayout(new BorderLayout());

        // Agregar la imagen del logo
        JLabel logo = new JLabel(new ImageIcon(logoImgURL));
        add(logo, BorderLayout.CENTER);

        // Crear la barra de progreso
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true); // Business.Entities. Muestra el porcentaje en la barra
        add(progressBar, BorderLayout.SOUTH);

        // Configurar tamaño y posición de la ventana
        setIconImage(new ImageIcon(iconImgURL).getImage());
        setSize(560, 490);
        setLocationRelativeTo(null);
    }

    /**
     * Updates the progress bar value.
     *
     * @param progress An integer from 0 to 100 representing the current progress percentage.
     */
    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }
}