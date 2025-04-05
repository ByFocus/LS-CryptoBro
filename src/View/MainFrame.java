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
        JPanel userPanel = new JPanel();

        // Panel con el balance
        JPanel BalancePanel = new JPanel();
        BalancePanel.add(new JLabel("Balance: "));
        BalancePanel.add(new JLabel("XXX "));


        userPanel.add(BalancePanel);

        // Panel del Usuario
        JPanel UserPanel = new JPanel(new BorderLayout());
        BalancePanel.add(new JLabel("USER"), BorderLayout.WEST);

        userPanel.add(BalancePanel);

        return userPanel;
    }
}
