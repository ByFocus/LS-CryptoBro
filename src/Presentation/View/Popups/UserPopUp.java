package Presentation.View.Popups;

import Business.Entities.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserPopUp extends JFrame {
    //Constantes con los eventos
    public static final String USER_LOGOUT = "USER_LOGOUT";
    public static final String USER_ERASE_ACCOUNT = "USER_ERASE_ACCOUNT";

    //Constantes con titulos, enlaces a fotos y textos
    private static final String FRAME_TITLE = "User Profile";
    private static final String userProfileImgURL = "imgs/usuario.png";

    //Atributos
    private final String userName;
    private final String userEmail;
    private String userBalance;
    private final String userPassword;

    private JButton logOutButton;
    private JButton deleteAccountButton;

    public UserPopUp(String userName, String userEmail, String userBalance, String userPassword, boolean admin) {
        configureFrame();
        configureUserProfile();

        this.userName = userName;
        this.userEmail = userEmail;
        this.userBalance = userBalance;
        this.userPassword = userPassword;


        confiureActions(admin);
    }

    private void configureUserProfile(){
        Container c = getContentPane();
        c.setBackground(new Color(244, 233, 205));
        c.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));


        Image userProfileImg = new ImageIcon(userProfileImgURL).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel userProfile = new JLabel(new ImageIcon(userProfileImg));
        userProfile.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        userProfile.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.add(userProfile);

        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setFont(new Font("Serif", Font.BOLD, 32));
        userNameLabel.setForeground(new Color(3, 25, 38));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.add(userNameLabel);

        JPanel userInfoPanel = new JPanel(new BorderLayout());
        userInfoPanel.setOpaque(false);
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setBackground(new Color(70, 129, 137));

        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(2, 10, 2, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel info1 = new JLabel("Balance: " + userBalance);
        textPanel.add(info1, gbc);

        gbc.gridy = 1;
        JLabel info2 = new JLabel("Email: " + userEmail);
        textPanel.add(info2, gbc);

        gbc.gridy = 2;
        JLabel info3 = new JLabel("Password: " + userPassword);
        textPanel.add(info3, gbc);

        userInfoPanel.add(textPanel);
        c.add(userInfoPanel);
    }

    private void confiureActions(boolean admin){
        //Boton para cerrar sesion
        JPanel actionPanel = new JPanel();

        logOutButton = new JButton("Log Out");
        logOutButton.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 21));
        logOutButton.setBackground(new Color(3, 25, 38)); // Color similar al botón Login en createLoginPanel
        logOutButton.setForeground(Color.WHITE);
        logOutButton.setActionCommand(USER_LOGOUT);

        actionPanel.add(logOutButton);
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        if (!admin) {
            deleteAccountButton = new JButton("Delete Account");
            deleteAccountButton.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 21));
            deleteAccountButton.setBackground(new Color(3, 25, 38));
            deleteAccountButton.setForeground(Color.WHITE);
            deleteAccountButton.setActionCommand(USER_ERASE_ACCOUNT);

            actionPanel.add(deleteAccountButton);
        }

        getContentPane().add(actionPanel);

    }

    private void configureFrame() {
        pack();
        setTitle(FRAME_TITLE);
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void registerController(ActionListener listener) {
        logOutButton.addActionListener(listener);
        deleteAccountButton.addActionListener(listener);
    }
}