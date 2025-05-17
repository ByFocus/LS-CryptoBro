package Presentation.View.Panels;

import javax.swing.*;
import java.awt.*;

public class ImageBackgroundPanel extends JPanel {
    private Image backgroundImage;

    public ImageBackgroundPanel(Image image) {
        this.backgroundImage = image;
        setLayout(new BorderLayout());
    }

    public void setImage(Image image) {
        backgroundImage = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            float alpha = 0.5f; // 0.0f = fully transparent, 1.0f = fully opaque
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}
