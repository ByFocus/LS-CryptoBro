package Presentation.View.Tables;

import Business.Entities.Crypto;
import Business.Entities.Purchase;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class WalletTableModel extends AbstractTableModel {

    private final String[] columnas = {"Crypto", "Unidades", "Precio de compra", "Balance", "Vender"};
    private List<Purchase> compras;

    public WalletTableModel(List<Purchase> compras) {
        this.compras = compras;
    }

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
            switch (columnIndex) {
                case 0:
                    return compra.getCrypto();
                case 1:
                    return compra.getUnits();
                case 2:
                    return compra.getPriceUnit();
                case 3:
                    return "-";
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
}

