package Presentation.View.Tables;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The type Sell column render.
 */
public class SellColumnRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (column == 4) {
            c.setBackground(new Color(244, 233, 205));
            c.setForeground(Color.BLACK);
            c.setFont(new Font("Arial", Font.BOLD, 18));
        } else if (column == 3) {
            if (value != null) {
                String text = (String) value;
                if (text.contains("+")) {
                    c.setForeground(Color.GREEN);
                } else {
                    c.setForeground(Color.RED);
                }
            }
        }
        else {
            c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
        }
        return c;
    }
}