package Presentation.View.Popups;

import Business.Entities.Crypto;

import javax.swing.*;
import java.awt.*;

public class CryptoInfo extends JFrame {
    public static final String iconImgURL = "imgs/icono.png";

    private final JLabel cryptoNameLabel;

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
        Container c = getContentPane();
        c.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        cryptoNameLabel.setFont(new Font("Arial", Font.BOLD, 38));
        cryptoNameLabel.setForeground(new Color(3, 25, 38));
        cryptoNameLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        cryptoNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.add(cryptoNameLabel);

        Image userProfileImg = new ImageIcon("imgs/follador.png").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel userProfile = new JLabel(new ImageIcon(userProfileImg));
        userProfile.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        userProfile.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.add(userProfile);

    }
}
