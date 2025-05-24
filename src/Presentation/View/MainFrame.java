package Presentation.View;

import Business.Entities.Crypto;
import Presentation.Controllers.AdminTabController;
import Presentation.Controllers.MarketTabController;
import Presentation.Controllers.WalletTabController;
import Presentation.View.Panels.RoundedPanel;
import Presentation.View.Tabs.*;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 * The type Main frame, it is the main window of the CryptoBro application.
 * It provides the graphical user interface (GUI) where users can view and manage
 * their wallet, market data, and, if they're an admin, perform administrative actions.
 */
public class MainFrame extends JFrame {
    //Constantes con titulos, enlaces a fots y textos
    private static final String userProfileImgURL = "imgs/usuario.png";
    private static final String iconImgURL = "imgs/icono.png";

    private static final String FONT = "Arial";

    private static final String FRAME_TITLE = "CryptoBro!";

    private static final String WALLET_TAB_TITLE = "Wallet";
    private static final String MARKET_TAB_TITLE = "Market";
    private static final String ADMIN_TAB_TITLE = "Admin";

    private static final String BALANCE_LABEL = "Balance: ";
    private static final String PROFIT_LABEL = "     Profit: ";

    //Atributos
    private JPanel userPanel;
    private final JLabel userNameLabel;
    private final JLabel balanceCountLabel;
    private final JLabel gainsCountLabel;

    private JTabbedPane mainPanel;
    private WalletTab walletPanel;
    private MarketTab marketPanel;
    private AdminTab adminPanel;

    /**
     * Constructs the main application frame for the user or admin.
     * It sets up the window configuration, initializes user info (username, balance, and gains),
     * and prepares the top profile panel.
     *
     * @param identifier the user's name or "admin"
     * @param balance    the current account balance
     * @param gains      the current estimated gains
     */
    public MainFrame(String identifier, String balance, String gains) {
        configureFrame();
        userNameLabel = new JLabel(identifier);
        balanceCountLabel = new JLabel();
        balanceCountLabel.setText(balance);
        gainsCountLabel = new JLabel();
        try {
            setEstimatedGains(Double.parseDouble(gains));
        } catch (NumberFormatException e) {
            gainsCountLabel.setText(gains);
        }


        userPanel = new JPanel();
        this.getContentPane().add(configureProfile(), BorderLayout.NORTH);
    }

    /**
     * Configures the basic properties of the main application frame:
     * title, size, icon, background color, close operation, and layout.
     */
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

    /**
     * Configures the tabbed interface for the main application window.
     * If the user is an admin, includes admin functionalities.
     *
     * @param admin true if the user is an admin, false otherwise
     */
    public void configureTabs(boolean admin) {
        //MODIFICACIONES DE LAS CARACTERISTICAS DE UN TABBED PANE
        UIManager.put("TabbedPane.tabInsets", new Insets(5, 10, 5, 10));
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.selectedLabelShift", 0);
        UIManager.put("TabbedPane.labelShift", 0);
        UIManager.put("TabbedPane.tabAreaBackground", new Color(28, 36, 52));

        marketPanel = MarketTabController.getInstance().getMarketTab(admin);
        mainPanel = new JTabbedPane();

        mainPanel.addTab(MARKET_TAB_TITLE, marketPanel);
        if (admin) {
            adminPanel = AdminTabController.getInstance().getAdminTab();
            mainPanel.addTab(ADMIN_TAB_TITLE, adminPanel);
        }
        else {
            walletPanel = WalletTabController.getInstance().getWalletTab();
            mainPanel.addTab(WALLET_TAB_TITLE, walletPanel);
        }

        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Builds the top profile bar with the user's name, balance, and estimated gains.
     * Includes profile picture and stylized user info panel.
     *
     * @return a configured JPanel representing the profile section
     */
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

        JLabel profitLabel = new JLabel(PROFIT_LABEL);
        profitLabel.setFont(new Font(FONT, Font.BOLD, 18));
        profitLabel.setForeground(Color.WHITE);
        profitLabel.setVerticalAlignment(JLabel.CENTER);
        balancePanel.add(profitLabel);

        gainsCountLabel.setFont(new Font(FONT, Font.PLAIN, 18));
        gainsCountLabel.setForeground(Color.WHITE);
        gainsCountLabel.setVerticalAlignment(JLabel.CENTER);
        balancePanel.add(gainsCountLabel);

        userMenu.add(balancePanel, BorderLayout.WEST);

        // Panel del Usuario
        userPanel = new RoundedPanel(20);
        userPanel.setBackground(new Color(244, 233, 205));
        userPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

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

    /**
     * Returns the user profile panel, used for attaching listeners or modifying display.
     *
     * @return the user profile panel
     */
    public JPanel getUserPanel() {
        return userPanel;
    }

    /**
     * Updates the balance label with the provided amount, formatted to two decimal places.
     *
     * @param balance the user's current balance
     */
    public void setBalance(double balance) {
        balanceCountLabel.setText(String.format("%.2f€", balance));
    }

    /**
     * Updates the estimated gains label and changes its color based on gain value:
     * - Green for positive
     * - Red for negative
     * - White for zero
     *
     * @param estimatedGains the user's estimated profit or loss
     */
    public void setEstimatedGains(double estimatedGains) {
        String gainsText;
        DecimalFormat priceFormat = new DecimalFormat("#.#####€");
        if (estimatedGains < 0) {
            gainsCountLabel.setForeground(Color.RED);
            gainsText = priceFormat.format(estimatedGains);
        } else if (estimatedGains != 0){
            gainsCountLabel.setForeground(Color.GREEN);
            gainsText = "+"+priceFormat.format(estimatedGains);
        } else {
            gainsCountLabel.setForeground(Color.WHITE);
            gainsText = "Ninguno";
        }
        gainsCountLabel.setText(gainsText);
    }

    /**
     * Closes and cleans up the main frame by removing tabs and resetting the panels.
     */
    public void close() {
        int index = mainPanel.indexOfComponent(marketPanel);
        mainPanel.removeTabAt(index);
        marketPanel = null;

        if (walletPanel != null) {
            index = mainPanel.indexOfComponent(walletPanel);
            mainPanel.removeTabAt(index);
            walletPanel = null;
        }

        if (adminPanel != null) {
            index = mainPanel.indexOfComponent(adminPanel);
            mainPanel.removeTabAt(index);
            adminPanel = null;
        }

        mainPanel = null;
        this.dispose();
    }
}