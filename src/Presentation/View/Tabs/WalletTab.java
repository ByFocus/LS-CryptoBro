package Presentation.View.Tabs;

import Presentation.View.Tables.SellColumnRender;
import Presentation.View.Tables.WalletTableModel;

import javax.swing.*;
import java.awt.*;

public class WalletTab extends JPanel {
    private JTable cartera;

    public WalletTab() {
        this.setLayout(new BorderLayout());
        setBackground(new Color(70, 129, 137));

        WalletTableModel modelo = new WalletTableModel();
        cartera = new JTable(modelo);

        cartera.setRowHeight(75);
        cartera.setBackground(new Color(3, 25, 38));
        cartera.setForeground(new Color(244, 233, 205));
        cartera.setFont(new Font("Arial", Font.PLAIN, 18));
        cartera.getColumnModel().getColumn(4).setCellRenderer(new SellColumnRender());

        cartera.setRowSelectionAllowed(false);
        cartera.setCellSelectionEnabled(true);

        cartera.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(cartera);
        add(scroll);
    }

    public JTable getTablaData() {
        return cartera;
    }
}