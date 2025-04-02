package View;

import javax.swing.*;

public class MainFrame extends JFrame {

    public static final String FRAME_TITLE = "CryptoBro!";

    public MainFrame() {

        JPanel marketPanel = new JPanel();
        JPanel walletPanel = new JPanel();
        JTabbedPane mainPanel = new JTabbedPane();
        mainPanel.addTab("Market", marketPanel);
        mainPanel.addTab("Wallet", walletPanel);
        getContentPane().add(mainPanel);

        configureFrame();
    }

    private void configureFrame() {
        pack();
        setTitle(FRAME_TITLE);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void configureTabs() {

    }
}
