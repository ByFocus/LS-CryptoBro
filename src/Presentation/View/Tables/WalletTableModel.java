package Presentation.View.Tables;

import Business.BusinessExceptions.BusinessExeption;
import Business.CryptoManager;
import Business.Entities.Crypto;
import Business.Entities.Purchase;
import Presentation.View.Popups.MessageDisplayer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.nio.BufferOverflowException;
import java.util.List;

/**
 * The type Wallet table model.
 */
public class WalletTableModel extends AbstractTableModel {

    private final String[] columnas = {"Crypto", "Unidades", "Precio de compra", "% de cambio", "Precio actual total", "Beneficio (total)", "Vender"};
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
                    fireTableCellUpdated(i, 3);
                    fireTableCellUpdated(i, 4);
                    fireTableCellUpdated(i, 5);
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
    public Object getValueAt(int rowIndex, int columnIndex)  {
        if (rowIndex <= purchases.size()) {
            Purchase purchase = purchases.get(rowIndex);
            try {
                CryptoManager cm = new CryptoManager();
                Crypto crypto = cm.getCryptoByName(purchase.getCrypto());
                double benefitUnit = crypto.getCurrentPrice()*purchase.getUnits() - purchase.getPriceUnit()*purchase.getUnits();
                double benefitTotal = benefitUnit*purchase.getUnits();
                char sign = benefitUnit > 0? '+': ' ';

                switch (columnIndex) {
                    case 0:
                        return purchase.getCrypto();
                    case 1:
                        return purchase.getUnits();
                    case 2:
                        return String.format("%.5f€", purchase.getPriceUnit());
                    case 3:
                        return String.format("%c%.2f%%", sign, benefitUnit*100);
                    case 4:
                        return String.format("%.5f€", crypto.getCurrentPrice()*purchase.getUnits());
                    case 5:
                        return String.format("%c%.5f€",sign, benefitTotal);
                    case 6:
                        return "Vender";
                    default:
                        return null;
                }
            } catch (BusinessExeption e) {
                MessageDisplayer.displayError(e.getMessage());
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

