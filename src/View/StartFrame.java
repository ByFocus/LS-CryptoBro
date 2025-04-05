package View;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public static final String FRAME_TITLE = "CryptoBro Login";

    public StartFrame() {
        setTitle("CryptoBro Login");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Crear el CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Agregar paneles de Login y Register al CardLayout
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");

        add(mainPanel, BorderLayout.CENTER);

        // Mostrar inicialmente el panel de Login
        cardLayout.show(mainPanel, "Login");

        setLocationRelativeTo(null);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(Color.BLACK); // Fondo negro

        // Título
        JLabel titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE); // Texto blanco
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente personalizada
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        loginPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central con campos de entrada
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.BLACK); // Fondo negro
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.5;
        gbc.insets = new Insets(8, 25, 8, 25); // Espaciado interno

        // User Panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(Color.BLACK); // Fondo negro
        JLabel userLabel = new JLabel("User:          ");
        userLabel.setForeground(Color.WHITE); // Texto blanco
        userPanel.add(userLabel, BorderLayout.WEST);
        JTextField userField = new JTextField(15);
        userField.setBackground(Color.DARK_GRAY); // Fondo oscuro para el campo
        userField.setForeground(Color.WHITE); // Texto blanco en el campo
        userPanel.add(userField, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(userPanel, gbc);

        // Password Panel
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setBackground(Color.BLACK); // Fondo negro
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setForeground(Color.WHITE); // Texto blanco
        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBackground(Color.DARK_GRAY); // Fondo oscuro para el campo
        passwordField.setForeground(Color.WHITE); // Texto blanco en el campo
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        gbc.gridy = 1;
        inputPanel.add(passwordPanel, gbc);

        // Botón de Login
        JPanel loginButtonPanel = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(255, 185, 0)); // Color dorado
        loginButton.setForeground(Color.BLACK); // Texto negro en el botón

        // Acción del botón Login (puedes personalizarla)
        loginButton.addActionListener(e -> {
            dispose();
            new MainFrame().setVisible(true);
        });

        loginButtonPanel.add(loginButton);
        loginButtonPanel.setPreferredSize(new Dimension(120, 50));
        loginButtonPanel.setOpaque(false);
        gbc.gridy = 2;

        inputPanel.add(loginButtonPanel, gbc);

        loginPanel.add(inputPanel, BorderLayout.CENTER);

        // Botón para cambiar a Register
        JButton switchToRegisterButton = new JButton("Register");
        switchToRegisterButton.setBackground(new Color(218, 165, 32)); // Color dorado
        switchToRegisterButton.setForeground(Color.BLACK); // Texto negro en el botón

        switchToRegisterButton.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        JPanel switchButtonPanel = new JPanel();
        switchButtonPanel.setBackground(Color.BLACK); // Fondo negro
        switchButtonPanel.add(switchToRegisterButton);
        loginPanel.add(switchButtonPanel, BorderLayout.SOUTH);

        return loginPanel;
    }

    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel(new BorderLayout());

        // Título
        JLabel titleLabel = new JLabel("Register", JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente personalizada
        registerPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central con campos de entrada
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 25, 8, 25); // Espaciado interno
        gbc.weightx = 2; // Permitir expansión horizontal

        // User Panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.add(new JLabel("User:          "), BorderLayout.WEST);
        userPanel.add(new JTextField(20), BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(userPanel, gbc);

        // Email Panel
        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.add(new JLabel("Email:         "), BorderLayout.WEST);
        emailPanel.add(new JTextField(20), BorderLayout.CENTER);
        gbc.gridy = 1;
        inputPanel.add(emailPanel, gbc);

        // Password Panel
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(new JLabel("Password: "), BorderLayout.WEST);
        passwordPanel.add(new JPasswordField(20), BorderLayout.CENTER);
        gbc.gridy = 2;
        inputPanel.add(passwordPanel, gbc);

        // RegisterButton Panel
        JPanel registerButton = new JPanel(new FlowLayout());
        JButton loginButton = new JButton("Register");
        loginButton.addActionListener(e -> {
            dispose();
            new MainFrame().setVisible(true);
        });
        registerButton.add(loginButton);
        registerPanel.setPreferredSize(new Dimension(120, 50));
        gbc.gridy = 3;

        inputPanel.add(registerButton, gbc);

        registerPanel.add(inputPanel, BorderLayout.CENTER);

        // Botón para cambiar a Login
        JButton switchToLoginButton = new JButton("Login");

        switchToLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        JPanel switchButtonPanel = new JPanel();
        switchButtonPanel.add(switchToLoginButton);
        registerPanel.add(switchButtonPanel, BorderLayout.SOUTH);

        return registerPanel;
    }

    private void configureFrame() {
        pack();
        setTitle(FRAME_TITLE);
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }


}
