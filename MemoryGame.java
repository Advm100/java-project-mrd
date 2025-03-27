import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Timer;

/**
 * Improved Memory Game with consistent styling
 */
public class MemoryGame extends JFrame implements MiniGame {
    private static final int GAME_POINTS = 25;
    private static final int GRID_SIZE = 4; // 4x4 grid
    private MainMenu mainMenu;
    private JPanel cardsPanel;
    private JLabel attemptsLabel;
    private JLabel timerLabel;
    private JButton[][] cards = new JButton[GRID_SIZE][GRID_SIZE];
    private int[][] cardValues = new int[GRID_SIZE][GRID_SIZE];
    private boolean[][] revealed = new boolean[GRID_SIZE][GRID_SIZE];
    private int attempts = 0;
    private int pairsFound = 0;
    private int firstRow = -1, firstCol = -1;
    private boolean canClick = true;
    private Timer gameTimer;
    private int secondsElapsed = 0;
    
    public MemoryGame() {
        StyleUtils.applyGameStyle(this, "Memory Game - Easy");
        
        // Main panel with gradient background
        JPanel mainPanel = StyleUtils.createGradientPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Game header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Game panel (center)
        JPanel gameAreaPanel = createGameAreaPanel();
        
        // Control panel (bottom)
        JPanel controlPanel = createControlPanel();
        
        // Assemble the UI
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(gameAreaPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
        headerPanel.setOpaque(false);
        
        // Game title
        JLabel titleLabel = StyleUtils.createTitleLabel("MEMORY GAME");
        JLabel subtitleLabel = StyleUtils.createSubtitleLabel("Match pairs of cards to win");
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        // Game stats panel
        JPanel statsPanel = StyleUtils.createRoundPanel(new Color(50, 54, 62, 200), 15);
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        
        // Attempts counter
        attemptsLabel = new JLabel("Attempts: 0");
        attemptsLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        attemptsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Timer
        timerLabel = new JLabel("Time: 0s");
        timerLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        statsPanel.add(attemptsLabel);
        statsPanel.add(timerLabel);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(statsPanel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private JPanel createGameAreaPanel() {
        JPanel gameAreaPanel = new JPanel(new BorderLayout());
        gameAreaPanel.setOpaque(false);
        
        // Card panel with custom background
        JPanel cardContainerPanel = StyleUtils.createRoundPanel(new Color(40, 44, 52, 200), 20);
        cardContainerPanel.setLayout(new BorderLayout());
        cardContainerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Cards grid
        cardsPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 10, 10));
        cardsPanel.setOpaque(false);
        
        // Create styled card buttons
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cards[i][j] = createStyledCard(i, j);
                cardsPanel.add(cards[i][j]);
            }
        }
        
        cardContainerPanel.add(cardsPanel, BorderLayout.CENTER);
        gameAreaPanel.add(cardContainerPanel, BorderLayout.CENTER);
        
        return gameAreaPanel;
    }
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setOpaque(false);
        
        // Game info/instructions
        JLabel infoLabel = new JLabel("Click on cards to flip them and find matching pairs");
        infoLabel.setForeground(StyleUtils.TEXT_DIM);
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        
        // Button panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setOpaque(false);
        
        JButton restartButton = StyleUtils.createStyledButton("RESTART", StyleUtils.ACCENT_YELLOW);
        restartButton.addActionListener(e -> restartGame());
        
        JButton quitButton = StyleUtils.createStyledButton("QUIT GAME", StyleUtils.ACCENT_RED);
        quitButton.addActionListener(e -> endGame());
        
        buttonsPanel.add(restartButton);
        buttonsPanel.add(quitButton);
        
        controlPanel.add(infoLabel, BorderLayout.WEST);
        controlPanel.add(buttonsPanel, BorderLayout.EAST);
        
        return controlPanel;
    }
    
    private JButton createStyledCard(int row, int col) {
        JButton card = new JButton("?") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                if (revealed[row][col]) {
                    // Card is flipped - show color based on value
                    int value = cardValues[row][col];
                    // Generate color based on card value
                    Color cardColor = getCardColor(value);
                    g2d.setColor(cardColor);
                } else {
                    // Face down card - dark background
                    g2d.setColor(new Color(60, 64, 72));
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Border
                g2d.setColor(new Color(30, 34, 42));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                
                // Card text/symbol
                if (revealed[row][col]) {
                    // Show card value
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
                    String text = String.valueOf(cardValues[row][col]);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (getWidth() - fm.stringWidth(text)) / 2;
                    int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2d.drawString(text, textX, textY);
                } else {
                    // Show question mark
                    g2d.setColor(new Color(120, 124, 132));
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 28));
                    String text = "?";
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (getWidth() - fm.stringWidth(text)) / 2;
                    int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2d.drawString(text, textX, textY);
                }
                
                g2d.dispose();
            }
        };
        
        card.setPreferredSize(new Dimension(80, 80));
        card.setBorderPainted(false);
        card.setFocusPainted(false);
        card.setContentAreaFilled(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        card.addActionListener(e -> {
            if (canClick && !revealed[row][col]) {
                flipCard(row, col);
            }
        });
        
        return card;
    }
    
    private Color getCardColor(int value) {
        // Generate different colors based on card value
        Color[] cardColors = {
            StyleUtils.ACCENT_BLUE,
            StyleUtils.ACCENT_GREEN,
            StyleUtils.ACCENT_YELLOW,
            StyleUtils.ACCENT_RED,
            new Color(198, 120, 221), // Purple
            new Color(86, 182, 194),  // Cyan
            new Color(236, 154, 83),  // Orange
            new Color(82, 203, 142)   // Teal
        };
        
        return cardColors[(value - 1) % cardColors.length];
    }
    
    private void flipCard(int row, int col) {
        revealed[row][col] = true;
        cards[row][col].repaint();
        
        if (firstRow == -1) {
            // This is the first card flipped
            firstRow = row;
            firstCol = col;
        } else {
            // This is the second card
            attempts++;
            attemptsLabel.setText("Attempts: " + attempts);
            
            if (cardValues[firstRow][firstCol] == cardValues[row][col]) {
                // Match found
                firstRow = -1;
                firstCol = -1;
                pairsFound++;
                
                if (pairsFound == (GRID_SIZE * GRID_SIZE) / 2) {
                    gameWon();
                }
            } else {
                // No match
                canClick = false;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SwingUtilities.invokeLater(() -> {
                            revealed[firstRow][firstCol] = false;
                            revealed[row][col] = false;
                            cards[firstRow][firstCol].repaint();
                            cards[row][col].repaint();
                            firstRow = -1;
                            firstCol = -1;
                            canClick = true;
                        });
                    }
                }, 1000);
            }
        }
    }
    
    private void initializeCards() {
        // Reset game state
        pairsFound = 0;
        attempts = 0;
        firstRow = -1;
        firstCol = -1;
        canClick = true;
        secondsElapsed = 0;
        
        // Create pairs of values (1 to GRID_SIZE*GRID_SIZE/2)
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < (GRID_SIZE * GRID_SIZE) / 2; i++) {
            values.add(i + 1);
            values.add(i + 1);
        }
        
        // Shuffle the values
        Collections.shuffle(values);
        
        // Assign values to the grid
        int index = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cardValues[i][j] = values.get(index++);
                revealed[i][j] = false;
                cards[i][j].repaint();
            }
        }
        
        // Update labels
        attemptsLabel.setText("Attempts: 0");
        timerLabel.setText("Time: 0s");
        
        // Start timer
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsElapsed++;
                SwingUtilities.invokeLater(() -> {
                    timerLabel.setText("Time: " + secondsElapsed + "s");
                });
            }
        }, 1000, 1000);
    }
    
    private void restartGame() {
        // Stop any existing timer
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        
        // Reset and restart the game
        initializeCards();
    }
    
    private void gameWon() {
        // Stop timer
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        
        // Calculate score based on attempts and time
        int baseScore = 1000;
        int timeDeduction = secondsElapsed / 2;
        int attemptDeduction = attempts * 5;
        int score = Math.max(100, baseScore - timeDeduction - attemptDeduction);
        
        int earnedPoints = score * GAME_POINTS / 100;
        
        // Update game score in user profile
        UserManager.getInstance().updateGameScore("Memory Game", score);
        
        // Create a stylish win dialog
        JDialog winDialog = new JDialog(this, "Game Won!", true);
        winDialog.setSize(400, 300);
        winDialog.setLocationRelativeTo(this);
        winDialog.setResizable(false);
        
        JPanel winPanel = StyleUtils.createGradientPanel();
        winPanel.setLayout(new BorderLayout(0, 15));
        winPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Congrats message
        JLabel congratsLabel = new JLabel("Congratulations!");
        congratsLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        congratsLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        congratsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Stats panel
        JPanel statsPanel = StyleUtils.createRoundPanel(new Color(50, 54, 62), 15);
        statsPanel.setLayout(new GridLayout(4, 1, 0, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel attemptsStatLabel = new JLabel("Attempts: " + attempts);
        attemptsStatLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        attemptsStatLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JLabel timeStatLabel = new JLabel("Time: " + secondsElapsed + " seconds");
        timeStatLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        timeStatLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JLabel scoreStatLabel = new JLabel("Score: " + score);
        scoreStatLabel.setForeground(StyleUtils.ACCENT_GREEN);
        scoreStatLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel pointsStatLabel = new JLabel("Points earned: " + earnedPoints);
        pointsStatLabel.setForeground(StyleUtils.ACCENT_BLUE);
        pointsStatLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pointsStatLabel.setIcon(StyleUtils.createCoinIcon());
        
        statsPanel.add(attemptsStatLabel);
        statsPanel.add(timeStatLabel);
        statsPanel.add(scoreStatLabel);
        statsPanel.add(pointsStatLabel);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setOpaque(false);
        
        JButton playAgainButton = StyleUtils.createStyledButton("PLAY AGAIN", StyleUtils.ACCENT_GREEN);
        playAgainButton.addActionListener(e -> {
            winDialog.dispose();
            restartGame();
        });
        
        JButton menuButton = StyleUtils.createStyledButton("MAIN MENU", StyleUtils.ACCENT_BLUE);
        menuButton.addActionListener(e -> {
            winDialog.dispose();
            MiniGamesApp.addPoints(earnedPoints);
            endGame();
        });
        
        buttonsPanel.add(playAgainButton);
        buttonsPanel.add(menuButton);
        
        // Add components to win panel
        winPanel.add(congratsLabel, BorderLayout.NORTH);
        winPanel.add(statsPanel, BorderLayout.CENTER);
        winPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        winDialog.setContentPane(winPanel);
        winDialog.setVisible(true);
    }
    
    private void endGame() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        dispose();
        mainMenu.updatePointsDisplay();
        mainMenu.setVisible(true);
    }
    
    // Implementation of MiniGame interface
    public void start() {
        initializeCards();
        setVisible(true);
    }
    
    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
}