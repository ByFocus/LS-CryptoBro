package Presentation.View.Tabs;

import Business.Entities.Crypto;
import Presentation.View.CryptoTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MarketTab extends JPanel {
    private JTable cryptoTabla;

    public MarketTab(List<Crypto> cryptoList) {
        this.setLayout(new BorderLayout());
        setBackground(new Color(70, 129, 137));

        CryptoTableModel modelo = new CryptoTableModel(cryptoList);
        cryptoTabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(cryptoTabla);
        add(scroll);
    }

    public void loadCryptoData(List<Crypto> cryptoList) {
        CryptoTableModel modelo = new CryptoTableModel(cryptoList);
        cryptoTabla = new JTable(modelo);
    }
}