package Presentation.View.Tabs;

import Business.Entities.Purchase;
import Presentation.View.Tables.WalletTableRender;
import Presentation.View.Tables.WalletTableModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * The type Wallet tab.
 */
public class WalletTab extends JPanel {
    private JTable walletTable;

    /**
     * Instantiates a new Wallet tab.
     *
     * @param compras the compras
     */
    public WalletTab(List<Purchase> compras) {
        this.setLayout(new BorderLayout());

        //Color backColor =new Color(157, 190, 187);
        Color backColor = new Color(222, 212, 189);
        setBackground(backColor);

        WalletTableModel model = new WalletTableModel(compras);
        walletTable = new JTable(model);
        JTableHeader header = walletTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new WalletTableRender(true));

        walletTable.setRowHeight(40);
        walletTable.setBackground(new Color(28, 36, 52, 255));
        walletTable.setForeground(new Color(244, 233, 205));
        walletTable.setFont(new Font("Arial", Font.PLAIN, 18));
        walletTable.setShowGrid(false);
        walletTable.setIntercellSpacing(new Dimension(0, 0));
        for (int i = 0; i < walletTable.getColumnCount(); i++) {
            walletTable.getColumnModel().getColumn(i).setCellRenderer(new WalletTableRender(false));
        }

        walletTable.setRowSelectionAllowed(false);
        walletTable.setCellSelectionEnabled(true);

        walletTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(walletTable);
        scroll.getViewport().setBackground(backColor);
        scroll.setBackground(backColor);
        scroll.getVerticalScrollBar().setBackground(backColor);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);
    }


    /**
     * Gets tabla data.
     *
     * @return the tabla data
     */
    public JTable getTablaData() {
        return walletTable;
    }

    /**
     * Load purchases data.
     *
     * @param newPurchases the new purchases
     */
    public void loadPurchasesData(List<Purchase> newPurchases) {
        SwingUtilities.invokeLater(() -> {
            ((WalletTableModel) walletTable.getModel()).setData(newPurchases);
        });
    }

}