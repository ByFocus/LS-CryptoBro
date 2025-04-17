package Popups;

import View.MainFrame;
import View.ViewController;

import javax.swing.*;
import java.awt.*;

public class UserPopUp extends JFrame {
    private static final String FRAME_TITLE = "User Profile";
    private static final String userProfileImgURL = "imgs/follador.png";

    private ViewController controller;

    public UserPopUp(ViewController controller) {
        this.controller = controller;

        configureFrame();
        configureProfile();
    }

    private void configureProfile(){
        Container c = getContentPane();
        c.setBackground(new Color(244, 233, 205));
        c.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));


        Image userProfileImg = new ImageIcon(userProfileImgURL).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JLabel userProfile = new JLabel(new ImageIcon(userProfileImg));
        userProfile.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        userProfile.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.add(userProfile);

        JLabel userNameLabel = new JLabel("User");
        userNameLabel.setFont(new Font("Serif", Font.BOLD, 32));
        userNameLabel.setForeground(new Color(3, 25, 38));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.add(userNameLabel);

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(new Color(70, 129, 137));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(2, 10, 2, 10);


        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel info1 = new JLabel("Info");
        infoPanel.add(info1, gbc);

        gbc.gridy = 1;
        JLabel info2 = new JLabel("Info");
        infoPanel.add(info2, gbc);

        gbc.gridy = 2;
        JLabel info3 = new JLabel("Info");
        infoPanel.add(info3, gbc);

        c.add(infoPanel);

        //Boton para cerrar sesion
        JPanel logOutPanel = new JPanel();
        JButton logOutButton = new JButton("Log Out");
        logOutButton.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 21));
        logOutButton.setBackground(new Color(3, 25, 38)); // Color similar al botón Login en createLoginPanel
        logOutButton.setForeground(Color.WHITE);

        logOutButton.addActionListener(e -> {
            controller.logOut();
        });

        logOutPanel.add(logOutButton);
        logOutPanel.setOpaque(false);
        logOutPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        c.add(logOutPanel);
    }
    private void configureFrame() {
        pack();
        setTitle(FRAME_TITLE);
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
