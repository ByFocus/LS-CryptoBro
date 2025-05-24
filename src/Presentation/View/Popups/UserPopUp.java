package Presentation.View.Popups;

import Presentation.View.Panels.RoundedPanel;
import  java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The type User pop up.
 */
public class UserPopUp extends JFrame {
// Event constants
    public static final String USER_LOGOUT = "USER_LOGOUT";
    public static final String USER_ERASE_ACCOUNT = "USER_ERASE_ACCOUNT";
    public static final String USER_CHANGE_PASSWORD = "USER_CHANGE_PASSWORD";
    public static final String CHANGE_PASSWORD_OK = "CHANGE_PASSWORD_OK";
    public static final String ADD_BALANCE = "ADD_BALANCE";

// Fonts and resources
    public static final String FONT_TITLE = "Open Sans";
    public static final String FONT_TEXT = "Arial";
    public static final String FRAME_TITLE = "User Profile";
    public static final String userProfileImgURL = "imgs/usuario.png";

    // UI attributes
    private final JLabel userNameLabel;
    private final JLabel userEmailLabel;

    private JPasswordField oldPwdField;
    private JPasswordField newPwdField;
    private JPasswordField confirmPwdField;
    private JTextField balanceField;

    private JButton logOutButton;
    private JButton addBalanceButton;
    private JButton deleteAccountButton;
    private JButton changePasswordButton;
    private JDialog changePwdDialog;
    /**
     * Instantiates a new User pop up.
     *
     * @param userName    the user name
     * @param userEmail   the user email
     * @param admin       the admin
     */
    public UserPopUp(String userName, String userEmail, boolean admin) {
        configureFrame();

        this.userNameLabel = new JLabel(userName);
        this.userEmailLabel = new JLabel("Email: " + userEmail);

        configureUserProfile();
        configureActions(admin);
        setLocationRelativeTo(null);
    }

    private void configureUserProfile(){
        setSize(500, 600);

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
        userProfile.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        userProfile.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.add(userProfile);

        userNameLabel.setFont(new Font(FONT_TITLE, Font.BOLD, 38));
        userNameLabel.setForeground(new Color(3, 25, 38));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.add(userNameLabel);

        JPanel userInfoPanel = new JPanel(new BorderLayout());
        userInfoPanel.setOpaque(false);
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 0, 30));

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

        if (!admin) {
            JPanel balanceInputPanel = new JPanel();
            balanceInputPanel.setOpaque(false);
            actionPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

            balanceField = new JTextField();
            balanceField.setEditable(true);
            balanceField.setPreferredSize(new Dimension(100, 30));
            balanceField.setFont(new Font(FONT_TEXT, Font.PLAIN, 18));
            balanceInputPanel.add(balanceField);

            addBalanceButton = new JButton("Add Balance");
            addBalanceButton.setFont(new Font(FONT_TEXT, Font.BOLD, 18));
            addBalanceButton.setForeground(Color.white);
            addBalanceButton.setBackground(new Color(28, 36, 52));
            addBalanceButton.setActionCommand(ADD_BALANCE);

            balanceInputPanel.add(addBalanceButton);

            add(balanceInputPanel);

            deleteAccountButton = new JButton("Delete Account");
            deleteAccountButton.setFont(new Font(FONT_TEXT, Font.ITALIC | Font.BOLD, 21));
            deleteAccountButton.setBackground(new Color(213, 0, 0));
            deleteAccountButton.setForeground(Color.WHITE);
            deleteAccountButton.setActionCommand(USER_ERASE_ACCOUNT);
            actionPanel.add(deleteAccountButton);

        }
        changePasswordButton = new JButton("Change Password");
        changePasswordButton.setFont(new Font(FONT_TEXT, Font.BOLD, 21));
        changePasswordButton.setBackground(new Color(70, 129, 137));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setActionCommand(USER_CHANGE_PASSWORD);
        actionPanel.add(changePasswordButton);

        getContentPane().add(actionPanel);
    }

    /**
     * Show change password dialog.
     *
     * @param controller the controller
     */
    public void showChangePasswordDialog(ActionListener controller) {
        changePwdDialog = new JDialog(this, "Change Password", true);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        oldPwdField = new JPasswordField();
        newPwdField = new JPasswordField();
        confirmPwdField = new JPasswordField();

        panel.add(new JLabel("Old Password:"));
        panel.add(oldPwdField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPwdField);
        panel.add(new JLabel("Confirm New Password:"));
        panel.add(confirmPwdField);

        // Create a wrapper panel with padding
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(new EmptyBorder(30, 40, 30, 40)); // top, left, bottom, right
        paddedPanel.add(panel, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.setActionCommand(CHANGE_PASSWORD_OK);
        okButton.addActionListener(controller);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> changePwdDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        changePwdDialog.getContentPane().add(paddedPanel, BorderLayout.CENTER);
        changePwdDialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        changePwdDialog.pack();
        changePwdDialog.setLocationRelativeTo(this);
        changePwdDialog.setVisible(true);
    }


    /**
     * Gets old pwd field.
     *
     * @return the old pwd field
     */
    public JPasswordField getOldPwdField() { return oldPwdField; }

    /**
     * Gets new pwd field.
     *
     * @return the new pwd field
     */
    public JPasswordField getNewPwdField() { return newPwdField; }

    /**
     * Gets confirm pwd field.
     *
     * @return the confirm pwd field
     */
    public JPasswordField getConfirmPwdField() { return confirmPwdField; }

    /**
     * Gets change pwd dialog.
     *
     * @return the change pwd dialog
     */
    public JDialog getChangePwdDialog() { return changePwdDialog; }




    private void configureFrame() {
        setTitle(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Register controller.
     *
     * @param listener the listener
     */
    public void registerController(ActionListener listener) {
        logOutButton.addActionListener(listener);
        if (deleteAccountButton != null) {
            deleteAccountButton.addActionListener(listener);
        }
        if (changePasswordButton != null) {
            changePasswordButton.addActionListener(listener);
        }
        if (addBalanceButton != null) {
            addBalanceButton.addActionListener(listener);
        }
    }

    public double getBalanceChange() {
        return Double.parseDouble(balanceField.getText());
    }

    public void resetBalanceChange() {
        balanceField.setText("");
    }
}
