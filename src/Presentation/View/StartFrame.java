package Presentation.View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class StartFrame extends JFrame {
    //Constantes con los eventos
    public static final String USER_LOGIN = "USER_LOGIN";
    public static final String USER_REGISTER = "USER_REGISTER";
    public static final String SWITCH_LOGIN = "SWITCH_LOGIN";
    public static final String SWITCH_REGISTER = "SWITCH_REGISTER";

    //Constantes con titulos, enlaces a fots y textos
    public static final String iconImgURL = "imgs/icono.png";

    public static final String FONT = "Arial";

    public static final String LOGIN_VIEW = "LOGIN";
    public static final String REGISTER_VIEW = "REGISTER";

    public static final String FRAME_TITLE = "CryptoBro Login";
    public static final String LOGIN_TITLE = "Login";
    public static final String REGISTER_TITLE = "Register";

    public static final String USER_INPUT_LABEL = "        User: ";
    public static final String PASSWORD_INPUT_LABEL = "Password: ";
    public static final String EMAIL_INPUT_LABEL= "       Email: ";

    public static final String ASK_FOR_LOGIN = "¿Te has colado bro?";
    public static final String ASK_FOR_REGISTER = "Bro, ¿Todavía no eres parte de la familia?";

    //Atributos
    private JTextField userLogInField;
    private JPasswordField passwordLogInField;
    private JTextField userRegisterField;
    private JPasswordField passwordRegisterField;
    private JTextField emailRegisterField;

    private JButton loginButton;
    private JButton switchToRegisterButton;
    private JButton registerButton;
    private JButton switchToLoginButton;

    private final JPanel mainPanel;
    private final CardLayout cardLayout;

    private String actualCard;

    public StartFrame() {
        configureFrame();

        // Crear el CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Agregar paneles de Login y Register al CardLayout
        mainPanel.add(createLoginPanel(), LOGIN_VIEW);
        mainPanel.add(createRegisterPanel(), REGISTER_VIEW);

        add(mainPanel, BorderLayout.CENTER);

        // Mostrar inicialmente el panel de Login
        actualCard = LOGIN_VIEW;
        cardLayout.show(mainPanel, LOGIN_VIEW);
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(new Color(3, 25, 38)); // Fondo negro

        // Título
        JLabel titleLabel = new JLabel(LOGIN_TITLE, JLabel.CENTER);
        titleLabel.setForeground(new Color(157, 190, 187)); // Texto blanco
        titleLabel.setFont(new Font(FONT, Font.BOLD, 32));
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
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel(USER_INPUT_LABEL);
        userLabel.setFont(new Font(FONT, Font.PLAIN, 18));
        userLabel.setForeground(Color.WHITE); // Texto blanco
        userPanel.add(userLabel, BorderLayout.WEST);
        userLogInField = new JTextField(15);
        userLogInField.setBackground(Color.DARK_GRAY);
        userLogInField.setForeground(Color.WHITE);
        userLogInField.setFont(new Font(FONT, Font.PLAIN, 18));
        userPanel.add(userLogInField, BorderLayout.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(userPanel, gbc);

        // Password Panel
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel(PASSWORD_INPUT_LABEL);
        passwordLabel.setFont(new Font(FONT, Font.PLAIN, 18));
        passwordLabel.setForeground(Color.WHITE);
        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        passwordLogInField = new JPasswordField(15);
        passwordLogInField.setFont(new Font(FONT, Font.PLAIN, 18));
        passwordLogInField.setBackground(Color.DARK_GRAY);
        passwordLogInField.setForeground(Color.WHITE);
        passwordPanel.add(passwordLogInField, BorderLayout.CENTER);
        gbc.gridy = 1;
        inputPanel.add(passwordPanel, gbc);

        //Botón de Login
        JPanel loginButtonPanel = new JPanel(new FlowLayout());

        loginButton = new JButton(LOGIN_TITLE);
        loginButton.setFont(new Font(FONT, Font.ITALIC | Font.BOLD, 21));
        loginButton.setBackground(new Color(70, 129, 137, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setActionCommand(USER_LOGIN);

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
        JLabel registerLabel = new JLabel(ASK_FOR_REGISTER, JLabel.CENTER);
        registerLabel.setForeground(new Color(157, 190, 187)); // Texto blanco
        registerLabel.setFont(new Font(FONT, Font.BOLD, 18));
        registerPanel.add(registerLabel, BorderLayout.NORTH);

        //Boton
        JPanel switchButtonPanel = new JPanel();
        switchButtonPanel.setOpaque(false);

        switchToRegisterButton = new JButton(REGISTER_TITLE);
        switchToRegisterButton.setFont(new Font(FONT, Font.ITALIC | Font.BOLD, 21));
        switchToRegisterButton.setBackground(new Color(244, 233, 205)); // Color dorado
        switchToRegisterButton.setForeground(new Color(28, 36, 52)); // Texto negro en el botón
        switchToRegisterButton.setActionCommand(SWITCH_REGISTER);

        switchButtonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 40, 0));
        switchButtonPanel.add(switchToRegisterButton);
        registerPanel.add(switchButtonPanel, BorderLayout.CENTER);

        loginPanel.add(registerPanel, BorderLayout.SOUTH);

        return loginPanel;
    }

    private JPanel createRegisterPanel() {
        // Crear el panel principal para el registro
        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.setBackground(new Color(157, 190, 187));

        // Título
        JLabel titleLabel = new JLabel(REGISTER_TITLE, JLabel.CENTER);
        titleLabel.setForeground(new Color(3, 25, 38));
        titleLabel.setFont(new Font(FONT, Font.BOLD, 32));
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
        JLabel userLabel = new JLabel(USER_INPUT_LABEL);
        userLabel.setFont(new Font(FONT, Font.PLAIN, 18));
        userLabel.setForeground(new Color(3, 25, 38));
        userPanel.add(userLabel, BorderLayout.WEST);

        userRegisterField = new JTextField(15);
        userRegisterField.setBackground(new Color(244, 233, 205));
        userRegisterField.setForeground(new Color(3, 25, 38));
        userRegisterField.setFont(new Font(FONT, Font.PLAIN, 18));
        userPanel.add(userRegisterField, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(userPanel, gbc);

        // Email Panel
        JPanel emailPanel = new JPanel(new BorderLayout());
        emailPanel.setOpaque(false);
        JLabel emailLabel = new JLabel(EMAIL_INPUT_LABEL);
        emailLabel.setFont(new Font(FONT, Font.PLAIN, 18));
        emailLabel.setForeground(new Color(3, 25, 38));
        emailPanel.add(emailLabel, BorderLayout.WEST);

        emailRegisterField = new JTextField(15);
        emailRegisterField.setBackground(new Color(244, 233, 205));
        emailRegisterField.setForeground(new Color(3, 25, 38));
        emailRegisterField.setFont(new Font(FONT, Font.PLAIN, 18));
        emailPanel.add(emailRegisterField, BorderLayout.CENTER);

        gbc.gridy = 1;
        inputPanel.add(emailPanel, gbc);

        // Password Panel
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel(PASSWORD_INPUT_LABEL);
        passwordLabel.setFont(new Font(FONT, Font.PLAIN, 18));
        passwordLabel.setForeground(new Color(3, 25, 38));
        passwordPanel.add(passwordLabel, BorderLayout.WEST);

        passwordRegisterField = new JPasswordField(15);
        passwordRegisterField.setBackground(new Color(244, 233, 205));
        passwordRegisterField.setForeground(new Color(3, 25, 38));
        passwordRegisterField.setFont(new Font(FONT, Font.PLAIN, 18));
        passwordPanel.add(passwordRegisterField, BorderLayout.CENTER);

        gbc.gridy = 2;
        inputPanel.add(passwordPanel, gbc);

        // Botón de Register
        JPanel registerButtonPanel = new JPanel(new FlowLayout());

        registerButton = new JButton(REGISTER_TITLE);
        registerButton.setFont(new Font(FONT, Font.ITALIC | Font.BOLD, 21));
        registerButton.setBackground(new Color(3, 25, 38));
        registerButton.setForeground(Color.WHITE);
        registerButton.setActionCommand(USER_REGISTER);

        registerButtonPanel.add(registerButton);
        registerButtonPanel.setOpaque(false);
        registerButtonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        gbc.gridy = 3;
        inputPanel.add(registerButtonPanel, gbc);

        registerPanel.add(inputPanel, BorderLayout.CENTER);

        // Panel para volver al Login
        JPanel switchToLoginPanel = new JPanel(new BorderLayout());
        switchToLoginPanel.setOpaque(false);

        JLabel switchToLoginLabel = new JLabel(ASK_FOR_LOGIN, JLabel.CENTER);
        switchToLoginLabel.setForeground(new Color(3, 25, 38));
        switchToLoginLabel.setFont(new Font(FONT, Font.BOLD, 18));
        switchToLoginPanel.add(switchToLoginLabel, BorderLayout.NORTH);

        switchToLoginButton = new JButton(LOGIN_TITLE);
        switchToLoginButton.setFont(new Font(FONT, Font.ITALIC | Font.BOLD, 21));
        switchToLoginButton.setBackground(new Color(70, 129, 137));
        switchToLoginButton.setForeground(Color.WHITE);
        switchToLoginButton.setActionCommand(SWITCH_LOGIN);

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
        setIconImage(new ImageIcon(iconImgURL).getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void switchView(String view) {
        actualCard = view;
        cardLayout.show(mainPanel, view);
    }

    public void reset() {
        userLogInField.setText("");
        passwordLogInField.setText("");
        userRegisterField.setText("");
        passwordRegisterField.setText("");
        emailRegisterField.setText("");
    }

    public String getNameInput() {
        String text;

        if (actualCard.equals(LOGIN_VIEW)) text = userLogInField.getText();
        else text = userRegisterField.getText();

        return text;
    }

    public String getPasswordInput() {
        String text;

        if (actualCard.equals(LOGIN_VIEW)) text = new String(passwordLogInField.getPassword());
        else text = new String(passwordRegisterField.getPassword());
        return text;
    }

    public String getEmailInput() {
        return emailRegisterField.getText();
    }

    public void registerController(ActionListener listener) {
        loginButton.addActionListener(listener);
        registerButton.addActionListener(listener);
        switchToLoginButton.addActionListener(listener);
        switchToRegisterButton.addActionListener(listener);
    }
}