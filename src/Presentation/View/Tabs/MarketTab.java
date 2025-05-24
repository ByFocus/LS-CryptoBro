package Presentation.View.Tabs;

import Business.Entities.Crypto;
import Presentation.View.Tables.CryptoTableRender;
import Presentation.View.Tables.CryptoTableModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * The type Market tab.
 */
public class MarketTab extends JPanel {
    private JTable cryptoTabla;


    /**
     * Instantiates a new Market tab.
     *
     * @param cryptoList the crypto list
     */
    public MarketTab(List<Crypto> cryptoList) {
        this.setLayout(new BorderLayout());
        setBackground(new Color(157, 190, 187));

        CryptoTableModel modelo = new CryptoTableModel(cryptoList);
        cryptoTabla = new JTable(modelo);
        JTableHeader header = cryptoTabla.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new CryptoTableRender(true));


        cryptoTabla.setBackground(new Color(70, 129, 137));
        cryptoTabla.setForeground(Color.white);
        cryptoTabla.setFont(new Font("Arial", Font.PLAIN, 18));
        cryptoTabla.setRowHeight(40);
        cryptoTabla.setShowGrid(false);
        cryptoTabla.setIntercellSpacing(new Dimension(0, 0));
        for (int i = 0; i < cryptoTabla.getColumnCount(); i++) {
            cryptoTabla.getColumnModel().getColumn(i).setCellRenderer(new CryptoTableRender(false));
        }

        cryptoTabla.setRowSelectionAllowed(false);
        cryptoTabla.setCellSelectionEnabled(true);

        cryptoTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //cryptoTabla.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255)));

        JScrollPane scroll = new JScrollPane(cryptoTabla);
        scroll.getViewport().setBackground(new Color(157, 190, 187));
        scroll.setBackground(new Color(157, 190, 187));
        scroll.getVerticalScrollBar().setBackground(new Color(157, 190, 187));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

    }

    /**
     * Load crypto data.
     *
     * @param cryptoList the crypto list
     */
    public void loadCryptoData(List<Crypto> cryptoList) {
        //this utility prevents and updates this in a safe way, because swing components are not thread-safe
        SwingUtilities.invokeLater(() -> {
            ((CryptoTableModel) cryptoTabla.getModel()).setData(cryptoList);
        });
    }

    /**
     * Gets tabla data.
     *
     * @return the tabla data
     */
    public JTable getTablaData() {
        return cryptoTabla;
    }

}