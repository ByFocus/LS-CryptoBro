package View.Tabs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MarketTab extends JPanel {
    private static final String[] columns = {"Nombre", "Precio", "Categoria"};
    private Object[][] data = {
            {"CryptoCoin", 1200, "Financias"},
            {"Etherium", 690, "Financias"},
            {"DogeCoin", 1, "Meme"},
            {"BroCoin", 999999, "Bro"},
    };

    public MarketTab() {
        this.setLayout(new BorderLayout());
        setBackground(new Color(70, 129, 137));

        DefaultTableModel modelo = new DefaultTableModel(data, columns);
        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll);
    }

    public void setCryptoData(String[][] data) {
        this.data = data;
    }
}
