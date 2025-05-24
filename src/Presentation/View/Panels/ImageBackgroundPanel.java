package Presentation.View.Panels;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel that displays a background image with configurable transparency.
 */
public class ImageBackgroundPanel extends JPanel {
    private Image backgroundImage;
    private float alpha; // 0.0 full transparent, 1.0 opaco

    /**
     * Constructs a new ImageBackgroundPanel with the specified image and alpha.
     *
     * @param image the background image to display
     * @param alpha the transparency level (0.0 to 1.0)
     */
    public ImageBackgroundPanel(Image image, float alpha) {
        this.backgroundImage = image;
        this.alpha = alpha;
        setLayout(new BorderLayout());
    }

    /**
     * Sets a new image as the panel background and triggers repaint.
     *
     * @param image the new background image
     */
    public void setImage(Image image) {
        backgroundImage = image;
        repaint();
    }

    /**
     * Paints the component including the background image with the specified alpha.
     *
     * @param g the Graphics object used for drawing
     */
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
