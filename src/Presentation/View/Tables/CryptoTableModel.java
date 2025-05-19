package Presentation.View.Tables;

import Business.Entities.Crypto;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CryptoTableModel extends AbstractTableModel {
    private final List<Crypto> cryptos;
    private final String[] columnas = {"Nombre", "Categoría", "Precio Actual", "Variación", "Varición %"};

    public CryptoTableModel(List<Crypto> cryptos) {
        this.cryptos = cryptos;
    }

    public synchronized void setData(List<Crypto> cryptos) {
        this.cryptos.clear();
        this.cryptos.addAll(cryptos);
    }
    @Override
    public int getRowCount() {
        return cryptos.size();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex <= cryptos.size()) {
            Crypto crypto = cryptos.get(rowIndex);
            switch (columnIndex) {
                case 0: return crypto.getName();
                case 1: return crypto.getCategory();
                case 2: return crypto.getCurrentPrice();
                case 3: return "-";
                case 4: return "%";
                default: return null;
            }
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }
}

