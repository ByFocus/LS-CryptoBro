package Presentation.View.Buttons;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JButton implementation that displays a button with rounded corners.
 */
public class RoundedButton extends JButton {
    private int radius;

    /**
     * Constructs a new RoundedButton with the specified label and corner radius.
     *
     * @param label  the text to display on the button
     * @param radius the radius of the button's rounded corners
     */
    public RoundedButton(String label, int radius) {
        super(label);
        this.radius = radius;
        setContentAreaFilled(false);
    }

    /**
     * Paints the component with a custom rounded background.
     *
     * @param g the Graphics context to use for painting
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g2);
        g2.dispose();
    }

    /**
     * Overrides the border painting to skip drawing borders,
     * preserving the rounded appearance.
     *
     * @param g the Graphics context
     */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.dispose();
    }
}
