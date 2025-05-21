package Presentation.View.Tabs;

import Business.Entities.Crypto;
import Presentation.View.Tables.CryptoNameRender;
import Presentation.View.Tables.CryptoTableModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

public class MarketTab extends JPanel {
    private JTable cryptoTabla;


    public MarketTab(List<Crypto> cryptoList) {
        this.setLayout(new BorderLayout());
        setBackground(new Color(244, 233, 205));//new Color(70, 129, 137));

        CryptoTableModel modelo = new CryptoTableModel(cryptoList);
        cryptoTabla = new JTable(modelo);
        JTableHeader header = cryptoTabla.getTableHeader();
        header.setBackground(new Color(3, 25, 38));
        header.setForeground(Color.white);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setFont(new Font("Arial", Font.BOLD, 16));
        cryptoTabla.setBackground(new Color(70, 129, 137));
        cryptoTabla.setForeground(new Color(255, 255, 255));
        cryptoTabla.setFont(new Font("Arial", Font.PLAIN, 18));
        cryptoTabla.setRowHeight(40);
        for (int i = 0; i < cryptoTabla.getColumnCount(); i++) {
            cryptoTabla.getColumnModel().getColumn(i).setCellRenderer(new CryptoNameRender());
        }

        cryptoTabla.setRowSelectionAllowed(false);
        cryptoTabla.setCellSelectionEnabled(true);

        cryptoTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cryptoTabla.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255)));

        JScrollPane scroll = new JScrollPane(cryptoTabla);
        scroll.getViewport().setBackground(new Color(244, 233, 205));
        scroll.setBackground(new Color(244, 233, 205));
        scroll.getVerticalScrollBar().setBackground(new Color(244, 233, 205));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scroll, BorderLayout.CENTER);

    }

    public void loadCryptoData(List<Crypto> cryptoList) {
        //this utility prevents and updates this in a safe way, because swing components are not thread-safe
        SwingUtilities.invokeLater(() -> {
            ((CryptoTableModel) cryptoTabla.getModel()).setData(cryptoList);
        });
    }

    public JTable getTablaData() {
        return cryptoTabla;
    }

}