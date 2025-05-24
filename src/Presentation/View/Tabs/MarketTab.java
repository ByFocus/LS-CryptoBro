package Presentation.View.Tabs;

import Business.Entities.Crypto;
import Presentation.View.Tables.CryptoTableRender;
import Presentation.View.Tables.CryptoTableModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * The type Market tab represents the UI panel responsible for displaying
 * the current market view of available cryptocurrencies. It includes a table
 * showing details about each crypto, styled with custom renderers.
 */
public class MarketTab extends JPanel {
    private JTable cryptoTable;

    /**
     * Constructs a new {@code MarketTab} panel with a list of cryptocurrencies
     * to be displayed in a table.
     *
     * @param cryptoList the initial list of {@code Crypto} objects to show in the table
     */
    public MarketTab(List<Crypto> cryptoList) {
        this.setLayout(new BorderLayout());
        //Color backColor = new Color(244, 233, 205, 200);
        Color backColor =new Color(157, 190, 187);
        setBackground(backColor);

        CryptoTableModel model = new CryptoTableModel(cryptoList);
        cryptoTable = new JTable(model);
        JTableHeader header = cryptoTable.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new CryptoTableRender(true));


        cryptoTable.setBackground(new Color(70, 129, 137));
        cryptoTable.setForeground(Color.white);
        cryptoTable.setFont(new Font("Arial", Font.PLAIN, 18));
        cryptoTable.setRowHeight(40);
        cryptoTable.setShowGrid(false);
        cryptoTable.setIntercellSpacing(new Dimension(0, 0));
        for (int i = 0; i < cryptoTable.getColumnCount(); i++) {
            cryptoTable.getColumnModel().getColumn(i).setCellRenderer(new CryptoTableRender(false));
        }

        cryptoTable.setRowSelectionAllowed(false);
        cryptoTable.setCellSelectionEnabled(true);

        cryptoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //cryptoTabla.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255)));

        JScrollPane scroll = new JScrollPane(cryptoTable);
        scroll.getViewport().setBackground(backColor);
        scroll.setBackground(backColor);
        scroll.getVerticalScrollBar().setBackground(backColor);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

    }

    /**
     * Loads and updates the table model with a new list of cryptocurrencies.
     * This method ensures thread-safety using {@link SwingUtilities#invokeLater(Runnable)}.
     *
     * @param cryptoList the updated list of {@code Crypto} objects
     */
    public void loadCryptoData(List<Crypto> cryptoList) {
        //this utility prevents and updates this in a safe way, because swing components are not thread-safe
        SwingUtilities.invokeLater(() -> {
            ((CryptoTableModel) cryptoTable.getModel()).setData(cryptoList);
        });
    }

    /**
     * Returns the reference to the crypto table component.
     *
     * @return the {@code JTable} containing crypto data
     */
    public JTable getTablaData() {
        return cryptoTable;
    }

}