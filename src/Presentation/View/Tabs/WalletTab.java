package Presentation.View.Tabs;

import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Presentation.View.Tables.CryptoTableModel;
import Presentation.View.Tables.SellColumnRender;
import Presentation.View.Tables.WalletTableModel;

import javax.swing.*;
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
        setBackground(new Color(70, 129, 137));

        WalletTableModel modelo = new WalletTableModel(compras);
        walletTable = new JTable(modelo);

        walletTable.setRowHeight(75);
        walletTable.setBackground(new Color(3, 25, 38));
        walletTable.setForeground(new Color(244, 233, 205));
        walletTable.setFont(new Font("Arial", Font.PLAIN, 18));
        walletTable.getColumnModel().getColumn(4).setCellRenderer(new SellColumnRender());
        walletTable.getColumnModel().getColumn(3).setCellRenderer(new SellColumnRender());

        walletTable.setRowSelectionAllowed(false);
        walletTable.setCellSelectionEnabled(true);

        walletTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(walletTable);
        add(scroll);
    }

    /*public void loadPurchaseData(List<Purchase> compras) {
        ((CryptoTableModel) cartera.getModel()).setData(compras);
        ((CryptoTableModel) cartera.getModel()).fireTableDataChanged();
    }*/

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