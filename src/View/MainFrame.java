package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    private static final String userProfileImgURL = "imgs/follador.png";
    private static final String FRAME_TITLE = "CryptoBro!";

    private ViewController controller;

    public MainFrame(ViewController controller) {
        this.controller = controller;

        configureFrame();

        getContentPane().add(configureProfile(), BorderLayout.NORTH);
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
        JPanel marketPanel = new JPanel();
        marketPanel.setBackground(new Color(70, 129, 137));
        JPanel walletPanel = new JPanel();
        walletPanel.setBackground(new Color(119, 172, 162));
        JTabbedPane mainPanel = new JTabbedPane();
        mainPanel.addTab("Wallet", walletPanel);
        if (admin) mainPanel.addTab("Admin", marketPanel);
        else mainPanel.addTab("Market", marketPanel);

        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    //Barra de arriba de la pagina
    private JPanel configureProfile() {
        JPanel userMenu = new JPanel(new BorderLayout(20, 20));
        userMenu.setOpaque(false);

        // Panel con el balance
        JPanel balancePanel = new JPanel();
        balancePanel.setOpaque(false);

        JLabel balanceLabel = new JLabel("Balance: ");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setVerticalAlignment(JLabel.CENTER);
        balancePanel.add(balanceLabel);

        JLabel balanceCountLabel = new JLabel("0 ");
        balanceCountLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        balanceCountLabel.setForeground(Color.WHITE);
        balanceCountLabel.setVerticalAlignment(JLabel.CENTER);
        balancePanel.add(balanceCountLabel);


        userMenu.add(balancePanel, BorderLayout.WEST);

        // Panel del Usuario
        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("USER ");
        userLabel.setFont(new Font("Arial", Font.BOLD, 18));
        userLabel.setForeground(Color.WHITE);
        userPanel.add(userLabel);
        Image userProfileImg = new ImageIcon(userProfileImgURL).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel userProfile = new JLabel(new ImageIcon(userProfileImg));
        userPanel.add(userProfile);

        userPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.checkUserProfile();
            }
        });

        userMenu.add(userPanel, BorderLayout.EAST);
        userMenu.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return userMenu;
    }
}
