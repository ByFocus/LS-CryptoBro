package Presentation.View.Tables;

import Business.CryptoManager;
import Business.Entities.Crypto;
import Business.Entities.Purchase;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * The type Wallet table model.
 */
public class WalletTableModel extends AbstractTableModel {

    private final String[] columnas = {"Crypto", "Unidades", "Precio de compra", "Balance", "Vender"};
    private List<Purchase> purchases;

    /**
     * Instantiates a new Wallet table model.
     *
     * @param purchases the compras
     */
    public WalletTableModel(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    /**
     * Sets data.
     *
     * @param newPurchases   the compras
     */
    public void setData(List<Purchase> newPurchases) {
        SwingUtilities.invokeLater(() -> {
            synchronized (this) {
                // cambia la crypto
                for (int i = 0; i < Math.min(purchases.size(), newPurchases.size()); i++) {
                    Purchase existing = purchases.get(i);
                    Purchase updated = newPurchases.get(i);

                    // si los datos no han cambiado no hace falta repintar
                    if (!existing.equals(updated)) {
                        purchases.set(i, updated);
                    }
                    fireTableCellUpdated(i, 2);
                    fireTableCellUpdated(i, 3);
                    fireTableCellUpdated(i, 4);
                }

                // solo se modifican las filas si cambia el número de criptos
                if (newPurchases.size() > purchases.size()) {
                    for (int i = purchases.size(); i < newPurchases.size(); i++) {
                        purchases.add(newPurchases.get(i));
                        fireTableRowsInserted(i, i); // añade una fila
                    }
                } else if (newPurchases.size() < purchases.size()) {
                    int oldSize = purchases.size();
                    purchases.subList(newPurchases.size(), oldSize).clear();
                    fireTableRowsDeleted(newPurchases.size(), oldSize - 1); // quita una
                }
            }
        });
    }

    @Override
    public int getRowCount() {
        return purchases.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex <= purchases.size()) {
            Purchase compra = purchases.get(rowIndex);
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
        return purchases.get(row);
    }
    
}

