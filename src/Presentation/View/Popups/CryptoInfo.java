package Presentation.View.Popups;

import Business.Entities.Crypto;
import Presentation.View.Buttons.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class CryptoInfo extends JFrame {
    public static final String BUY_CRYPTO = "BUY_CRYPTO";

    public static final String iconImgURL = "imgs/icono.png";

    public static final String TITLE = "Crypto Infromation";

    public static final String FONT = "Arial";


    private final JLabel cryptoNameLabel;

    private int cantidad = 0;
    private JTextField cantidadLabel;
    private RoundedButton botonMenos;
    private RoundedButton botonMas;
    private JButton buyButton;

    public CryptoInfo(Crypto crypto){
        configureFrame();

        cryptoNameLabel = new JLabel(crypto.getName());

        configureCryptoInfo();
    }

    private void configureFrame(){
        setTitle(TITLE);
        setIconImage(new ImageIcon(iconImgURL).getImage());
        setSize(750, 450);
        getContentPane().setBackground(new Color(244, 233, 205));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    private void configureCryptoInfo(){
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        cryptoNameLabel.setFont(new Font(FONT, Font.BOLD, 38));
        cryptoNameLabel.setForeground(new Color(3, 25, 38));
        cryptoNameLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        cryptoNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(cryptoNameLabel);

        Image userProfileImg = new ImageIcon("imgs/follador.png").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel userProfile = new JLabel(new ImageIcon(userProfileImg));
        userProfile.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        userProfile.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(userProfile);

        JPanel panelContador = new JPanel(new FlowLayout());

        RoundedButton boton5Menos = new RoundedButton("-5", 15);
        boton5Menos.setFont(new Font(FONT,Font.PLAIN,16));
        boton5Menos.setBackground(new Color(70, 129, 167, 255));
        boton5Menos.setForeground(Color.WHITE);
        boton5Menos.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
        boton5Menos.addActionListener(e -> {
            cantidad -= 5;
            if (cantidad <= 0) {
                cantidad = 0;
            }
            cantidadLabel.setText(String.valueOf(cantidad));
        });

        botonMenos = new RoundedButton("-", 15);
        botonMenos.setFont(new Font(FONT,Font.PLAIN,18));
        botonMenos.setBackground(new Color(70, 129, 137, 255));
        botonMenos.setForeground(Color.WHITE);
        botonMenos.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
        botonMenos.addActionListener(e -> {
            if (cantidad > 0) {
                cantidad--;
                cantidadLabel.setText(String.valueOf(cantidad));
            }
        });

        botonMas = new RoundedButton("+", 15);
        botonMas.setFont(new Font(FONT,Font.PLAIN,18));
        botonMas.setBackground(new Color(70, 129, 137, 255));
        botonMas.setForeground(Color.WHITE);
        botonMas.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
        botonMas.addActionListener(e -> {
            cantidad++;
            cantidadLabel.setText(String.valueOf(cantidad));
        });

        RoundedButton boton5Mas = new RoundedButton("+5", 15);
        boton5Mas.setFont(new Font(FONT,Font.PLAIN,16));
        boton5Mas.setBackground(new Color(70, 129, 167, 255));
        boton5Mas.setForeground(Color.WHITE);
        boton5Mas.setBorder(BorderFactory.createEmptyBorder(5, 13, 5, 13));
        boton5Mas.addActionListener(e -> {
            cantidad += 5;
            cantidadLabel.setText(String.valueOf(cantidad));
        });


        cantidadLabel = new JTextField("0", 3);
        cantidadLabel.setFont(new Font(FONT,Font.PLAIN,18));
        cantidadLabel.setHorizontalAlignment(JTextField.CENTER);
        cantidadLabel.setEditable(true);

        panelContador.add(boton5Menos);
        panelContador.add(botonMenos);
        panelContador.add(cantidadLabel);
        panelContador.add(botonMas);
        panelContador.add(boton5Mas);

        panelContador.setOpaque(false);
        panelContador.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContador.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        add(panelContador, BorderLayout.CENTER);

        JPanel buyButtonPanel = new JPanel(new FlowLayout());
        buyButtonPanel.setOpaque(false);

        buyButton = new JButton("Comprar");
        buyButton.setFont(new Font(FONT, Font.ITALIC | Font.BOLD, 21));
        buyButton.setBackground(new Color(28, 36, 52, 255));
        buyButton.setForeground(Color.WHITE);
        buyButton.setActionCommand(BUY_CRYPTO);
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        buyButtonPanel.add(buyButton);

        add(buyButtonPanel, BorderLayout.SOUTH);
    }
}
