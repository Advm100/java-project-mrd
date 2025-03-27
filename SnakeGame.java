import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.Timer;

/**
 * Improved Snake Game with consistent styling
 */
public class SnakeGame extends JFrame implements MiniGame {
    private static final int GAME_POINTS = 50;
    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 25;
    private static final int GAME_SPEED = 150; // milliseconds
    
    private MainMenu mainMenu;
    private GamePanel gamePanel;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private Timer timer;
    
    private ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private int direction = KeyEvent.VK_RIGHT; // Initial direction
    private int score = 0;
    private int level = 1;
    private boolean gameOver = false;
    private boolean gamePaused = false;
    private Random random = new Random();
    
    public SnakeGame() {
        StyleUtils.applyGameStyle(this, "Snake Game - Medium");
        
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
        
        // Set up keyboard controls
        setupKeyboardControls();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                endGame();
            }
        });
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 10));
        headerPanel.setOpaque(false);
        
        // Game title
        JLabel titleLabel = StyleUtils.createTitleLabel("SNAKE GAME");
        JLabel subtitleLabel = StyleUtils.createSubtitleLabel("Control the snake and eat to grow");
        
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
        
        // Score counter
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Level
        levelLabel = new JLabel("Level: 1");
        levelLabel.setForeground(StyleUtils.ACCENT_YELLOW);
        levelLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        statsPanel.add(scoreLabel);
        statsPanel.add(levelLabel);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(statsPanel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private JPanel createGameAreaPanel() {
        JPanel gameAreaPanel = new JPanel(new BorderLayout());
        gameAreaPanel.setOpaque(false);
        
        // Game board panel with dark background
        JPanel gameBoardPanel = StyleUtils.createRoundPanel(new Color(30, 34, 42, 220), 20);
        gameBoardPanel.setLayout(new BorderLayout());
        gameBoardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Custom game panel for drawing the snake game
        gamePanel = new GamePanel();
        
        gameBoardPanel.add(gamePanel, BorderLayout.CENTER);
        gameAreaPanel.add(gameBoardPanel, BorderLayout.CENTER);
        
        return gameAreaPanel;
    }
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setOpaque(false);
        
        // Controls info
        JPanel controlsInfoPanel = StyleUtils.createRoundPanel(new Color(50, 54, 62, 150), 15);
        controlsInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // Arrow keys
        JLabel arrowsLabel = new JLabel("Arrows: Move");
        arrowsLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        arrowsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Space
        JLabel spaceLabel = new JLabel("Space: Pause/Resume");
        spaceLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        spaceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        controlsInfoPanel.add(arrowsLabel);
        controlsInfoPanel.add(spaceLabel);
        
        // Button panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setOpaque(false);
        
        JButton restartButton = StyleUtils.createStyledButton("RESTART", StyleUtils.ACCENT_YELLOW);
        restartButton.addActionListener(e -> restartGame());
        
        JButton quitButton = StyleUtils.createStyledButton("QUIT GAME", StyleUtils.ACCENT_RED);
        quitButton.addActionListener(e -> endGame());
        
        buttonsPanel.add(restartButton);
        buttonsPanel.add(quitButton);
        
        controlPanel.add(controlsInfoPanel, BorderLayout.WEST);
        controlPanel.add(buttonsPanel, BorderLayout.EAST);
        
        return controlPanel;
    }
    
    private void setupKeyboardControls() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        if (direction != KeyEvent.VK_DOWN && !gamePaused)
                            direction = KeyEvent.VK_UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != KeyEvent.VK_UP && !gamePaused)
                            direction = KeyEvent.VK_DOWN;
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != KeyEvent.VK_RIGHT && !gamePaused)
                            direction = KeyEvent.VK_LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != KeyEvent.VK_LEFT && !gamePaused)
                            direction = KeyEvent.VK_RIGHT;
                        break;
                    case KeyEvent.VK_SPACE:
                        togglePause();
                        break;
                }
            }
            return false;
        });
    }
    
    private void togglePause() {
        gamePaused = !gamePaused;
        gamePanel.repaint(); // Ensure pause indicator is shown/hidden
    }
    
    private void initializeGame() {
        // Initialize snake (3 segments at the start)
        snake.clear();
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));
        
        direction = KeyEvent.VK_RIGHT;
        score = 0;
        level = 1;
        gameOver = false;
        gamePaused = false;
        
        // Update labels
        scoreLabel.setText("Score: 0");
        levelLabel.setText("Level: 1");
        
        // Place initial food
        placeFood();
        
        // Start game timer
        startGameTimer();
    }
    
    private void restartGame() {
        // Stop existing timer
        if (timer != null) {
            timer.cancel();
        }
        
        // Reset and restart
        initializeGame();
    }
    
    private void startGameTimer() {
        if (timer != null) {
            timer.cancel();
        }
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!gameOver && !gamePaused) {
                    moveSnake();
                    checkCollision();
                    gamePanel.repaint();
                }
            }
        }, GAME_SPEED, Math.max(50, GAME_SPEED - (level * 10))); // Speed increases with level
    }
    
    private void moveSnake() {
        // Create new head based on current direction
        Point head = new Point(snake.get(0));
        
        switch (direction) {
            case KeyEvent.VK_UP:
                head.y--;
                break;
            case KeyEvent.VK_DOWN:
                head.y++;
                break;
            case KeyEvent.VK_LEFT:
                head.x--;
                break;
            case KeyEvent.VK_RIGHT:
                head.x++;
                break;
        }
        
        // Add new head to snake
        snake.add(0, head);
        
        // Check if food is eaten
        if (head.equals(food)) {
            score += 10 * level;
            scoreLabel.setText("Score: " + score);
            
            // Increase level every 5 food items
            if (score > 0 && score % (50 * level) == 0) {
                level++;
                levelLabel.setText("Level: " + level);
                startGameTimer(); // Restart timer to apply new speed
            }
            
            placeFood();
        } else {
            // Remove tail if no food is eaten
            snake.remove(snake.size() - 1);
        }
    }
    
    private void checkCollision() {
        Point head = snake.get(0);
        
        // Check wall collision
        if (head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE) {
            gameOver();
            return;
        }
        
        // Check self collision (start from index 1 to avoid checking head against itself)
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver();
                return;
            }
        }
    }
    
    private void placeFood() {
        // Place food at random location (not on snake)
        boolean validLocation;
        do {
            food = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
            validLocation = true;
            
            // Check if food is not on snake
            for (Point p : snake) {
                if (p.equals(food)) {
                    validLocation = false;
                    break;
                }
            }
        } while (!validLocation);
    }
    
    private void gameOver() {
        gameOver = true;
        
        // Stop timer
        if (timer != null) {
            timer.cancel();
        }
        
        // Update game score in user profile
        UserManager.getInstance().updateGameScore("Snake Game", score);
        
        // Ensure game panel is updated to show game over
        gamePanel.repaint();
        
        // Add short delay before showing dialog
        Timer delayTimer = new Timer();
        delayTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> showGameOverDialog());
            }
        }, 1000);
    }
    
    private void showGameOverDialog() {
        int earnedPoints = score * GAME_POINTS / 100;
        
        // Create a stylish game over dialog
        JDialog gameOverDialog = new JDialog(this, "Game Over", true);
        gameOverDialog.setSize(400, 300);
        gameOverDialog.setLocationRelativeTo(this);
        gameOverDialog.setResizable(false);
        
        JPanel gameOverPanel = StyleUtils.createGradientPanel();
        gameOverPanel.setLayout(new BorderLayout(0, 15));
        gameOverPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Game over message
        JLabel gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gameOverLabel.setForeground(StyleUtils.ACCENT_RED);
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Stats panel
        JPanel statsPanel = StyleUtils.createRoundPanel(new Color(50, 54, 62), 15);
        statsPanel.setLayout(new GridLayout(3, 1, 0, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel scoreStatLabel = new JLabel("Score: " + score);
        scoreStatLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        scoreStatLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JLabel levelStatLabel = new JLabel("Level: " + level);
        levelStatLabel.setForeground(StyleUtils.ACCENT_YELLOW);
        levelStatLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JLabel pointsStatLabel = new JLabel("Points earned: " + earnedPoints);
        pointsStatLabel.setForeground(StyleUtils.ACCENT_BLUE);
        pointsStatLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pointsStatLabel.setIcon(StyleUtils.createCoinIcon());
        
        statsPanel.add(scoreStatLabel);
        statsPanel.add(levelStatLabel);
        statsPanel.add(pointsStatLabel);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setOpaque(false);
        
        JButton playAgainButton = StyleUtils.createStyledButton("PLAY AGAIN", StyleUtils.ACCENT_GREEN);
        playAgainButton.addActionListener(e -> {
            gameOverDialog.dispose();
            MiniGamesApp.addPoints(earnedPoints);
            restartGame();
        });
        
        JButton menuButton = StyleUtils.createStyledButton("MAIN MENU", StyleUtils.ACCENT_BLUE);
        menuButton.addActionListener(e -> {
            gameOverDialog.dispose();
            MiniGamesApp.addPoints(earnedPoints);
            endGame();
        });
        
        buttonsPanel.add(playAgainButton);
        buttonsPanel.add(menuButton);
        
        // Add components to dialog panel
        gameOverPanel.add(gameOverLabel, BorderLayout.NORTH);
        gameOverPanel.add(statsPanel, BorderLayout.CENTER);
        gameOverPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        gameOverDialog.setContentPane(gameOverPanel);
        gameOverDialog.setVisible(true);
    }
    
    private void endGame() {
        if (timer != null) {
            timer.cancel();
        }
        dispose();
        mainMenu.updatePointsDisplay();
        mainMenu.setVisible(true);
    }
    
    // Custom panel for drawing the snake game with improved graphics
    private class GamePanel extends JPanel {
        public GamePanel() {
            setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE));
            setBackground(new Color(30, 34, 42));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw grid background (subtle)
            g2d.setColor(new Color(40, 44, 52));
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if ((i + j) % 2 == 0) {
                        g2d.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
            
            // Draw snake with improved styling
            for (int i = 0; i < snake.size(); i++) {
                Point p = snake.get(i);
                int x = p.x * CELL_SIZE;
                int y = p.y * CELL_SIZE;
                int margin = 1; // Small gap between segments
                
                if (i == 0) {
                    // Head - different color and eyes
                    g2d.setColor(new Color(122, 165, 91)); // Darker green
                    g2d.fillRoundRect(x + margin, y + margin, CELL_SIZE - 2*margin, CELL_SIZE - 2*margin, 8, 8);
                    
                    // Eyes
                    g2d.setColor(Color.WHITE);
                    int eyeSize = 5;
                    
                    // Position eyes based on direction
                    switch (direction) {
                        case KeyEvent.VK_RIGHT:
                            g2d.fillOval(x + CELL_SIZE - 10, y + 7, eyeSize, eyeSize);
                            g2d.fillOval(x + CELL_SIZE - 10, y + CELL_SIZE - 12, eyeSize, eyeSize);
                            break;
                        case KeyEvent.VK_LEFT:
                            g2d.fillOval(x + 5, y + 7, eyeSize, eyeSize);
                            g2d.fillOval(x + 5, y + CELL_SIZE - 12, eyeSize, eyeSize);
                            break;
                        case KeyEvent.VK_UP:
                            g2d.fillOval(x + 7, y + 5, eyeSize, eyeSize);
                            g2d.fillOval(x + CELL_SIZE - 12, y + 5, eyeSize, eyeSize);
                            break;
                        case KeyEvent.VK_DOWN:
                            g2d.fillOval(x + 7, y + CELL_SIZE - 10, eyeSize, eyeSize);
                            g2d.fillOval(x + CELL_SIZE - 12, y + CELL_SIZE - 10, eyeSize, eyeSize);
                            break;
                    }
                } else {
                    // Body with gradient based on position (further from head = slightly darker)
                    float intensity = 1.0f - (float)i / snake.size() * 0.3f;
                    g2d.setColor(new Color(
                        Math.round(StyleUtils.ACCENT_GREEN.getRed() * intensity) / 255f,
                        Math.round(StyleUtils.ACCENT_GREEN.getGreen() * intensity) / 255f,
                        Math.round(StyleUtils.ACCENT_GREEN.getBlue() * intensity) / 255f
                    ));
                    g2d.fillRoundRect(x + margin, y + margin, CELL_SIZE - 2*margin, CELL_SIZE - 2*margin, 8, 8);
                }
            }
            
            // Draw food with glow effect
            if (food != null) {
                int x = food.x * CELL_SIZE;
                int y = food.y * CELL_SIZE;
                
                // Glow
                g2d.setColor(new Color(StyleUtils.ACCENT_RED.getRed(), 
                                     StyleUtils.ACCENT_RED.getGreen(), 
                                     StyleUtils.ACCENT_RED.getBlue(), 100));
                g2d.fillOval(x - 2, y - 2, CELL_SIZE + 4, CELL_SIZE + 4);
                
                // Food
                g2d.setColor(StyleUtils.ACCENT_RED);
                g2d.fillOval(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
                
                // Highlight
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fillOval(x + 5, y + 5, CELL_SIZE/3, CELL_SIZE/3);
            }
            
            // Draw game status overlays
            if (gameOver) {
                drawOverlay(g2d, "GAME OVER", StyleUtils.ACCENT_RED);
            } else if (gamePaused) {
                drawOverlay(g2d, "PAUSED", StyleUtils.ACCENT_BLUE);
            }
        }
        
        private void drawOverlay(Graphics2D g2d, String text, Color color) {
            // Semi-transparent background
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            
            // Text
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int x = (getWidth() - textWidth) / 2;
            int y = getHeight() / 2 + fm.getAscent()/2;
            
            // Text shadow
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.drawString(text, x + 2, y + 2);
            
            // Text
            g2d.setColor(color);
            g2d.drawString(text, x, y);
        }
    }
    
    // Implementation of MiniGame interface
    public void start() {
        initializeGame();
        setVisible(true);
    }
    
    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }
}