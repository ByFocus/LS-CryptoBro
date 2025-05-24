package Presentation.View.Tables;

import Business.Entities.Crypto;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * The type Crypto table model is a custom table model for displaying a list of cryptocurrencies
 *  in a {@link JTable}. It formats and provides access to data such as the crypto name, category,
 *  current price, and market variation (both absolute and percentage).
 */
public class CryptoTableModel extends AbstractTableModel {
    private final List<Crypto> cryptos;
    private final String[] columns = {"Nombre", "Categoría", "Precio Inicial", "Precio Actual", "Variación", "Variación %"};

    /**
     * Constructs a new {@code CryptoTableModel} with the specified list of cryptocurrencies.
     *
     * @param cryptos the list of {@link Crypto} objects to display
     */
    public CryptoTableModel(List<Crypto> cryptos) {
        this.cryptos = cryptos;
    }

    /**
     * Updates the data in the model with a new list of cryptocurrencies.
     * This method ensures thread safety and notifies the table about cell or row changes accordingly.
     *
     * @param newCryptos the updated list of cryptocurrencies
     */
    public synchronized void setData(List<Crypto> newCryptos) {

        // cambia la crypto
        for (int i = 0; i < Math.min(cryptos.size(), newCryptos.size()); i++) {
            Crypto existing = cryptos.get(i);
            Crypto updated = newCryptos.get(i);

            // si los datos no han cambiado no hace falta repintar
            if (!existing.equals(updated)) {
                cryptos.set(i, updated);
            }
            fireTableCellUpdated(i, 2);
            fireTableCellUpdated(i, 3);
            fireTableCellUpdated(i, 4);
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
    }

    /**
     * Returns the number of rows in the table, corresponding to the number of cryptos.
     *
     * @return the number of rows
     */
    @Override
    public int getRowCount() {
        return cryptos.size();
    }

    /**
     * Returns the number of columns in the table.
     *
     * @return the number of columns
     */
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    /**
     * Returns the value for a specific cell at the given row and column.
     *
     * @param rowIndex    the row index
     * @param columnIndex the column index
     * @return the value to display in the table cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < cryptos.size()) {
            Crypto crypto = cryptos.get(rowIndex);
            double marketGap = crypto.calculateMarketGap();
            double percentageVariation = marketGap*100.0/crypto.getInitialPrice();
            char sign = marketGap > 0? '+': ' ';

            switch (columnIndex) {
                case 0: return crypto.getName();
                case 1: return crypto.getCategory();
                case 2: return String.format("%.4f€",crypto.getInitialPrice());
                case 3: return String.format("%.4f€",crypto.getCurrentPrice());
                case 4: return String.format("%c%.6f",sign, marketGap);
                case 5: return String.format("%c%.2f%%",sign, percentageVariation);
                default: return null;
            }
        }
        return null;
    }

    /**
     * Returns the name of the column at the specified index.
     *
     * @param column the column index
     * @return the column name
     */
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

}

