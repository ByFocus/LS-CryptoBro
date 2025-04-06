package View;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public static final String FRAME_TITLE = "CryptoBro Login";

    public StartFrame() {
        configureFrame();

        // Crear el CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Agregar paneles de Login y Register al CardLayout
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");

        add(mainPanel, BorderLayout.CENTER);

        // Mostrar inicialmente el panel de Login
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(new Color(3, 25, 38)); // Fondo negro

        // Título
        JLabel titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setForeground(new Color(157, 190, 187)); // Texto blanco
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        loginPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central con campos de entrada
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.5;
        gbc.insets = new Insets(8, 25, 8, 40); // Espaciado interno

        // User Panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setOpaque(false); // Fondo negro
        JLabel userLabel = new JLabel("        User: ");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userLabel.setForeground(Color.WHITE); // Texto blanco
        userPanel.add(userLabel, BorderLayout.WEST);
        JTextField userField = new JTextField(15);
        userField.setBackground(Color.DARK_GRAY); // Fondo oscuro para el campo
        userField.setForeground(Color.WHITE);
        userField.setFont(new Font("Arial", Font.PLAIN, 18));
        userPanel.add(userField, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(userPanel, gbc);

        // Password Panel
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false); // Fondo negro
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setForeground(Color.WHITE); // Texto blanco
        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordField.setBackground(Color.DARK_GRAY); // Fondo oscuro para el campo
        passwordField.setForeground(Color.WHITE); // Texto blanco en el campo
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        gbc.gridy = 1;
        inputPanel.add(passwordPanel, gbc);

        // Botón de Login
        JPanel loginButtonPanel = new JPanel(new FlowLayout());
        JLabel loginButtonLabel = new JLabel("Login");
        loginButtonLabel.setForeground(Color.WHITE);
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 21));
        loginButton.setBackground(new Color(70, 129, 137, 255)); // Color dorado
        loginButton.setForeground(Color.WHITE);

        // Acción del botón Login
        loginButton.addActionListener(e -> {
            dispose();
            new MainFrame().setVisible(true);
        });

        loginButtonPanel.add(loginButton);
        loginButtonPanel.setPreferredSize(new Dimension(120, 80));
        loginButtonPanel.setOpaque(false);
        loginButtonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        gbc.gridy = 2;

        inputPanel.add(loginButtonPanel, gbc);
        loginPanel.add(inputPanel, BorderLayout.CENTER);

        // Panel para registrarse
        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.setOpaque(false);

        //Texto preguntando por registrarse
        JLabel registerLabel = new JLabel("Bro, ¿Todavía no eres parte de la familia?", JLabel.CENTER);
        registerLabel.setForeground(new Color(157, 190, 187)); // Texto blanco
        registerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        registerPanel.add(registerLabel, BorderLayout.NORTH);

        //Boton
        JPanel switchButtonPanel = new JPanel();
        switchButtonPanel.setOpaque(false);

        JButton switchToRegisterButton = new JButton("Register");
        switchToRegisterButton.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 21));
        switchToRegisterButton.setBackground(new Color(244, 233, 205)); // Color dorado
        switchToRegisterButton.setForeground(new Color(28, 36, 52)); // Texto negro en el botón

        switchToRegisterButton.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        switchButtonPanel.add(switchToRegisterButton);
        switchButtonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 40, 0));
        registerPanel.add(switchButtonPanel, BorderLayout.CENTER);

        loginPanel.add(registerPanel, BorderLayout.SOUTH);

        return loginPanel;
    }

    private JPanel createRegisterPanel() {
        // Crear el panel principal para el registro
        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.setBackground(new Color(157, 190, 187));

        // Título
        JLabel titleLabel = new JLabel("Register", JLabel.CENTER);
        titleLabel.setForeground(new Color(3, 25, 38));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        registerPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central con campos de entrada
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.5;
        gbc.insets = new Insets(8, 25, 8, 40);

        // User Panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("        User: ");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userLabel.setForeground(new Color(3, 25, 38));
        userPanel.add(userLabel, BorderLayout.WEST);

        JTextField userField = new JTextField(15);
        userField.setBackground(new Color(244, 233, 205));
        userField.setForeground(new Color(3, 25, 38));
        userField.setFont(new Font("Arial", Font.PLAIN, 18));
        userPanel.add(userField, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(userPanel, gbc);

        // Email Panel
        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.setOpaque(false); // Fondo transparente
        JLabel emailLabel = new JLabel("       Email: ");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        emailLabel.setForeground(new Color(3, 25, 38)); // Texto blanco
        emailPanel.add(emailLabel, BorderLayout.WEST);

        JTextField emailField = new JTextField(15);
        emailField.setBackground(new Color(244, 233, 205)); // Fondo oscuro para el campo
        emailField.setForeground(new Color(3, 25, 38));
        emailField.setFont(new Font("Arial", Font.PLAIN, 18));
        emailPanel.add(emailField, BorderLayout.CENTER);

        gbc.gridy = 1;
        inputPanel.add(emailPanel, gbc);

        // Password Panel
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false); // Fondo transparente
        JLabel passwordLabel = new JLabel("Password: ");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setForeground(new Color(3, 25, 38)); // Texto blanco
        passwordPanel.add(passwordLabel, BorderLayout.WEST);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setBackground(new Color(244, 233, 205)); // Fondo oscuro para el campo
        passwordField.setForeground(new Color(3, 25, 38));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        gbc.gridy = 2;
        inputPanel.add(passwordPanel, gbc);

        // Botón de Register
        JPanel registerButtonPanel = new JPanel(new FlowLayout());

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 21));
        registerButton.setBackground(new Color(119, 172, 162)); // Color similar al botón Login en createLoginPanel
        registerButton.setForeground(Color.WHITE);

        registerButton.addActionListener(e -> {
            // Acción al presionar "Register"
            dispose();
            new MainFrame().setVisible(true);
        });

        registerButtonPanel.add(registerButton);
        registerButtonPanel.setOpaque(false);
        registerButtonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        gbc.gridy = 3;
        inputPanel.add(registerButtonPanel, gbc);

        registerPanel.add(inputPanel, BorderLayout.CENTER);

        // Panel para volver al Login
        JPanel switchToLoginPanel = new JPanel(new BorderLayout());
        switchToLoginPanel.setOpaque(false);

        JLabel switchToLoginLabel = new JLabel("¿Te has colado bro?", JLabel.CENTER);
        switchToLoginLabel.setForeground(new Color(3, 25, 38));
        switchToLoginLabel.setFont(new Font("Arial", Font.BOLD, 18));
        switchToLoginPanel.add(switchToLoginLabel, BorderLayout.NORTH);

        JButton switchToLoginButton = new JButton("Login");
        switchToLoginButton.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 21));
        switchToLoginButton.setBackground(new Color(70, 129, 137));
        switchToLoginButton.setForeground(Color.WHITE);

        switchToLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        JPanel switchButtonWrapper = new JPanel();
        switchButtonWrapper.setOpaque(false);
        switchButtonWrapper.add(switchToLoginButton);
        switchButtonWrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 40, 0));

        switchToLoginPanel.add(switchButtonWrapper, BorderLayout.SOUTH);

        registerPanel.add(switchToLoginPanel, BorderLayout.SOUTH);

        return registerPanel;
    }

    private void configureFrame() {
        setTitle(FRAME_TITLE);
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }


}
