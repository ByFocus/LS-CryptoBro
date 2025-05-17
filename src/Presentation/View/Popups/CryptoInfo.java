package Presentation.View.Popups;

import Business.Entities.Crypto;

import javax.swing.*;
import java.awt.*;

public class CryptoInfo extends JFrame {
    public static final String iconImgURL = "imgs/icono.png";

    private final JLabel cryptoNameLabel;

    private int cantidad = 0;
    private JTextField cantidadLabel;
    private JButton botonMenos;
    private JButton botonMas;
    private JButton buyButton;

    public CryptoInfo(Crypto crypto){
        configureFrame();

        cryptoNameLabel = new JLabel(crypto.getName());

        configureCryptoInfo();
    }

    private void configureFrame(){
        setTitle("CryptoName");
        setIconImage(new ImageIcon(iconImgURL).getImage());
        setSize(800, 500);
        getContentPane().setBackground(new Color(244, 233, 205));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    private void configureCryptoInfo(){
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        cryptoNameLabel.setFont(new Font("Arial", Font.BOLD, 38));
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

        botonMenos = new JButton("-");
        botonMas = new JButton("+");
        cantidadLabel = new JTextField("0", 3);
        cantidadLabel.setHorizontalAlignment(JTextField.CENTER);
        cantidadLabel.setEditable(false);

        botonMenos.addActionListener(e -> {
            if (cantidad > 0) {
                cantidad--;
                cantidadLabel.setText(String.valueOf(cantidad));
            }
        });

        botonMas.addActionListener(e -> {
            cantidad++;
            cantidadLabel.setText(String.valueOf(cantidad));
        });

        panelContador.add(botonMenos);
        panelContador.add(cantidadLabel);
        panelContador.add(botonMas);
        panelContador.setOpaque(false);
        panelContador.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContador.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        add(panelContador, BorderLayout.CENTER);

        JPanel buyButtonPanel = new JPanel(new FlowLayout());
        buyButtonPanel.setOpaque(false);

        buyButton = new JButton("Comprar");
        buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buyButtonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        buyButtonPanel.add(buyButton);

        add(buyButtonPanel, BorderLayout.SOUTH);
    }
}
