package Presentation.View.Tables;

import Business.CryptoManager;
import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Persistance.CryptoSQLDAO;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * The type Wallet table model.
 */
public class WalletTableModel extends AbstractTableModel {

    private final String[] columnas = {"Crypto", "Unidades", "Precio de compra", "Balance", "Vender"};
    private List<Purchase> compras;

    /**
     * Instantiates a new Wallet table model.
     *
     * @param compras the compras
     */
    public WalletTableModel(List<Purchase> compras) {
        this.compras = compras;
    }

    /**
     * Sets data.
     *
     * @param compras the compras
     */
    public void setData(List<Purchase> compras) {
        SwingUtilities.invokeLater(() -> {
            synchronized (this) {
                this.compras.clear();
                this.compras.addAll(compras);
                fireTableDataChanged();
            }
        });
    }

    @Override
    public int getRowCount() {
        return compras.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex <= compras.size()) {
            Purchase compra = compras.get(rowIndex);
            CryptoManager cm = new CryptoManager();
            Crypto crypto = cm.getCryptoByName(compra.getCrypto());
            double benefit = crypto.getCurrentPrice()*compra.getUnits() - compra.getPriceUnit()*compra.getUnits();
            char sign = benefit > 0? '+': ' ';

            switch (columnIndex) {
                case 0:
                    return compra.getCrypto();
                case 1:
                    return compra.getUnits();
                case 2:
                    return String.format("%.8f", compra.getPriceUnit());
                case 3:
                    return String.format("%c%.8f",sign, benefit);
                case 4:
                    return "Vender";
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    /**
     * Gets purchase at row.
     *
     * @param row the row
     * @return the purchase at row
     */
    public Purchase getPurchaseAtRow(int row) {
        return compras.get(row);
    }
    
}

