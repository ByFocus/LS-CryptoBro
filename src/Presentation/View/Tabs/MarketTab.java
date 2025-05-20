package Presentation.View.Tabs;

import Business.Entities.Crypto;
import Presentation.View.Tables.CryptoNameRender;
import Presentation.View.Tables.CryptoTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MarketTab extends JPanel {
    private JTable cryptoTabla;


    public MarketTab(List<Crypto> cryptoList) {
        this.setLayout(new BorderLayout());
        setBackground(new Color(70, 129, 137));

        CryptoTableModel modelo = new CryptoTableModel(cryptoList);
        cryptoTabla = new JTable(modelo);

        cryptoTabla.setRowHeight(75);
        cryptoTabla.setBackground(new Color(70, 129, 137));
        cryptoTabla.setForeground(new Color(255, 255, 255));
        cryptoTabla.setFont(new Font("Arial", Font.PLAIN, 18));
        cryptoTabla.getColumnModel().getColumn(0).setCellRenderer(new CryptoNameRender());
        cryptoTabla.getColumnModel().getColumn(3).setCellRenderer(new CryptoNameRender());
        cryptoTabla.getColumnModel().getColumn(4).setCellRenderer(new CryptoNameRender());

        cryptoTabla.setRowSelectionAllowed(false);
        cryptoTabla.setCellSelectionEnabled(true);

        cryptoTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(cryptoTabla);
        add(scroll);
    }

    public void loadCryptoData(List<Crypto> cryptoList) {
        //this utility prevents and updates this in a safe way, because swing components are not thread-safe
        SwingUtilities.invokeLater(() -> {
            ((CryptoTableModel) cryptoTabla.getModel()).setData(cryptoList);
        });
    }

    public void test() {
    }

    public JTable getTablaData() {
        return cryptoTabla;
    }
}