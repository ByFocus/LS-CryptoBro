package Presentation.View;

import Business.Entities.User;
import Presentation.View.Tabs.*;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    //Constantes con titulos, enlaces a fots y textos
    public static final String userProfileImgURL = "imgs/usuario.png";
    public static final String FONT = "Arial";

    public static final String FRAME_TITLE = "CryptoBro!";

    public static final String WALLET_TAB_TITLE = "Wallet";
    public static final String MARKET_TAB_TITLE = "Market";
    public static final String ADMIN_TAB_TITLE = "Admin";

    public static final String BALANCE_LABEL = "Balance: ";

    //Atributos
    private JPanel userPanel;
    private JLabel userNameLabel;
    private JLabel balanceCountLabel;

    private MainFrame(String userName, String balance) {
        configureFrame();
        userNameLabel = new JLabel(userName);
        balanceCountLabel = new JLabel(balance);
        userPanel = new JPanel();
    }
    public static MainFrame newUserMainFrame(User user) {
        MainFrame userFrame = new MainFrame(user.getUsername(), String.valueOf(user.getBalance()));
        userFrame.getContentPane().add(userFrame.configureProfile(), BorderLayout.NORTH);
        userFrame.configureTabs(false);
        return userFrame;
    }
    public static MainFrame newAdminMainFrame() {
        MainFrame adminFrame = new MainFrame("admin", "UNLIMITED");
        adminFrame.getContentPane().add(adminFrame.configureProfile(), BorderLayout.NORTH);
        adminFrame.configureTabs(true);
        return adminFrame;
    }
    private void configureFrame() {
        pack();
        setTitle(FRAME_TITLE);
        setSize(1100, 700);
        getContentPane().setBackground(new Color(3, 25, 38));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    //Pestañas con las acciones
    public void configureTabs(boolean admin) {
        JPanel marketPanel = new MarketTab();
        JPanel walletPanel = new JPanel();
        walletPanel.setBackground(new Color(119, 172, 162));
        JTabbedPane mainPanel = new JTabbedPane();
        mainPanel.addTab(WALLET_TAB_TITLE, walletPanel);
        if (admin) mainPanel.addTab(ADMIN_TAB_TITLE, marketPanel);
        else mainPanel.addTab(MARKET_TAB_TITLE, marketPanel);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    //Barra de arriba de la pagina
    private JPanel configureProfile() {
        JPanel userMenu = new JPanel(new BorderLayout(20, 20));
        userMenu.setOpaque(false);

        // Panel con el balance
        JPanel balancePanel = new JPanel();
        balancePanel.setOpaque(false);

        JLabel balanceLabel = new JLabel(BALANCE_LABEL);
        balanceLabel.setFont(new Font(FONT, Font.BOLD, 18));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setVerticalAlignment(JLabel.CENTER);
        balancePanel.add(balanceLabel);

        balanceCountLabel.setFont(new Font(FONT, Font.PLAIN, 18));
        balanceCountLabel.setForeground(Color.WHITE);
        balanceCountLabel.setVerticalAlignment(JLabel.CENTER);
        balancePanel.add(balanceCountLabel);


        userMenu.add(balancePanel, BorderLayout.WEST);

        // Panel del Usuario
        userPanel = new JPanel();
        userPanel.setOpaque(false);

        userNameLabel.setFont(new Font(FONT, Font.BOLD, 18));
        userNameLabel.setForeground(Color.WHITE);
        userPanel.add(userNameLabel);
        Image userProfileImg = new ImageIcon(userProfileImgURL).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel userProfile = new JLabel(new ImageIcon(userProfileImg));
        userPanel.add(userProfile);

        userMenu.add(userPanel, BorderLayout.EAST);
        userMenu.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return userMenu;
    }

    public JPanel registerController() {
        return userPanel;
    }
}