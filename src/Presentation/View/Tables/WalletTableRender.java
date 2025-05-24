package Presentation.View.Tables;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *  The WalletTableRender is a custom cell renderer for the wallet table used in the UI.
 *  It supports different styling for table headers and cell content, including conditional
 *  coloring and formatting based on the column and cell value.
 */
public class WalletTableRender extends DefaultTableCellRenderer {

    private boolean headerFlag;

    /**
     * Constructs a new {@code WalletTableRender}.
     *
     * @param header true if the renderer is used for table headers; false for regular cells
     */
    public WalletTableRender(boolean header) {
        super();
        headerFlag = header;
    }

    /**
     * Returns the component used for drawing the cell, with custom styling applied
     * based on header status and cell content.
     *
     * @param table      the {@code JTable} that uses this renderer
     * @param value      the value to assign to the cell
     * @param isSelected whether the cell is selected
     * @param hasFocus   whether the cell has focus
     * @param row        the row index of the cell
     * @param column     the column index of the cell
     * @return the component used for rendering the cell
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
            if (column == 6) {
                c.setBackground(new Color(244, 233, 205));
                c.setForeground(Color.BLACK);
                c.setFont(new Font("Arial", Font.BOLD, 18));
            } else if (column == 3 || column == 5) {
                if (value != null) {
                    String text = (String) value;
                    if (text.contains("+")) {
                        c.setForeground(Color.GREEN);
                    } else {
                        c.setForeground(Color.RED);
                    }
                }
            } else if (column == 0) {
                c.setFont(new Font("Arial", Font.BOLD, 18));
                c.setForeground(Color.WHITE);
            }
            else {
                c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            }
        }
        return c;
    }
}