import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * Main menu class with improved ergonomics and visual design
 */
public class MainMenu extends JFrame {
    private JLabel pointsLabel;
    private JLabel usernameLabel;
    
    public MainMenu() {
        StyleUtils.applyGameStyle(this, "Mini-Games Collection");
        
        // Main panel with custom background
        JPanel mainPanel = StyleUtils.createGradientPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Center content with some padding
        JPanel contentPanel = new JPanel(new BorderLayout(0, 40));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Create the header panel (improved layout)
        JPanel headerPanel = createHeaderPanel();
        
        // Game selection panel with improved layout
        JPanel gamesPanel = createGamesPanel();
        
        // Footer with stats and logout
        JPanel footerPanel = createFooterPanel();
        
        // Assemble the UI
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(gamesPanel, BorderLayout.CENTER);
        contentPanel.add(footerPanel, BorderLayout.SOUTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        // Create an attractive header with better spacing
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        
        // App logo/image at the top
        JLabel logoLabel = new JLabel(createLogoIcon());
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Main title
        JLabel titleLabel = StyleUtils.createTitleLabel("MINI-GAMES COLLECTION");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Welcome message with username
        String username = UserManager.getInstance().getCurrentUser().getUsername();
        usernameLabel = new JLabel("Welcome, " + username + "!");
        usernameLabel.setForeground(StyleUtils.TEXT_DIM);
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Points display in an attractive panel
        JPanel pointsPanel = StyleUtils.createRoundPanel(new Color(60, 64, 72, 200), 15);
        pointsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        int points = UserManager.getInstance().getCurrentUser().getTotalPoints();
        pointsLabel = new JLabel("Points: " + points);
        pointsLabel.setForeground(StyleUtils.ACCENT_GREEN);
        pointsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pointsLabel.setIcon(StyleUtils.createCoinIcon());
        
        pointsPanel.add(pointsLabel);
        pointsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pointsPanel.setMaximumSize(new Dimension(200, 40));
        
        // Add all components with proper spacing
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(usernameLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(pointsPanel);
        
        return headerPanel;
    }
    
    private JPanel createGamesPanel() {
        JPanel gamesPanel = new JPanel(new BorderLayout(0, 20));
        gamesPanel.setOpaque(false);
        
        // Section title
        JLabel sectionLabel = new JLabel("SELECT A GAME");
        sectionLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Game cards with more organized layout
        JPanel cardsContainer = new JPanel(new GridLayout(1, 2, 30, 0));
        cardsContainer.setOpaque(false);
        
        // Create improved game cards
        cardsContainer.add(createGameCard(
            "Memory Game", 
            createMemoryGameImage(), 
            "Easy (25 points)",
            "Match pairs of cards to test your memory",
            StyleUtils.ACCENT_GREEN,
            e -> launchGame(new MemoryGame())
        ));
        
        cardsContainer.add(createGameCard(
            "Snake Game", 
            createSnakeGameImage(), 
            "Medium (50 points)",
            "Control the snake to eat food and grow longer",
            StyleUtils.ACCENT_YELLOW,
            e -> launchGame(new SnakeGame())
        ));
        
        gamesPanel.add(sectionLabel, BorderLayout.NORTH);
        gamesPanel.add(cardsContainer, BorderLayout.CENTER);
        
        return gamesPanel;
    }
    
    private JPanel createFooterPanel() {
    JPanel footerPanel = new JPanel(new BorderLayout(20, 0));
    footerPanel.setOpaque(false);
    
    // Game stats area
    JPanel statsPanel = StyleUtils.createRoundPanel(new Color(60, 64, 72, 150), 10);
    statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
    
    User user = UserManager.getInstance().getCurrentUser();
    
    // Memory game stats
    JPanel memoryStatsPanel = new JPanel();
    memoryStatsPanel.setOpaque(false);
    memoryStatsPanel.setLayout(new BoxLayout(memoryStatsPanel, BoxLayout.Y_AXIS));
    
    JLabel memoryTitle = new JLabel("Memory Game");
    memoryTitle.setForeground(StyleUtils.ACCENT_GREEN);
    memoryTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
    memoryTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    JLabel memoryScore = new JLabel("Best Score: " + user.getGameScore("Memory Game"));
    memoryScore.setForeground(StyleUtils.TEXT_BRIGHT);
    memoryScore.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    memoryScore.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    memoryStatsPanel.add(memoryTitle);
    memoryStatsPanel.add(memoryScore);
    
    // Snake game stats
    JPanel snakeStatsPanel = new JPanel();
    snakeStatsPanel.setOpaque(false);
    snakeStatsPanel.setLayout(new BoxLayout(snakeStatsPanel, BoxLayout.Y_AXIS));
    
    JLabel snakeTitle = new JLabel("Snake Game");
    snakeTitle.setForeground(StyleUtils.ACCENT_YELLOW);
    snakeTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
    snakeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    JLabel snakeScore = new JLabel("Best Score: " + user.getGameScore("Snake Game"));
    snakeScore.setForeground(StyleUtils.TEXT_BRIGHT);
    snakeScore.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    snakeScore.setAlignmentX(Component.CENTER_ALIGNMENT);
    
    snakeStatsPanel.add(snakeTitle);
    snakeStatsPanel.add(snakeScore);
    
    statsPanel.add(memoryStatsPanel);
    statsPanel.add(snakeStatsPanel);
    
    // Button Panel for Actions
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
    buttonPanel.setOpaque(false);
    
    // Space Profile button
    JButton spaceProfileButton = StyleUtils.createStyledButton("SPACE PROFILE", new Color(100, 150, 255));
    spaceProfileButton.addActionListener(e -> {
        setVisible(false);
        SpaceProfileScreen spaceScreen = new SpaceProfileScreen(this);
        spaceScreen.setVisible(true);
    });
    
    // Logout button
    JButton logoutButton = StyleUtils.createStyledButton("LOGOUT", StyleUtils.ACCENT_RED);
    logoutButton.addActionListener(e -> {
        UserManager.getInstance().logoutUser();
        dispose();
        new LoginScreen().setVisible(true);
    });
    
    buttonPanel.add(spaceProfileButton);
    buttonPanel.add(logoutButton);
    
    footerPanel.add(statsPanel, BorderLayout.CENTER);
    footerPanel.add(buttonPanel, BorderLayout.EAST);
    
    return footerPanel;
}
    
    private JPanel createGameCard(String title, Image image, String difficulty, 
                                 String description, Color accentColor, ActionListener action) {
        // Create a more attractive rounded panel for the game card
        JPanel cardPanel = StyleUtils.createRoundPanel(new Color(50, 54, 62), 20);
        cardPanel.setLayout(new BorderLayout(0, 10));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Add a colored top strip
        JPanel colorStrip = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(accentColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.dispose();
            }
        };
        colorStrip.setPreferredSize(new Dimension(0, 8));
        colorStrip.setOpaque(false);
        
        // Game image with better centering
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Title panel with name and difficulty
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel diffLabel = new JLabel(difficulty);
        diffLabel.setForeground(accentColor);
        diffLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html><div style='text-align: center;'>" + description + "</div></html>");
        descLabel.setForeground(StyleUtils.TEXT_DIM);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(diffLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(descLabel);
        
        // Play button
        JButton playButton = StyleUtils.createStyledButton("PLAY NOW", accentColor);
        playButton.addActionListener(action);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setMaximumSize(new Dimension(150, 40));
        
        // Button panel for centering
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(playButton);
        
        // Assemble card components
        cardPanel.add(colorStrip, BorderLayout.NORTH);
        cardPanel.add(imageLabel, BorderLayout.CENTER);
        cardPanel.add(titlePanel, BorderLayout.SOUTH);
        cardPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add hover effect
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cardPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(accentColor, 2, true)
                ));
                cardPanel.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                cardPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                cardPanel.repaint();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                action.actionPerformed(new ActionEvent(cardPanel, ActionEvent.ACTION_PERFORMED, "clicked"));
            }
        });
        
        return cardPanel;
    }
    
    private ImageIcon createLogoIcon() {
        // Create a simple logo
        int width = 80;
        int height = 80;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw circular background
        g2d.setColor(StyleUtils.ACCENT_BLUE);
        g2d.fillOval(0, 0, width, height);
        
        // Draw gamepad icon
        g2d.setColor(new Color(240, 240, 255));
        
        // Controller body
        RoundRectangle2D.Double body = new RoundRectangle2D.Double(15, 25, 50, 30, 15, 15);
        g2d.fill(body);
        
        // Left grip
        Ellipse2D.Double leftGrip = new Ellipse2D.Double(5, 20, 20, 40);
        g2d.fill(leftGrip);
        
        // Right grip
        Ellipse2D.Double rightGrip = new Ellipse2D.Double(55, 20, 20, 40);
        g2d.fill(rightGrip);
        
        // Buttons
        g2d.setColor(StyleUtils.ACCENT_BLUE);
        g2d.fillOval(55, 25, 10, 10);
        g2d.fillOval(60, 35, 10, 10);
        
        // Directional pad
        g2d.fillRect(20, 35, 15, 5);
        g2d.fillRect(25, 30, 5, 15);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private Image createMemoryGameImage() {
        // Create a simple graphical representation of a memory game
        int width = 250;
        int height = 180;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2d.setColor(StyleUtils.BACKGROUND_DARK);
        g2d.fillRect(0, 0, width, height);
        
        // Draw memory cards in a grid
        int cardSize = 45;
        int margin = 10;
        int startX = (width - (4 * cardSize + 3 * margin)) / 2;
        int startY = (height - (3 * cardSize + 2 * margin)) / 2;
        
        // Define card colors
        Color[] cardColors = {
            StyleUtils.ACCENT_GREEN,
            StyleUtils.ACCENT_YELLOW,
            StyleUtils.ACCENT_RED,
            StyleUtils.ACCENT_BLUE,
            new Color(198, 120, 221), // Purple
            new Color(86, 182, 194)   // Cyan
        };
        
        // Draw the cards
        int colorIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 4; col++) {
                int x = startX + col * (cardSize + margin);
                int y = startY + row * (cardSize + margin);
                
                if (row == 1 && col == 1) {
                    // Flipped card - show color
                    g2d.setColor(cardColors[colorIndex % cardColors.length]);
                    g2d.fillRoundRect(x, y, cardSize, cardSize, 10, 10);
                    colorIndex++;
                } else if (row == 1 && col == 2) {
                    // Flipped card - match
                    g2d.setColor(cardColors[colorIndex % cardColors.length]);
                    g2d.fillRoundRect(x, y, cardSize, cardSize, 10, 10);
                } else {
                    // Face down card
                    g2d.setColor(new Color(60, 64, 72));
                    g2d.fillRoundRect(x, y, cardSize, cardSize, 10, 10);
                    
                    // Question mark
                    g2d.setColor(new Color(100, 104, 112));
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 20));
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = "?";
                    int textX = x + (cardSize - fm.stringWidth(text)) / 2;
                    int textY = y + (cardSize - fm.getHeight()) / 2 + fm.getAscent();
                    g2d.drawString(text, textX, textY);
                }
                
                // Card border
                g2d.setColor(StyleUtils.BACKGROUND_DARK);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(x, y, cardSize, cardSize, 10, 10);
                
                colorIndex++;
            }
        }
        
        g2d.dispose();
        return image;
    }
    
    private Image createSnakeGameImage() {
        // Create a simple graphical representation of a snake game
        int width = 250;
        int height = 180;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background (game board)
        g2d.setColor(StyleUtils.BACKGROUND_DARK);
        g2d.fillRect(0, 0, width, height);
        
        // Draw grid lines
        g2d.setColor(new Color(50, 54, 62));
        int cellSize = 18;
        
        // Vertical lines
        for (int x = 0; x <= width; x += cellSize) {
            g2d.drawLine(x, 0, x, height);
        }
        
        // Horizontal lines
        for (int y = 0; y <= height; y += cellSize) {
            g2d.drawLine(0, y, width, y);
        }
        
        // Draw snake (from center left, moving right)
        g2d.setColor(StyleUtils.ACCENT_GREEN);
        
        // Snake body segments
        int[] snakeX = {3, 4, 5, 6, 7, 7};
        int[] snakeY = {5, 5, 5, 5, 5, 6};
        
        for (int i = 0; i < snakeX.length; i++) {
            int x = snakeX[i] * cellSize;
            int y = snakeY[i] * cellSize;
            
            if (i == 0) {
                // Head
                g2d.setColor(new Color(122, 165, 91)); // Darker green
            } else {
                // Body
                g2d.setColor(StyleUtils.ACCENT_GREEN);
            }
            
            g2d.fillRoundRect(x, y, cellSize, cellSize, 6, 6);
        }
        
        // Draw food
        g2d.setColor(StyleUtils.ACCENT_RED);
        int foodX = 9 * cellSize;
        int foodY = 5 * cellSize;
        g2d.fillOval(foodX + 2, foodY + 2, cellSize - 4, cellSize - 4);
        
        g2d.dispose();
        return image;
    }
    
    private void launchGame(MiniGame game) {
        setVisible(false);
        game.setMainMenu(this);
        game.start();
    }
    
    public void updatePointsDisplay() {
        User user = UserManager.getInstance().getCurrentUser();
        pointsLabel.setText("Points: " + user.getTotalPoints());
    }
}