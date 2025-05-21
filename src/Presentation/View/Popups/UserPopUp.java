package Presentation.View.Popups;

import Presentation.View.Panels.RoundedPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UserPopUp extends JFrame {
    // Event constants
    public static final String USER_LOGOUT = "USER_LOGOUT";
    public static final String USER_ERASE_ACCOUNT = "USER_ERASE_ACCOUNT";

    // Fonts and resources
    public static final String FONT_TITLE = "Open Sans";
    public static final String FONT_TEXT = "Arial";
    public static final String FRAME_TITLE = "User Profile";
    public static final String userProfileImgURL = "imgs/usuario.png";

    // UI attributes
    private final JLabel userNameLabel;
    private final JLabel userEmailLabel;
    private JLabel userBalanceLabel;
    private final JLabel userPasswordLabel;

    private JButton logOutButton;
    private JButton deleteAccountButton;
    private JButton changePasswordButton;

    public UserPopUp(String userName, String userEmail, String userBalance, String userPassword, boolean admin) {
        configureFrame();

        this.userNameLabel = new JLabel(userName);
        this.userBalanceLabel = new JLabel("Balance: " + userBalance);
        this.userEmailLabel = new JLabel("Email: " + userEmail);
        this.userPasswordLabel = new JLabel("Password: " + userPassword);

        configureUserProfile();
        configureActions(admin);
    }

    private void configureUserProfile(){
        setSize(600, 500);

        Container c = getContentPane();
        c.setBackground(new Color(244, 233, 205));
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
        topPanel.setOpaque(false);

        logOutButton = new JButton("Log Out");
        logOutButton.setFont(new Font(FONT_TEXT, Font.ITALIC | Font.BOLD, 21));
        logOutButton.setBackground(new Color(3, 25, 38));
        logOutButton.setForeground(Color.WHITE);
        logOutButton.setActionCommand(USER_LOGOUT);

        topPanel.add(logOutButton);
        c.add(topPanel);

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
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 0, 30));

        JPanel textPanel = new RoundedPanel(20); // 20 is the border radius
        textPanel.setBackground(new Color(70, 129, 137));
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(15, 10, 15, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        userEmailLabel.setFont(new Font(FONT_TEXT, Font.BOLD, 18));
        userEmailLabel.setForeground(Color.white);
        textPanel.add(userEmailLabel, gbc);


        userInfoPanel.add(textPanel, BorderLayout.NORTH);
        c.add(userInfoPanel);
    }

    private void configureActions(boolean admin){
        // Bottom panel for other action buttons
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // Only add delete and change password if not admin
        if (!admin) {
            deleteAccountButton = new JButton("Delete Account");
            deleteAccountButton.setFont(new Font(FONT_TEXT, Font.ITALIC | Font.BOLD, 21));
            deleteAccountButton.setBackground(new Color(213, 0, 0));
            deleteAccountButton.setForeground(Color.WHITE);
            deleteAccountButton.setActionCommand(USER_ERASE_ACCOUNT);
            actionPanel.add(deleteAccountButton);

            changePasswordButton = new JButton("Change Password");
            changePasswordButton.setFont(new Font(FONT_TEXT, Font.BOLD, 21));
            changePasswordButton.setBackground(new Color(70, 129, 137));
            changePasswordButton.setForeground(Color.WHITE);
            // You may want to set a different action command for this button
            changePasswordButton.setActionCommand("USER_CHANGE_PASSWORD");
            actionPanel.add(changePasswordButton);
        }

        getContentPane().add(actionPanel);
    }

    private void configureFrame() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void registerController(ActionListener listener) {
        logOutButton.addActionListener(listener);
        if (deleteAccountButton != null) {
            deleteAccountButton.addActionListener(listener);
        }
        if (changePasswordButton != null) {
            changePasswordButton.addActionListener(listener);
        }
    }

    public void setBalance(String balance){
        userBalanceLabel.setText(balance);
    }
}
