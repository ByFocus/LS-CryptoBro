package Presentation.View;

import Business.Entities.Crypto;
import Business.Entities.User;
import Presentation.View.Tabs.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    //Constantes con titulos, enlaces a fots y textos
    public static final String userProfileImgURL = "imgs/usuario.png";
    public static final String iconImgURL = "imgs/icono.png";

    public static final String FONT = "Arial";

    public static final String FRAME_TITLE = "CryptoBro!";

    public static final String WALLET_TAB_TITLE = "Wallet";
    public static final String MARKET_TAB_TITLE = "Market";
    public static final String ADMIN_TAB_TITLE = "Admin";

    public static final String BALANCE_LABEL = "Balance: ";

    //Atributos
    private JPanel userPanel;
    private final JLabel userNameLabel;
    private final JLabel balanceCountLabel;

    private JPanel walletPanel;
    private MarketTab marketPanel;
    private JPanel adminPanel;

    private MainFrame(String userName, String balance) {
        configureFrame();
        userNameLabel = new JLabel(userName);
        balanceCountLabel = new JLabel(balance);
        userPanel = new JPanel();
    }

    public static MainFrame configureApp(String userName, String balance) {
        MainFrame userFrame = new MainFrame(userName, balance);
        userFrame.getContentPane().add(userFrame.configureProfile(), BorderLayout.NORTH);
        return userFrame;
    }

    private void configureFrame() {
        pack();
        setTitle(FRAME_TITLE);
        setIconImage(new ImageIcon(iconImgURL).getImage());
        setSize(1100, 700);
        getContentPane().setBackground(new Color(3, 25, 38));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    //Pestañas con las acciones
    public void configureTabs(List<Crypto> cryptoList, boolean admin) {
        marketPanel = new MarketTab(cryptoList);

        walletPanel = new JPanel();
        walletPanel.setBackground(new Color(119, 172, 162));

        adminPanel = new JPanel();
        adminPanel.setBackground(new Color(3, 25, 38));

        JTabbedPane mainPanel = new JTabbedPane();

        mainPanel.addTab(WALLET_TAB_TITLE, walletPanel);
        if (admin) mainPanel.addTab(ADMIN_TAB_TITLE, adminPanel);
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
        userPanel.setBackground(new Color(244, 233, 205));

        userNameLabel.setFont(new Font(FONT, Font.BOLD, 18));
        userNameLabel.setForeground(new Color(3, 25, 38));
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

    public JTable getTable() {
        return marketPanel.getTablaData();
    }
    public void refreshMarket(List<Crypto> cryptoList) {
        marketPanel.loadCryptoData(cryptoList);
    }

    public void setBalance(String balance) {
        balanceCountLabel.setText(balance);
    }
}