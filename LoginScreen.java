import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Login and registration screen for the mini-games platform
 */
public class LoginScreen extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;
    
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JPasswordField confirmPasswordField;
    
    private Color backgroundColor = new Color(40, 44, 52);
    private Color accentColor = new Color(97, 175, 239);
    private Color textColor = new Color(220, 223, 228);
    
    public LoginScreen() {
        setTitle("Mini-Games Collection - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Main panel with background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Create gradient background
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(30, 34, 42), 
                    getWidth(), getHeight(), new Color(50, 54, 62)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        // Create login and register panels
        createLoginPanel();
        createRegisterPanel();
        
        // Add panels to card layout
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        
        // Show login panel initially
        cardLayout.show(mainPanel, "login");
        
        setContentPane(mainPanel);
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel(new BorderLayout(20, 20));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Title
        JLabel titleLabel = new JLabel("MINI-GAMES COLLECTION");
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Login to Your Account");
        subtitleLabel.setForeground(accentColor);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(textColor);
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        loginUsernameField = createStyledTextField();
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(textColor);
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        loginPasswordField = new JPasswordField();
        styleTextField(loginPasswordField);
        
        // Login button
        JButton loginButton = new JButton("LOGIN");
        styleButton(loginButton, new Color(97, 175, 239));
        loginButton.addActionListener(e -> handleLogin());
        
        // Register link
        JButton registerLinkButton = new JButton("Don't have an account? Register here");
        registerLinkButton.setForeground(new Color(150, 153, 158));
        registerLinkButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerLinkButton.setBorderPainted(false);
        registerLinkButton.setContentAreaFilled(false);
        registerLinkButton.setFocusPainted(false);
        registerLinkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLinkButton.addActionListener(e -> cardLayout.show(mainPanel, "register"));
        
        // Add components to form
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(loginUsernameField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(loginPasswordField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(loginButton);
        
        // Center components
        for (Component c : formPanel.getComponents()) {
            if (c instanceof JComponent) {
                ((JComponent) c).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }
        
        // Add everything to main panel
        loginPanel.add(titlePanel, BorderLayout.NORTH);
        loginPanel.add(formPanel, BorderLayout.CENTER);
        loginPanel.add(registerLinkButton, BorderLayout.SOUTH);
    }
    
    private void createRegisterPanel() {
        registerPanel = new JPanel(new BorderLayout(20, 20));
        registerPanel.setOpaque(false);
        registerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Title
        JLabel titleLabel = new JLabel("MINI-GAMES COLLECTION");
        titleLabel.setForeground(textColor);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Create New Account");
        subtitleLabel.setForeground(accentColor);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        
        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(textColor);
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        registerUsernameField = createStyledTextField();
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(textColor);
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        registerPasswordField = new JPasswordField();
        styleTextField(registerPasswordField);
        
        // Confirm password field
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setForeground(textColor);
        confirmLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        confirmPasswordField = new JPasswordField();
        styleTextField(confirmPasswordField);
        
        // Register button
        JButton registerButton = new JButton("CREATE ACCOUNT");
        styleButton(registerButton, new Color(152, 195, 121));
        registerButton.addActionListener(e -> handleRegister());
        
        // Login link
        JButton loginLinkButton = new JButton("Already have an account? Login here");
        loginLinkButton.setForeground(new Color(150, 153, 158));
        loginLinkButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loginLinkButton.setBorderPainted(false);
        loginLinkButton.setContentAreaFilled(false);
        loginLinkButton.setFocusPainted(false);
        loginLinkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLinkButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        
        // Add components to form
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(registerUsernameField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(registerPasswordField);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(confirmLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(registerButton);
        
        // Center components
        for (Component c : formPanel.getComponents()) {
            if (c instanceof JComponent) {
                ((JComponent) c).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }
        
        // Add everything to main panel
        registerPanel.add(titlePanel, BorderLayout.NORTH);
        registerPanel.add(formPanel, BorderLayout.CENTER);
        registerPanel.add(loginLinkButton, BorderLayout.SOUTH);
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(textColor);
        field.setBackground(new Color(30, 34, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 64, 72), 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setCaretColor(textColor);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }
    
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    }
    
    private void handleLogin() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (UserManager.getInstance().loginUser(username, password)) {
            // Login successful
            dispose();
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleRegister() {
        String username = registerUsernameField.getText().trim();
        String password = new String(registerPasswordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (UserManager.getInstance().registerUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", 
                    "Registration", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "login");
            
            // Pre-fill the login form with the registered username
            loginUsernameField.setText(username);
            loginPasswordField.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}