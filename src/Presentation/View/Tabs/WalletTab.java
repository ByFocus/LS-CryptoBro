package Presentation.View.Tabs;

import Business.Entities.Purchase;
import Presentation.View.Tables.SellColumnRender;
import Presentation.View.Tables.WalletTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WalletTab extends JPanel {
    private JTable walletTable;

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

    public JTable getTablaData() {
        return walletTable;
    }

    public void loadPurchasesData(List<Purchase> newPurchases) {
        SwingUtilities.invokeLater(() -> {
            ((WalletTableModel) walletTable.getModel()).setData(newPurchases);
        });
    }

}