package Presentation.View.Tables;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The type Crypto name render.
 */
public class CryptoTableRender extends DefaultTableCellRenderer {
    private boolean headerFlag;

    public CryptoTableRender(boolean header) {
        super();
        headerFlag = header;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(SwingConstants.CENTER);
        ((JLabel)c).setBorder(BorderFactory.createLineBorder(new Color(3, 25, 38)));
        if (headerFlag) {
            c.setBackground(new Color(3, 25, 38));
            c.setForeground(Color.WHITE);
            c.setFont(new Font("Arial", Font.BOLD, 16));
            ((JLabel)c).setBorder(BorderFactory.createLineBorder(new Color(3, 25, 38)));
        } else {
            if (column == 0) {
                c.setBackground(new Color(28, 36, 52, 255)); //);//new Color(70, 129, 137));
                c.setForeground(Color.white);
                c.setFont(new Font("Arial", Font.BOLD, 18));
            } else if (column == 3 || column == 4) {
                if (value != null) {
                    String text = (String) value;
                    if (text.contains("+")) {
                        c.setForeground(Color.GREEN);
                    } else {
                        c.setForeground(Color.RED);
                    }
                }
            } else {
                c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            }
        }
        return c;
    }
}