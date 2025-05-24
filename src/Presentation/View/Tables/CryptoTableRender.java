package Presentation.View.Tables;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The type Crypto name render is a custom table cell renderer for cryptocurrency-related tables.
 *  It adjusts the styling of table cells based on whether the cell belongs to the header or body,
 *  and based on specific logic like highlighting positive or negative values with color.
 */
public class CryptoTableRender extends DefaultTableCellRenderer {
    private boolean headerFlag;

    /**
     * Constructs a new {@code CryptoTableRender}.
     *
     * @param header if {@code true}, the renderer is used for table headers;
     *               otherwise, it's used for regular cells.
     */
    public CryptoTableRender(boolean header) {
        super();
        headerFlag = header;
    }

    /**
     * Returns a component that has been configured to display the specified value.
     * Applies custom formatting based on whether the cell is a header, contains a crypto name,
     * or shows a percentage/profit value.
     *
     * @param table      the {@link JTable} that is asking the renderer to draw
     * @param value      the value of the cell to be rendered
     * @param isSelected whether the cell is selected
     * @param hasFocus   whether the cell has focus
     * @param row        the row index of the cell being drawn
     * @param column     the column index of the cell being drawn
     * @return the {@link Component} used for rendering the cell
     */
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
                c.setBackground(new Color(28, 36, 52, 255));
                c.setForeground(Color.white);
                c.setFont(new Font("Arial", Font.BOLD, 18));
            } else if (column == 4 || column == 5) {
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