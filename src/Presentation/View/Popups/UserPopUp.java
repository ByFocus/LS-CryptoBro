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
    private static final String FONT_TITLE = "Serif";
    private static final String FONT_TEXT = "Arial";

    private static final String FRAME_TITLE = "User Profile";
    private static final String userProfileImgURL = "imgs/usuario.png";

    //Atributos
    private final JLabel userNameLabel;
    private final JLabel userEmailLabel;
    private JLabel userBalanceLabel;
    private final JLabel userPasswordLabel;

    private JButton logOutButton;
    private JButton deleteAccountButton;

    public UserPopUp(String userName, String userEmail, String userBalance, String userPassword, boolean admin) {
        configureFrame();

        this.userNameLabel = new JLabel(userName);;
        this.userBalanceLabel = new JLabel("Balance: " + userBalance);
        this.userEmailLabel = new JLabel("Email: " + userEmail);
        this.userPasswordLabel = new JLabel("Password: " + userPassword);;

        configureUserProfile();
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

        userNameLabel.setFont(new Font(FONT_TITLE, Font.BOLD, 38));
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
        gbc.insets = new Insets(15, 10, 15, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;

        userBalanceLabel.setFont(new Font(FONT_TEXT, Font.BOLD, 18));

        textPanel.add(userBalanceLabel, gbc);

        gbc.gridy = 1;

        userEmailLabel.setFont(new Font(FONT_TEXT, Font.BOLD, 18));

        textPanel.add(userEmailLabel, gbc);

        gbc.gridy = 2;

        userPasswordLabel.setFont(new Font(FONT_TEXT, Font.BOLD, 18));

        textPanel.add(userPasswordLabel, gbc);

        userInfoPanel.add(textPanel);
        c.add(userInfoPanel);
    }

    private void confiureActions(boolean admin){
        //Boton para cerrar sesion
        JPanel actionPanel = new JPanel();

        logOutButton = new JButton("Log Out");
        logOutButton.setFont(new Font(FONT_TEXT, Font.ITALIC | Font.BOLD, 21));
        logOutButton.setBackground(new Color(3, 25, 38));
        logOutButton.setForeground(Color.WHITE);
        logOutButton.setActionCommand(USER_LOGOUT);

        actionPanel.add(logOutButton);
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        if (!admin) {
            deleteAccountButton = new JButton("Delete Account");
            deleteAccountButton.setFont(new Font(FONT_TEXT, Font.ITALIC | Font.BOLD, 21));
            deleteAccountButton.setBackground(new Color(119, 172, 162));
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

    public void setBalance(String balance){
        userBalanceLabel.setText(balance);
    }
}