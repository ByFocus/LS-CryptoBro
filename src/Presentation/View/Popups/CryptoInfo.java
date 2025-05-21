package Presentation.View.Popups;

import Business.Entities.Crypto;
import Presentation.View.Buttons.RoundedButton;
import Presentation.View.Panels.GraficoCriptomoneda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CryptoInfo extends JFrame {
    public static final String BUY_CRYPTO = "BUY_CRYPTO";

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
    private JTextField amountLabel;
    private RoundedButton botonMenos;
    private RoundedButton botonMas;
    private JButton buyButton;
    private JButton sellButton;

    public CryptoInfo(String cryptoName, int mode){
        configureFrame();

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

        RoundedButton boton5Menos = new RoundedButton("-5", 15);
        boton5Menos.setFont(new Font(FONT,Font.PLAIN,16));
        boton5Menos.setBackground(new Color(70, 129, 167, 255));
        boton5Menos.setForeground(Color.WHITE);
        boton5Menos.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
        boton5Menos.addActionListener(e -> {
            amount -= 5;
            if (amount <= 0) {
                amount = 0;
            }
            amountLabel.setText(String.valueOf(amount));
        });

        botonMenos = new RoundedButton("-", 15);
        botonMenos.setFont(new Font(FONT,Font.PLAIN,18));
        botonMenos.setBackground(new Color(70, 129, 137, 255));
        botonMenos.setForeground(Color.WHITE);
        botonMenos.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
        botonMenos.addActionListener(e -> {
            if (amount > 0) {
                amount--;
                amountLabel.setText(String.valueOf(amount));
            }
        });

        botonMas = new RoundedButton("+", 15);
        botonMas.setFont(new Font(FONT,Font.PLAIN,18));
        botonMas.setBackground(new Color(70, 129, 137, 255));
        botonMas.setForeground(Color.WHITE);
        botonMas.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
        botonMas.addActionListener(e -> {
            amount++;
            amountLabel.setText(String.valueOf(amount));
        });

        RoundedButton boton5Mas = new RoundedButton("+5", 15);
        boton5Mas.setFont(new Font(FONT,Font.PLAIN,16));
        boton5Mas.setBackground(new Color(70, 129, 167, 255));
        boton5Mas.setForeground(Color.WHITE);
        boton5Mas.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
        boton5Mas.addActionListener(e -> {
            amount += 5;
            amountLabel.setText(String.valueOf(amount));
        });


        amountLabel = new JTextField("0", 3);
        amountLabel.setFont(new Font(FONT,Font.PLAIN,18));
        amountLabel.setHorizontalAlignment(JTextField.CENTER);
        amountLabel.setEditable(true);

        panelContador.add(boton5Menos);
        panelContador.add(botonMenos);
        panelContador.add(amountLabel);
        panelContador.add(botonMas);
        panelContador.add(boton5Mas);

        panelContador.setOpaque(false);
        panelContador.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContador.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        add(panelContador, BorderLayout.CENTER);

        addButton(mode);
    }

    public void addButton(int option) {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);

        switch (option) {
            case MODE_BUY_CRYPTO:
                buyButton = new JButton("Comprar");
                styleButton(buyButton);
                buyButton.setActionCommand(BUY_CRYPTO);
                buyButton.putClientProperty("parentCryptoInfo", this);
                buttonPanel.add(buyButton);
                break;
            case MODE_SELL_CRYPTO:
                sellButton = new JButton("Vender");
                styleButton(sellButton);
                sellButton.setActionCommand(SELL_CRYPTO);
                sellButton.putClientProperty("parentCryptoInfo", this);
                buttonPanel.add(sellButton);
                break;
            case MODE_ADMIN:
                buyButton = new JButton("Comprar");
                sellButton = new JButton("Vender");

                styleButton(buyButton);
                styleButton(sellButton);

                buyButton.setActionCommand(BUY_CRYPTO);
                sellButton.setActionCommand(SELL_CRYPTO);

                buyButton.putClientProperty("parentCryptoInfo", this);
                sellButton.putClientProperty("parentCryptoInfo", this);
                System.out.println("hola");
                buttonPanel.add(buyButton);
                buttonPanel.add(sellButton);
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
        if (sellButton != null) sellButton.addActionListener(listener);
        if (buyButton != null) buyButton.addActionListener(listener);
    }

    public int getAmountOfCrypto() {
        return amount;
    }

    public String getCryptoName() {
        return cryptoNameLabel.getText();
    }

    public GraficoCriptomoneda getGrafica() {
        return grafica;
    }

    public void resetAmount() {
        amount = 0;
        amountLabel.setText(String.valueOf(amount));
    }
}
