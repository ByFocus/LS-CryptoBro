package Presentation.View.Panels;

import javax.swing.*;
import java.awt.*;

public class ImageBackgroundPanel extends JPanel {
    private Image backgroundImage;
    private float alpha; // 0.0 full transparent, 1.0 opaco

    public ImageBackgroundPanel(Image image, float alpha) {
        this.backgroundImage = image;
        this.alpha = alpha;
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
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}
