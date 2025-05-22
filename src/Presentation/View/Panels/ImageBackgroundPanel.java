package Presentation.View.Panels;

import javax.swing.*;
import java.awt.*;

/**
 * The type Image background panel.
 */
public class ImageBackgroundPanel extends JPanel {
    private Image backgroundImage;
    private float alpha; // 0.0 full transparent, 1.0 opaco

    /**
     * Instantiates a new Image background panel.
     *
     * @param image the image
     * @param alpha the alpha
     */
    public ImageBackgroundPanel(Image image, float alpha) {
        this.backgroundImage = image;
        this.alpha = alpha;
        setLayout(new BorderLayout());
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(Image image) {
        backgroundImage = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}
