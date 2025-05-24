package Presentation.View.Panels;

import javax.swing.*;
import java.awt.*;

/**
 * The type Rounded panel is a custom JPanel with rounded corners.
 */
public class    RoundedPanel extends JPanel {
    private int cornerRadius;

    /**
     * Constructs a new RoundedPanel with a specified corner radius.
     *
     * @param radius the radius of the rounded corners
     */
    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false); // Needed for transparency
    }

    /**
     * Paints the panel with rounded corners.
     *
     * @param g the Graphics object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, cornerRadius, cornerRadius);
    }
}
