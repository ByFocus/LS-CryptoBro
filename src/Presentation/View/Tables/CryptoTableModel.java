package Presentation.View.Tables;

import Business.Entities.Crypto;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CryptoTableModel extends AbstractTableModel {
    private final List<Crypto> cryptos;
    private final String[] columnas = {"Nombre", "Categoría", "Precio Actual", "Variación", "Varición %"};

    public CryptoTableModel(List<Crypto> cryptos) {
        this.cryptos = cryptos;
    }

    /*
    public synchronized void setData(List<Crypto> cryptos) {
        boolean numCryptosChanged = getRowCount() != cryptos.size();
        this.cryptos.clear();
        this.cryptos.addAll(cryptos);
        if (numCryptosChanged) {
           fireTableDataChanged();
        } else {
           fireTableRowsUpdated();
        }
    }*/

    public synchronized void setData(List<Crypto> newCryptos) {
        // miramos si ha cambiado el número de cryptos
        boolean structureChanged = (newCryptos.size() != cryptos.size());

        // cambia la crypto
        for (int i = 0; i < Math.min(cryptos.size(), newCryptos.size()); i++) {
            Crypto existing = cryptos.get(i);
            Crypto updated = newCryptos.get(i);

            // si los datos no han cambiado no hace falta repintar
            if (!existing.equals(updated)) {
                cryptos.set(i, updated);
            }
            fireTableRowsUpdated(i, i); // Notify UI to repaint this row
        }

        // solo se modifican las filas si cambia el número de criptos
        if (newCryptos.size() > cryptos.size()) {
            for (int i = cryptos.size(); i < newCryptos.size(); i++) {
                cryptos.add(newCryptos.get(i));
                fireTableRowsInserted(i, i); // añade una fila
            }
        } else if (newCryptos.size() < cryptos.size()) {
            int oldSize = cryptos.size();
            cryptos.subList(newCryptos.size(), oldSize).clear();
            fireTableRowsDeleted(newCryptos.size(), oldSize - 1); // quita una
        }

        // Cambia toda la tabla
        if (structureChanged) {
            SwingUtilities.invokeLater(() -> {
                fireTableStructureChanged();
            });
        }
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
        if (rowIndex < cryptos.size()) {
            Crypto crypto = cryptos.get(rowIndex);
            double marketGap = crypto.calculateMarketGap();
            char sign = marketGap > 0? '+': ' ';

            switch (columnIndex) {
                case 0: return crypto.getName();
                case 1: return crypto.getCategory();
                case 2: return String.format("%.8f",crypto.getCurrentPrice());
                case 3: return String.format("%c%.8f",sign, marketGap);
                case 4: return String.format("%c%.2f%%",sign, crypto.calculateMarketGap()*100);
                default: return null;
            }
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }

    public void clear() {
        cryptos.clear();
        SwingUtilities.invokeLater(() -> {
            fireTableDataChanged();
        });
    }
}

