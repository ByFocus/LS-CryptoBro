package Presentation.View.Popups;

import javax.swing.*;
import java.awt.*;

public class CryptoInfo extends JFrame {
    public static final String iconImgURL = "imgs/icono.png";

    public CryptoInfo(){
        configureFrame();

    }

    public void configureFrame(){
        setTitle("CryptoName");
        setIconImage(new ImageIcon(iconImgURL).getImage());
        setSize(800, 500);
        getContentPane().setBackground(new Color(244, 233, 205));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }
}
