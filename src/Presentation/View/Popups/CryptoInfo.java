package Presentation.View.Popups;

import Presentation.View.Buttons.RoundedButton;
import Presentation.View.Panels.GraficoCriptomoneda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The type Crypto info.
 */
public class CryptoInfo extends JFrame {

    public static final String BUY_CRYPTO = "BUY_CRYPTO";
    public static final String CHANGE_PRICE = "CHANGE_PRICE";
    public static final String SELL_CRYPTO = "SELL_CRYPTO";

    public static final int MODE_BUY_CRYPTO = 0;
    public static final int MODE_SELL_CRYPTO = 1;
    public static final int MODE_ADMIN = 2;

    private static final String iconImgURL = "imgs/icono.png";

    private static final String TITLE = "Crypto Infromation";

    private static final String FONT = "Arial";

    private final JLabel cryptoNameLabel;
    private GraficoCriptomoneda graph;

    private final int assignedRow;
    private final int mode;
    private int numCryptos;
    private int amount = 0;
    private JLabel inventoryLabel;
    private JTextField amountLabel;
    private JButton functionButton;

    /**
     * Instantiates a new Crypto info.
     *
     * @param cryptoName the crypto name
     * @param mode       the mode
     * @param row        the row
     */
    public CryptoInfo(String cryptoName, int mode, int numCryptos, int row){
        configureFrame();
        assignedRow = row;
        cryptoNameLabel = new JLabel(cryptoName);
        this.numCryptos = numCryptos;
        this.mode = mode;
        configureCryptoInfo();
    }

    private void configureFrame(){
        setTitle(TITLE);
        setIconImage(new ImageIcon(iconImgURL).getImage());
        setSize(800, 500);
        getContentPane().setBackground(new Color(28,36,52,255));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    private void configureCryptoInfo(){
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        cryptoNameLabel.setFont(new Font(FONT, Font.BOLD|Font.ITALIC, 38));
        cryptoNameLabel.setForeground(new Color(244, 233, 205));
        cryptoNameLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        cryptoNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(cryptoNameLabel);

        if (mode != MODE_ADMIN) {
            inventoryLabel = new JLabel();
            inventoryLabel.setFont(new Font(FONT, Font.BOLD, 14));
            inventoryLabel.setForeground(new Color(244, 233, 205));
            inventoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            updateInventoryLabel();
            add(inventoryLabel);
        }

        graph = new GraficoCriptomoneda();
        graph.setPreferredSize(new Dimension(getWidth(), getHeight()));
        graph.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        add(graph);

        JPanel panelContador = new JPanel(new FlowLayout());

        amountLabel = new JTextField("0", 3);
        amountLabel.setFont(new Font(FONT,Font.PLAIN,18));
        amountLabel.setHorizontalAlignment(JTextField.CENTER);
        amountLabel.setEditable(false);


        //Botones para sumar o restar cantidad de compra
        if (mode != MODE_ADMIN) {
            RoundedButton boton5Menos = new RoundedButton("-5", 15);
            boton5Menos.setFont(new Font(FONT, Font.PLAIN, 16));
            boton5Menos.setBackground(new Color(70, 129, 167, 255));
            boton5Menos.setForeground(Color.WHITE);
            boton5Menos.setBorder(BorderFactory.createEmptyBorder(5, 18, 5, 13));
            boton5Menos.addActionListener(e -> {
                if (amount < 5) {
                    updateAmountLabel(0);
                } else {
                    updateAmountLabel(amount-5);
                }
            });

            RoundedButton botonMenos = new RoundedButton("-", 15);
            botonMenos.setFont(new Font(FONT, Font.PLAIN, 18));
            botonMenos.setBackground(new Color(70, 129, 137, 255));
            botonMenos.setForeground(Color.WHITE);
            botonMenos.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 13));
            botonMenos.addActionListener(e -> {
                if (amount > 0) {
                   updateAmountLabel(amount-1);
                }
            });

            RoundedButton botonMas = new RoundedButton("+", 15);
            botonMas.setFont(new Font(FONT, Font.PLAIN, 18));
            botonMas.setBackground(new Color(70, 129, 137, 255));
            botonMas.setForeground(Color.WHITE);
            botonMas.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
            botonMas.addActionListener(e -> {
                updateAmountLabel(amount+1);
            });

            RoundedButton boton5Mas = new RoundedButton("+5", 15);
            boton5Mas.setFont(new Font(FONT, Font.PLAIN, 16));
            boton5Mas.setBackground(new Color(70, 129, 167, 255));
            boton5Mas.setForeground(Color.WHITE);
            boton5Mas.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
            boton5Mas.addActionListener(e -> {
               updateAmountLabel(amount+5);
            });
            panelContador.add(boton5Menos);
            panelContador.add(botonMenos);
            panelContador.add(amountLabel);
            panelContador.add(botonMas);
            panelContador.add(boton5Mas);
        } else {
            panelContador.add(amountLabel);
        }



        panelContador.setOpaque(false);
        panelContador.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContador.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        Component glue1 = Box.createVerticalGlue();
        glue1.setMinimumSize(new Dimension(0, 30));
        Component glue2 = Box.createVerticalGlue();
        glue2.setMinimumSize(new Dimension(0, 30));

        add(panelContador, BorderLayout.CENTER);
        if (mode == MODE_SELL_CRYPTO) {
            RoundedButton allButton = new RoundedButton("Todas", 15);
            allButton.addActionListener(e-> {
                updateAmountLabel(numCryptos);
            });
            allButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            allButton.setFont(new Font(FONT, Font.PLAIN, 16));
            allButton.setBackground(new Color(70, 129, 167, 255));
            allButton.setForeground(Color.WHITE);
            add(allButton);
        }
        add(glue1);
        addFunctionButton(mode);
        add(glue2);

    }

    private void updateInventoryLabel() {
        String labelText;

        if (mode == MODE_BUY_CRYPTO) {
            if (numCryptos == 0) {
                labelText = "No hay cryptos en posesión";
            } else {
                labelText = "Tienes " + numCryptos + " cryptos en posesión";
            }
        } else {
            if (numCryptos == 0) {
                labelText = "No quedan cryptos asociadas a esta venta";
            } else {
                labelText = "Hay " + numCryptos + " cryptos asociadas a esta venta";
            }
        }
        inventoryLabel.setText(labelText);
    }

    /**
     * Add function button.
     *
     * @param option the option
     */
    public void addFunctionButton(int option) {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        switch (option) {
            case MODE_BUY_CRYPTO:
                functionButton = new JButton("Comprar");
                styleButton(functionButton);
                functionButton.setActionCommand(BUY_CRYPTO);
                functionButton.putClientProperty("parentCryptoInfo", this);
                buttonPanel.add(functionButton);
                break;
            case MODE_SELL_CRYPTO:
                functionButton = new JButton("Vender");
                styleButton(functionButton);
                functionButton.setActionCommand(SELL_CRYPTO);
                functionButton.putClientProperty("parentCryptoInfo", this);
                break;
            case MODE_ADMIN:
                amountLabel.setEditable(true); // para que pueda introducir el valor
                functionButton = new JButton("Cambiar Precio");
                styleButton(functionButton);
                functionButton.setActionCommand(CHANGE_PRICE);
                functionButton.putClientProperty("parentCryptoInfo", this);
                break;
        }

        add(functionButton, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font(FONT, Font.ITALIC | Font.BOLD, 21));
        button.setBackground(new Color(244, 233, 205));
        button.setForeground(new Color(28,36,52,255));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    /**
     * Register controller.
     *
     * @param listener the listener
     */
    public void registerController(ActionListener listener) {
        if (functionButton != null) functionButton.addActionListener(listener);
    }

    /**
     * Gets amount label.
     *
     * @return the amount label
     */
    public String getAmountLabel() {
        return amountLabel.getText();
    }

    /**
     * Gets crypto name.
     *
     * @return the crypto name
     */
    public String getCryptoName() {
        return cryptoNameLabel.getText();
    }

    /**
     * Gets graph.
     *
     * @return the graph
     */
    public GraficoCriptomoneda getGraph() {
        return graph;
    }

    /**
     * Get row int.
     *
     * @return the int
     */
    public int getRow(){
        return assignedRow;
    }

    /**
     * Reset amount.
     */
    public void resetAmount() {
        updateAmountLabel(0);
    }

    private void updateAmountLabel(int num) {
        amount = num;
        amountLabel.setText(String.valueOf(amount));
    }

    public void modifyUnits(int units) {
        numCryptos += units;
        updateInventoryLabel();
    }
}
