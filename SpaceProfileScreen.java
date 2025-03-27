import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Enhanced space-themed profile screen with improved UI elements
 */
public class SpaceProfileScreen extends JFrame {
    private MainMenu mainMenu;
    private User user;
    
    public SpaceProfileScreen(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        this.user = UserManager.getInstance().getCurrentUser();
        
        StyleUtils.applyGameStyle(this, "Space Profile - " + user.getUsername());
        
        // Main panel with space-themed background
        JPanel mainPanel = createSpaceBackgroundPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // Add header
        JPanel headerPanel = createHeaderPanel();
        
        // Add profile content
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);
        
        // Left panel for user info and spaceship
        JPanel leftPanel = createUserInfoPanel();
        
        // Right panel for achievements
        JPanel rightPanel = createAchievementsPanel();
        
        // Footer with return button
        JPanel footerPanel = createFooterPanel();
        
        // Assemble all panels
        contentPanel.add(leftPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createSpaceBackgroundPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create deep space background with gradient
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(10, 15, 30), 
                    getWidth(), getHeight(), new Color(30, 40, 70)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw stars
                drawStars(g2d, getWidth(), getHeight());
                
                g2d.dispose();
            }
            
            private void drawStars(Graphics2D g2d, int width, int height) {
                // Draw different sizes of stars
                for (int i = 0; i < 200; i++) {
                    int x = (int) (Math.random() * width);
                    int y = (int) (Math.random() * height);
                    
                    // Vary star appearance
                    if (Math.random() > 0.9) {
                        // Brighter, larger star
                        int size = (int) (Math.random() * 3) + 2;
                        g2d.setColor(new Color(255, 255, 240));
                        g2d.fillOval(x, y, size, size);
                    } else {
                        // Regular star
                        int size = (int) (Math.random() * 2) + 1;
                        int brightness = 150 + (int) (Math.random() * 105);
                        g2d.setColor(new Color(brightness, brightness, brightness));
                        g2d.fillOval(x, y, size, size);
                    }
                }
            }
        };
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 20));
        headerPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel("SPACE PROFILE");
        titleLabel.setForeground(new Color(200, 220, 255));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // User rank
        SpaceProfile.SpaceRank rank = user.getSpaceProfile().getCurrentRank();
        JLabel rankLabel = new JLabel(rank.getTitle());
        rankLabel.setForeground(getRankColor(rank));
        rankLabel.setFont(new Font("Arial", Font.BOLD, 20));
        rankLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(rankLabel, BorderLayout.SOUTH);
        
        // Rank progress panel with enhanced styling
        JPanel progressPanel = createEnhancedProgressPanel(rank);
        
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        headerPanel.add(progressPanel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private JPanel createEnhancedProgressPanel(SpaceProfile.SpaceRank rank) {
        // Create a custom progress panel for rank advancement
        JPanel progressPanel = StyleUtils.createRoundPanel(new Color(20, 30, 50, 180), 15);
        progressPanel.setLayout(new BorderLayout(10, 15));
        progressPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        SpaceProfile.SpaceRank nextRank = getNextRank(rank);
        
        if (nextRank != null) {
            // Top section with rank info
            JPanel infoPanel = new JPanel(new BorderLayout(15, 0));
            infoPanel.setOpaque(false);
            
            // Current rank
            JPanel currentRankPanel = new JPanel();
            currentRankPanel.setLayout(new BoxLayout(currentRankPanel, BoxLayout.Y_AXIS));
            currentRankPanel.setOpaque(false);
            
            JLabel currentRankTitle = new JLabel("Current");
            currentRankTitle.setForeground(new Color(150, 170, 190));
            currentRankTitle.setFont(new Font("Arial", Font.PLAIN, 12));
            currentRankTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel currentRankLabel = new JLabel(rank.getTitle());
            currentRankLabel.setForeground(getRankColor(rank));
            currentRankLabel.setFont(new Font("Arial", Font.BOLD, 14));
            currentRankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            currentRankPanel.add(currentRankTitle);
            currentRankPanel.add(Box.createVerticalStrut(5));
            currentRankPanel.add(currentRankLabel);
            
            // Next rank
            JPanel nextRankPanel = new JPanel();
            nextRankPanel.setLayout(new BoxLayout(nextRankPanel, BoxLayout.Y_AXIS));
            nextRankPanel.setOpaque(false);
            
            JLabel nextRankTitle = new JLabel("Next Rank");
            nextRankTitle.setForeground(new Color(150, 170, 190));
            nextRankTitle.setFont(new Font("Arial", Font.PLAIN, 12));
            nextRankTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel nextRankLabel = new JLabel(nextRank.getTitle());
            nextRankLabel.setForeground(getRankColor(nextRank));
            nextRankLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nextRankLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            nextRankPanel.add(nextRankTitle);
            nextRankPanel.add(Box.createVerticalStrut(5));
            nextRankPanel.add(nextRankLabel);
            
            infoPanel.add(currentRankPanel, BorderLayout.WEST);
            infoPanel.add(nextRankPanel, BorderLayout.EAST);
            
            // Progress bar section
            JPanel barPanel = new JPanel(new BorderLayout(0, 5));
            barPanel.setOpaque(false);
            
            // Calculate progress percentage
            int currentPoints = user.getTotalPoints();
            int currentRankPoints = rank.getPointsRequired();
            int nextRankPoints = nextRank.getPointsRequired();
            int pointsNeeded = nextRankPoints - currentRankPoints;
            int pointsProgress = currentPoints - currentRankPoints;
            int progressPercentage = Math.min(100, (pointsProgress * 100) / Math.max(1, pointsNeeded));
            
            // Create custom progress bar
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue(progressPercentage);
            progressBar.setStringPainted(true);
            progressBar.setString(currentPoints + " / " + nextRankPoints + " points");
            progressBar.setForeground(getRankColor(rank));
            progressBar.setBackground(new Color(30, 40, 60));
            progressBar.setPreferredSize(new Dimension(0, 25));
            
            // Points status
            JPanel pointsStatusPanel = new JPanel(new BorderLayout());
            pointsStatusPanel.setOpaque(false);
            
            JLabel pointsLabel = new JLabel(currentPoints + " / " + nextRankPoints + " points");
            pointsLabel.setForeground(new Color(180, 200, 220));
            pointsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            JLabel neededLabel = new JLabel((nextRankPoints - currentPoints) + " points needed");
            neededLabel.setForeground(new Color(150, 170, 190));
            neededLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            neededLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            
            pointsStatusPanel.add(pointsLabel, BorderLayout.WEST);
            pointsStatusPanel.add(neededLabel, BorderLayout.EAST);
            
            barPanel.add(progressBar, BorderLayout.CENTER);
            barPanel.add(pointsStatusPanel, BorderLayout.SOUTH);
            
            // Assemble everything
            progressPanel.add(infoPanel, BorderLayout.NORTH);
            progressPanel.add(barPanel, BorderLayout.CENTER);
        } else {
            // Max rank reached
            JLabel maxRankLabel = new JLabel("MAXIMUM RANK ACHIEVED!");
            maxRankLabel.setForeground(new Color(255, 215, 0)); // Gold
            maxRankLabel.setFont(new Font("Arial", Font.BOLD, 18));
            maxRankLabel.setHorizontalAlignment(SwingConstants.CENTER);
            maxRankLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            
            progressPanel.add(maxRankLabel, BorderLayout.CENTER);
        }
        
        return progressPanel;
    }
    
    private JPanel createUserInfoPanel() {
        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);
        userInfoPanel.setPreferredSize(new Dimension(300, 400));
        
        // User avatar/spaceship
        JLabel avatarLabel = new JLabel(createSpaceshipImage());
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Create panel for spaceship info with enhanced visuals
        JPanel shipInfoPanel = StyleUtils.createRoundPanel(new Color(20, 30, 50, 200), 15);
        shipInfoPanel.setLayout(new BoxLayout(shipInfoPanel, BoxLayout.Y_AXIS));
        shipInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        shipInfoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Title for the info panel
        JLabel infoTitleLabel = new JLabel("SPACESHIP DATA");
        infoTitleLabel.setForeground(new Color(100, 150, 255));
        infoTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(60, 80, 120));
        separator.setMaximumSize(new Dimension(250, 2));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Spaceship name
        JLabel shipNameLabel = new JLabel("Spaceship: " + user.getSpaceProfile().getSpaceshipName());
        shipNameLabel.setForeground(new Color(150, 200, 255));
        shipNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        shipNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Home world
        JLabel homeWorldLabel = new JLabel("Home World: " + user.getSpaceProfile().getHomeWorld());
        homeWorldLabel.setForeground(new Color(150, 200, 255));
        homeWorldLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        homeWorldLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Spaceship stats in a nicer grid
        JPanel statsPanel = StyleUtils.createRoundPanel(new Color(40, 50, 70, 150), 10);
        statsPanel.setLayout(new GridLayout(3, 1, 5, 5));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        statsPanel.add(createStatRow("MISSIONS", String.valueOf(user.getSpaceProfile().getMissionCount())));
        statsPanel.add(createStatRow("POINTS", String.valueOf(user.getTotalPoints())));
        statsPanel.add(createStatRow("ACHIEVEMENTS", 
                user.getSpaceProfile().getUnlockedAchievementsCount() + "/" + 
                user.getSpaceProfile().getAchievements().size()));
        
        // Edit spaceship button with better sizing
        JButton editButton = StyleUtils.createStyledButton("CUSTOMIZE SHIP", StyleUtils.ACCENT_BLUE);
        editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editButton.setMaximumSize(new Dimension(200, 35));
        editButton.addActionListener(e -> showCustomizeDialog());
        
        // Add all components
        shipInfoPanel.add(infoTitleLabel);
        shipInfoPanel.add(Box.createVerticalStrut(5));
        shipInfoPanel.add(separator);
        shipInfoPanel.add(Box.createVerticalStrut(10));
        shipInfoPanel.add(shipNameLabel);
        shipInfoPanel.add(Box.createVerticalStrut(5));
        shipInfoPanel.add(homeWorldLabel);
        shipInfoPanel.add(Box.createVerticalStrut(15));
        shipInfoPanel.add(statsPanel);
        shipInfoPanel.add(Box.createVerticalStrut(15));
        shipInfoPanel.add(editButton);
        
        userInfoPanel.add(avatarLabel);
        userInfoPanel.add(Box.createVerticalStrut(15));
        userInfoPanel.add(shipInfoPanel);
        
        return userInfoPanel;
    }
    
    private JPanel createStatRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(label + ":");
        nameLabel.setForeground(new Color(180, 200, 220));
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(new Color(220, 240, 255));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 14));
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(nameLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createAchievementsPanel() {
        JPanel achievementsPanel = new JPanel(new BorderLayout(0, 15));
        achievementsPanel.setOpaque(false);
        
        // Title
        JLabel titleLabel = new JLabel("SPACE ACHIEVEMENTS");
        titleLabel.setForeground(new Color(150, 200, 255));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Scrollable achievements list
        JPanel achievementsListPanel = new JPanel();
        achievementsListPanel.setLayout(new BoxLayout(achievementsListPanel, BoxLayout.Y_AXIS));
        achievementsListPanel.setOpaque(false);
        
        // Get achievements from user's space profile
        List<SpaceProfile.Achievement> achievements = user.getSpaceProfile().getAchievements();
        
        for (SpaceProfile.Achievement achievement : achievements) {
            achievementsListPanel.add(createAchievementPanel(achievement));
            achievementsListPanel.add(Box.createVerticalStrut(10));
        }
        
        // Enhanced scroll pane for achievements
        JScrollPane scrollPane = new JScrollPane(achievementsListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Style the scroll bar
        scrollPane.getVerticalScrollBar().setBackground(new Color(20, 30, 50));
        scrollPane.getVerticalScrollBar().setForeground(new Color(80, 120, 180));
        
        // Add a border to make it look better
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(new Color(40, 60, 100, 100), 1, true)
        ));
        
        achievementsPanel.add(titleLabel, BorderLayout.NORTH);
        achievementsPanel.add(scrollPane, BorderLayout.CENTER);
        
        return achievementsPanel;
    }
    
    private JPanel createAchievementPanel(SpaceProfile.Achievement achievement) {
        JPanel achievementPanel = StyleUtils.createRoundPanel(
                new Color(20, 30, 50, achievement.isUnlocked() ? 200 : 100), 10);
        achievementPanel.setLayout(new BorderLayout(15, 0));
        achievementPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        
        // Achievement icon
        JLabel iconLabel = new JLabel(createAchievementIcon(achievement));
        
        // Achievement info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(achievement.getName());
        nameLabel.setForeground(achievement.isUnlocked() ? 
                new Color(255, 215, 0) : new Color(120, 130, 140));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel descLabel = new JLabel(achievement.getDescription());
        descLabel.setForeground(achievement.isUnlocked() ? 
                new Color(200, 220, 255) : new Color(80, 90, 100));
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(descLabel);
        
        // Status indicator
        JLabel statusLabel = new JLabel(achievement.isUnlocked() ? "UNLOCKED" : "LOCKED");
        statusLabel.setForeground(achievement.isUnlocked() ? 
                new Color(100, 255, 100) : new Color(255, 100, 100));
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        achievementPanel.add(iconLabel, BorderLayout.WEST);
        achievementPanel.add(infoPanel, BorderLayout.CENTER);
        achievementPanel.add(statusLabel, BorderLayout.EAST);
        
        return achievementPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setOpaque(false);
        
        JButton backButton = StyleUtils.createStyledButton("RETURN TO MAIN MENU", StyleUtils.ACCENT_BLUE);
        backButton.addActionListener(e -> {
            dispose();
            mainMenu.setVisible(true);
        });
        
        footerPanel.add(backButton);
        
        return footerPanel;
    }
    
    private ImageIcon createSpaceshipImage() {
        // Create a spaceship image based on user's rank
        int width = 200;
        int height = 170;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw spaceship based on rank
        SpaceProfile.SpaceRank rank = user.getSpaceProfile().getCurrentRank();
        Color mainColor = getRankColor(rank);
        Color accentColor = new Color(
            clamp(mainColor.getRed() + 40, 0, 255),
            clamp(mainColor.getGreen() + 40, 0, 255),
            clamp(mainColor.getBlue() + 40, 0, 255)
        );
        
        // Ship style based on rank
        switch (rank) {
            case CADET:
                drawBasicShip(g2d, width, height, mainColor, accentColor);
                break;
            case PILOT:
                drawFighterShip(g2d, width, height, mainColor, accentColor);
                break;
            case CAPTAIN:
            case COMMANDER:
                drawAdvancedShip(g2d, width, height, mainColor, accentColor);
                break;
            case ADMIRAL:
            case EXPLORER:
            case HERO:
                drawEliteShip(g2d, width, height, mainColor, accentColor);
                break;
        }
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private void drawBasicShip(Graphics2D g2d, int width, int height, Color mainColor, Color accentColor) {
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Ship body
        Polygon body = new Polygon();
        body.addPoint(centerX, centerY - 40);
        body.addPoint(centerX + 40, centerY + 20);
        body.addPoint(centerX, centerY + 10);
        body.addPoint(centerX - 40, centerY + 20);
        g2d.setColor(mainColor);
        g2d.fill(body);
        
        // Ship highlights - Using clamp to ensure valid color values
        g2d.setColor(new Color(
            clamp(mainColor.getRed() + 20, 0, 255),
            clamp(mainColor.getGreen() + 20, 0, 255),
            clamp(mainColor.getBlue() + 20, 0, 255)
        ));
        g2d.fillPolygon(
            new int[] {centerX, centerX + 20, centerX},
            new int[] {centerY - 40, centerY - 15, centerY - 15},
            3
        );
        
        // Ship cockpit
        g2d.setColor(new Color(100, 200, 255, 180));
        g2d.fillOval(centerX - 15, centerY - 20, 30, 30);
        
        // Cockpit highlight
        g2d.setColor(new Color(150, 220, 255, 150));
        g2d.fillOval(centerX - 5, centerY - 15, 10, 10);
        
        // Engines
        g2d.setColor(accentColor);
        g2d.fillRect(centerX - 30, centerY + 20, 20, 10);
        g2d.fillRect(centerX + 10, centerY + 20, 20, 10);
        
        // Engine glow
        g2d.setColor(new Color(255, 150, 50, 150));
        g2d.fillRect(centerX - 30, centerY + 30, 20, 15);
        g2d.fillRect(centerX + 10, centerY + 30, 20, 15);
    }
    
    private void drawFighterShip(Graphics2D g2d, int width, int height, Color mainColor, Color accentColor) {
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Ship body
        g2d.setColor(mainColor);
        Path2D.Double body = new Path2D.Double();
        body.moveTo(centerX, centerY - 50);
        body.lineTo(centerX + 20, centerY - 20);
        body.lineTo(centerX + 40, centerY + 30);
        body.lineTo(centerX, centerY + 10);
        body.lineTo(centerX - 40, centerY + 30);
        body.lineTo(centerX - 20, centerY - 20);
        body.closePath();
        g2d.fill(body);
        
        // Wings
        g2d.setColor(accentColor);
        g2d.fillRect(centerX - 70, centerY, 30, 50);
        g2d.fillRect(centerX + 40, centerY, 30, 50);
        
        // Cockpit
        g2d.setColor(new Color(100, 200, 255, 200));
        g2d.fillOval(centerX - 15, centerY - 30, 30, 40);
        
        // Engine glow
        g2d.setColor(new Color(255, 150, 50, 150));
        g2d.fillRect(centerX - 30, centerY + 30, 20, 20);
        g2d.fillRect(centerX + 10, centerY + 30, 20, 20);
    }
    
    private void drawAdvancedShip(Graphics2D g2d, int width, int height, Color mainColor, Color accentColor) {
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Ship body
        g2d.setColor(mainColor);
        Path2D.Double body = new Path2D.Double();
        body.moveTo(centerX, centerY - 60);
        body.curveTo(centerX + 30, centerY - 40, centerX + 50, centerY, centerX + 30, centerY + 40);
        body.lineTo(centerX - 30, centerY + 40);
        body.curveTo(centerX - 50, centerY, centerX - 30, centerY - 40, centerX, centerY - 60);
        g2d.fill(body);
        
        // Wing details
        g2d.setColor(accentColor);
        g2d.fillRect(centerX - 80, centerY, 50, 30);
        g2d.fillRect(centerX + 30, centerY, 50, 30);
        
        // Cockpit
        g2d.setColor(new Color(100, 200, 255, 220));
        g2d.fillOval(centerX - 20, centerY - 40, 40, 50);
        
        // Engine glow
        g2d.setColor(new Color(255, 150, 50, 150));
        g2d.fillArc(centerX - 30, centerY + 40, 60, 60, 0, 180);
    }
    
    private void drawEliteShip(Graphics2D g2d, int width, int height, Color mainColor, Color accentColor) {
        int centerX = width / 2;
        int centerY = height / 2;
        
        // Ship body - more detailed
        g2d.setColor(mainColor);
        Path2D.Double body = new Path2D.Double();
        body.moveTo(centerX, centerY - 70);
        body.curveTo(centerX + 40, centerY - 50, centerX + 60, centerY - 30, centerX + 40, centerY + 20);
        body.lineTo(centerX + 20, centerY + 40);
        body.lineTo(centerX - 20, centerY + 40);
        body.lineTo(centerX - 40, centerY + 20);
        body.curveTo(centerX - 60, centerY - 30, centerX - 40, centerY - 50, centerX, centerY - 70);
        g2d.fill(body);
        
        // Accent details
        g2d.setColor(accentColor);
        g2d.fillRect(centerX - 90, centerY - 20, 60, 40);
        g2d.fillRect(centerX + 30, centerY - 20, 60, 40);
        
        // Additional details
        g2d.setColor(new Color(200, 220, 255));
        g2d.fillRect(centerX - 50, centerY - 30, 100, 10);
        g2d.fillRect(centerX - 40, centerY + 20, 80, 5);
        
        // Cockpit
        g2d.setColor(new Color(100, 200, 255, 230));
        g2d.fillOval(centerX - 25, centerY - 50, 50, 60);
        
        // Engine glow
        g2d.setColor(new Color(255, 150, 50, 150));
        g2d.fillArc(centerX - 30, centerY + 40, 60, 70, 0, 180);
    }
    
    private ImageIcon createAchievementIcon(SpaceProfile.Achievement achievement) {
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw achievement background
        if (achievement.isUnlocked()) {
            // Gold medal for unlocked achievements
            g2d.setColor(new Color(255, 215, 0));
            g2d.fillOval(0, 0, size, size);
            
            // Highlight
            g2d.setColor(new Color(255, 240, 150));
            g2d.fillOval(size/4, size/4, size/2, size/2);
            
            // Border
            g2d.setColor(new Color(200, 150, 0));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(1, 1, size-2, size-2);
            
            // Achievement specific icon
            drawAchievementSymbol(g2d, achievement, size, true);
        } else {
            // Gray medal for locked achievements
            g2d.setColor(new Color(100, 100, 100));
            g2d.fillOval(0, 0, size, size);
            
            // Locked symbol
            g2d.setColor(new Color(60, 60, 60));
            g2d.fillRect(size/3, size/4, size/3, size/2);
            g2d.fillOval(size/3, size/4, size/3, size/3);
        }
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private void drawAchievementSymbol(Graphics2D g2d, SpaceProfile.Achievement achievement, int size, boolean unlocked) {
        g2d.setColor(unlocked ? new Color(80, 50, 0) : new Color(60, 60, 60));
        
        String name = achievement.getName();
        
        if (name.contains("Contact")) {
            // First Contact - handshake or communication symbol
            g2d.drawLine(size/4, size/2, size*3/4, size/2);
            g2d.drawLine(size/4, size/2, size/3, size*2/3);
            g2d.drawLine(size*3/4, size/2, size*2/3, size*2/3);
        } else if (name.contains("Voyager") || name.contains("Mission")) {
            // Voyager or Mission - spaceship symbol
            int[] xPoints = {size/2, size*3/4, size/2, size/4};
            int[] yPoints = {size/4, size*2/3, size/2, size*2/3};
            g2d.fillPolygon(xPoints, yPoints, 4);
        } else if (name.contains("Explorer") || name.contains("Veteran")) {
            // Explorer or Veteran - star symbol
            drawStar(g2d, size/2, size/2, size/3, 5);
        } else if (name.contains("Memory")) {
            // Memory Master - brain or card symbol
            g2d.drawRoundRect(size/4, size/3, size/2, size/3, 3, 3);
            g2d.drawLine(size/2, size/3, size/2, size*2/3);
        } else if (name.contains("Serpent") || name.contains("Snake")) {
            // Snake achievements - snake symbol
            g2d.drawLine(size/4, size/2, size*3/4, size/2);
            g2d.fillOval(size*2/3, size/3, size/6, size/6);
        } else if (name.contains("Collector") || name.contains("Galactic")) {
            // Collector achievements - coin/treasure symbol
            g2d.drawOval(size/4, size/4, size/2, size/2);
            g2d.drawString("$", size*3/8, size*5/8);
        } else if (name.contains("Admiral") || name.contains("Distinction")) {
            // Admiral - badge/star symbol
            drawStar(g2d, size/2, size/2, size/3, 5);
        } else if (name.contains("Legend")) {
            // Legend - crown symbol
            int[] xPoints = {size/4, size/3, size*3/8, size/2, size*5/8, size*2/3, size*3/4};
            int[] yPoints = {size/2, size/3, size/2, size/3, size/2, size/3, size/2};
            g2d.fillPolygon(xPoints, yPoints, 7);
        }
    }
    
    private void drawStar(Graphics2D g2d, int centerX, int centerY, int radius, int points) {
        double angle = Math.PI / points;
        int[] xPoints = new int[points * 2];
        int[] yPoints = new int[points * 2];
        
        for (int i = 0; i < points * 2; i++) {
            double r = (i % 2 == 0) ? radius : radius / 2;
            xPoints[i] = (int) (centerX + r * Math.sin(i * angle));
            yPoints[i] = (int) (centerY + r * Math.cos(i * angle));
        }
        
        g2d.fillPolygon(xPoints, yPoints, points * 2);
    }
    
    private Color getRankColor(SpaceProfile.SpaceRank rank) {
        switch (rank) {
            case CADET:
                return new Color(100, 150, 255); // Blue
            case PILOT:
                return new Color(50, 200, 50);   // Green
            case CAPTAIN:
                return new Color(200, 150, 0);   // Bronze
            case COMMANDER:
                return new Color(180, 180, 180); // Silver
            case ADMIRAL:
                return new Color(255, 215, 0);   // Gold
            case EXPLORER:
                return new Color(180, 100, 255); // Purple
            case HERO:
                return new Color(255, 100, 100); // Red
            default:
                return new Color(255, 255, 255); // White
        }
    }
    
    private SpaceProfile.SpaceRank getNextRank(SpaceProfile.SpaceRank currentRank) {
        SpaceProfile.SpaceRank[] ranks = SpaceProfile.SpaceRank.values();
        int currentIndex = currentRank.ordinal();
        
        if (currentIndex < ranks.length - 1) {
            return ranks[currentIndex + 1];
        }
        
        return null; // Already at max rank
    }
    
    // Utility method to clamp color values to valid range
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    private void showCustomizeDialog() {
        JDialog customizeDialog = new JDialog(this, "Customize Spaceship", true);
        customizeDialog.setSize(450, 350);
        customizeDialog.setLocationRelativeTo(this);
        
        // Custom dialog panel
        JPanel dialogPanel = StyleUtils.createGradientPanel();
        dialogPanel.setLayout(new BorderLayout(20, 20));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Customize Your Spaceship");
        titleLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Form panel with styled fields
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setOpaque(false);
        
        JLabel shipNameLabel = new JLabel("Spaceship Name:");
        shipNameLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        
        JTextField shipNameField = new JTextField(user.getSpaceProfile().getSpaceshipName());
        styleTextField(shipNameField);
        
        JLabel homeWorldLabel = new JLabel("Home World:");
        homeWorldLabel.setForeground(StyleUtils.TEXT_BRIGHT);
        
        JTextField homeWorldField = new JTextField(user.getSpaceProfile().getHomeWorld());
        styleTextField(homeWorldField);
        
        formPanel.add(shipNameLabel);
        formPanel.add(shipNameField);
        formPanel.add(homeWorldLabel);
        formPanel.add(homeWorldField);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        
        JButton saveButton = StyleUtils.createStyledButton("SAVE", StyleUtils.ACCENT_GREEN);
        saveButton.addActionListener(e -> {
            // Update spaceship info
            user.getSpaceProfile().setSpaceshipName(shipNameField.getText());
            user.getSpaceProfile().setHomeWorld(homeWorldField.getText());
            
            // Save user data
            UserManager.getInstance().saveUserData();
            
            customizeDialog.dispose();
            
            // Update the space profile screen
            dispose();
            SpaceProfileScreen newScreen = new SpaceProfileScreen(mainMenu);
            newScreen.setVisible(true);
        });
        
        JButton cancelButton = StyleUtils.createStyledButton("CANCEL", StyleUtils.ACCENT_RED);
        cancelButton.addActionListener(e -> customizeDialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add components to dialog
        dialogPanel.add(titleLabel, BorderLayout.NORTH);
        dialogPanel.add(formPanel, BorderLayout.CENTER);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        customizeDialog.setContentPane(dialogPanel);
        customizeDialog.setVisible(true);
    }
    
    private void styleTextField(JTextField field) {
        field.setBackground(new Color(30, 40, 60));
        field.setForeground(StyleUtils.TEXT_BRIGHT);
        field.setCaretColor(StyleUtils.TEXT_BRIGHT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 80, 120), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
    }
}