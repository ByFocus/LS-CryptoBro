package View;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String userProfileImgURL = "imgs/follador.png";
    public static final String FRAME_TITLE = "CryptoBro!";

    public MainFrame() {
        configureFrame();

        getContentPane().add(configureProfile(), BorderLayout.NORTH);
        getContentPane().add(configureTabs(), BorderLayout.CENTER);
    }

    private void configureFrame() {
        pack();
        setTitle(FRAME_TITLE);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
    }

    //Pestañas con las acciones
    private JTabbedPane configureTabs() {
        JPanel marketPanel = new JPanel();
        marketPanel.setBackground(new Color(70, 129, 137));
        JPanel walletPanel = new JPanel();
        walletPanel.setBackground(new Color(70, 129, 137));
        JTabbedPane mainPanel = new JTabbedPane();
        mainPanel.addTab("Market", marketPanel);
        mainPanel.addTab("Wallet", walletPanel);
        getContentPane().add(mainPanel);

        return mainPanel;
    }

    //Barra de arriba de la pagina
    private JPanel configureProfile() {
        JPanel userMenu = new JPanel(new BorderLayout(20, 20));
        userMenu.setBackground(new Color(3, 25, 38));

        // Panel con el balance
        JPanel balancePanel = new JPanel();
        balancePanel.setOpaque(false);

        JLabel balanceLabel = new JLabel("Balance: ");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setForeground(new Color(244, 233, 205));
        balanceLabel.setVerticalAlignment(JLabel.CENTER);
        balancePanel.add(balanceLabel);

        JLabel balanceCountLabel = new JLabel("XXX ");
        balanceCountLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        balanceCountLabel.setForeground(new Color(244, 233, 205));
        balanceCountLabel.setVerticalAlignment(JLabel.CENTER);
        balancePanel.add(balanceCountLabel);


        userMenu.add(balancePanel, BorderLayout.WEST);

        // Panel del Usuario
        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("USER ");
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userLabel.setForeground(new Color(244, 233, 205));
        userPanel.add(userLabel);
        Image userProfileImg = new ImageIcon(userProfileImgURL).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel userProfile = new JLabel(new ImageIcon(userProfileImg));
        userPanel.add(userProfile);
        userMenu.add(userPanel, BorderLayout.EAST);
        userMenu.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return userMenu;
    }
}
