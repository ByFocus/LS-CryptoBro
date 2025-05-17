package Presentation.View.Tables;

import javax.swing.table.AbstractTableModel;

public class WalletTableModel extends AbstractTableModel {

    private final String[] columnas = {"Crypto", "Unidades", "Precio de compra", "Balance", "Vender"};

    public WalletTableModel() {
    }

    @Override
    public int getRowCount() {
        return 5;
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return "1";
            case 1: return "2";
            case 2: return "3";
            case 3: return "4";
            case 4: return "Vender";
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }
}

