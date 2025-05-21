package Presentation.View.Popups;

import Presentation.View.Buttons.RoundedButton;
import Presentation.View.Panels.GraficoCriptomoneda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CryptoInfo extends JFrame {
    public static final String BUY_CRYPTO = "BUY_CRYPTO";
    public static final String CHANGE_PRICE = "CHANGE_PRICE";
    public static final String SELL_CRYPTO = "SELL_CRYPTO";

    public static final String iconImgURL = "imgs/icono.png";

    public static final String TITLE = "Crypto Infromation";

    public static final String FONT = "Arial";

    public static final int MODE_BUY_CRYPTO = 0;

    public static final int MODE_SELL_CRYPTO = 1;

    public static final int MODE_ADMIN = 2;

    private final JLabel cryptoNameLabel;

    private GraficoCriptomoneda grafica;

    private int amount = 0;
    private int assignedRow;
    private JTextField amountLabel;
    private JButton functionButton;

    public CryptoInfo(String cryptoName, int mode, int row){
        configureFrame();
        assignedRow = row;
        cryptoNameLabel = new JLabel(cryptoName);

        configureCryptoInfo(mode);
    }

    private void configureFrame(){
        setTitle(TITLE);
        setIconImage(new ImageIcon(iconImgURL).getImage());
        setSize(750, 450);
        getContentPane().setBackground(new Color(28,36,52,255));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    private void configureCryptoInfo(int mode){
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        cryptoNameLabel.setFont(new Font(FONT, Font.BOLD, 38));
        cryptoNameLabel.setForeground(new Color(244, 233, 205));
        cryptoNameLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        cryptoNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(cryptoNameLabel);

        grafica = new GraficoCriptomoneda();
        grafica.setPreferredSize(new Dimension(getWidth(), getHeight()));
        grafica.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        add(grafica);

        JPanel panelContador = new JPanel(new FlowLayout());

        amountLabel = new JTextField("0", 3);
        amountLabel.setFont(new Font(FONT,Font.PLAIN,18));
        amountLabel.setHorizontalAlignment(JTextField.CENTER);
        amountLabel.setEditable(false);

        if (mode != MODE_ADMIN) {
            RoundedButton boton5Menos = new RoundedButton("-5", 15);
            boton5Menos.setFont(new Font(FONT, Font.PLAIN, 16));
            boton5Menos.setBackground(new Color(70, 129, 167, 255));
            boton5Menos.setForeground(Color.WHITE);
            boton5Menos.setBorder(BorderFactory.createEmptyBorder(5, 18, 5, 13));
            boton5Menos.addActionListener(e -> {
                amount -= 5;
                if (amount <= 0) {
                    amount = 0;
                }
                amountLabel.setText(String.valueOf(amount));
            });

            RoundedButton botonMenos = new RoundedButton("-", 15);
            botonMenos.setFont(new Font(FONT, Font.PLAIN, 18));
            botonMenos.setBackground(new Color(70, 129, 137, 255));
            botonMenos.setForeground(Color.WHITE);
            botonMenos.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 13));
            botonMenos.addActionListener(e -> {
                if (amount > 0) {
                    amount--;
                    amountLabel.setText(String.valueOf(amount));
                }
            });

            RoundedButton botonMas = new RoundedButton("+", 15);
            botonMas.setFont(new Font(FONT, Font.PLAIN, 18));
            botonMas.setBackground(new Color(70, 129, 137, 255));
            botonMas.setForeground(Color.WHITE);
            botonMas.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
            botonMas.addActionListener(e -> {
                amount++;
                amountLabel.setText(String.valueOf(amount));
            });

            RoundedButton boton5Mas = new RoundedButton("+5", 15);
            boton5Mas.setFont(new Font(FONT, Font.PLAIN, 16));
            boton5Mas.setBackground(new Color(70, 129, 167, 255));
            boton5Mas.setForeground(Color.WHITE);
            boton5Mas.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
            boton5Mas.addActionListener(e -> {
                amount += 5;
                amountLabel.setText(String.valueOf(amount));
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
        add(panelContador, BorderLayout.CENTER);
        addFunctionButton(mode);
        Component glue = Box.createVerticalGlue();
        glue.setMinimumSize(new Dimension(0, 30));
        add(glue);

    }

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
                buttonPanel.add(functionButton);
                break;
            case MODE_ADMIN:
                amountLabel.setEditable(true); // para que pueda introducir el valor
                functionButton = new JButton("Cambiar Precio");
                styleButton(functionButton);
                functionButton.setActionCommand(CHANGE_PRICE);
                functionButton.putClientProperty("parentCryptoInfo", this);
                buttonPanel.add(functionButton);
                break;
        }

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font(FONT, Font.ITALIC | Font.BOLD, 21));
        button.setBackground(new Color(244, 233, 205));
        button.setForeground(new Color(28,36,52,255));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void registerController(ActionListener listener) {
        if (functionButton != null) functionButton.addActionListener(listener);
    }

    public String getAmountLabel() {
        return amountLabel.getText();
    }

    public String getCryptoName() {
        return cryptoNameLabel.getText();
    }

    public GraficoCriptomoneda getGrafica() {
        return grafica;
    }

    public int getRow(){
        return assignedRow;
    }

    public void resetAmount() {
        amount = 0;
        amountLabel.setText(String.valueOf(amount));
    }
}
