package View;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

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
        JPanel walletPanel = new JPanel();
        JTabbedPane mainPanel = new JTabbedPane();
        mainPanel.addTab("Market", marketPanel);
        mainPanel.addTab("Wallet", walletPanel);
        getContentPane().add(mainPanel);

        return mainPanel;
    }

    //Barra de arriba de la pagina
    private JPanel configureProfile() {
        JPanel userMenu = new JPanel(new BorderLayout(20, 20));

        // Panel con el balance
        JPanel balancePanel = new JPanel();
        
        JLabel balanceLabel = new JLabel("Balance: ");
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 15));
        balancePanel.add(balanceLabel);
        balancePanel.add(new JLabel("XXX "));

        userMenu.add(balancePanel, BorderLayout.WEST);

        // Panel del Usuario
        JPanel userPanel = new JPanel();
        userPanel.add(new JLabel("USER"));

        userMenu.add(userPanel, BorderLayout.EAST);
        userMenu.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        userMenu.setBackground(Color.DARK_GRAY);
        return userMenu;
    }
}
