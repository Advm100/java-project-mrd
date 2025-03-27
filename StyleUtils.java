import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * Utility class for consistent styling across the application
 */
public class StyleUtils {
    // Common colors
    public static final Color BACKGROUND_DARK = new Color(30, 34, 42);
    public static final Color BACKGROUND_MEDIUM = new Color(40, 44, 52);
    public static final Color BACKGROUND_LIGHT = new Color(50, 54, 62);
    public static final Color ACCENT_BLUE = new Color(97, 175, 239);
    public static final Color ACCENT_GREEN = new Color(152, 195, 121);
    public static final Color ACCENT_YELLOW = new Color(229, 192, 123);
    public static final Color ACCENT_RED = new Color(224, 108, 117);
    public static final Color TEXT_BRIGHT = new Color(220, 223, 228);
    public static final Color TEXT_DIM = new Color(180, 183, 188);
    
    // Common dimensions
    public static final Dimension WINDOW_SIZE = new Dimension(900, 700);
    public static final int PADDING = 20;
    
    /**
     * Applies a custom dark theme to the game window
     */
    public static void applyGameStyle(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setSize(WINDOW_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Creates a panel with the standard gradient background
     */
    public static JPanel createGradientPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Create gradient background
                GradientPaint gp = new GradientPaint(
                    0, 0, BACKGROUND_DARK, 
                    getWidth(), getHeight(), BACKGROUND_LIGHT
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
    
    /**
     * Creates a styled title label
     */
    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_BRIGHT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    /**
     * Creates a styled subtitle label
     */
    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(ACCENT_BLUE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    /**
     * Creates a styled button
     */
    public static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(brighten(color, 0.15f));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a round-corner panel
     */
    public static JPanel createRoundPanel(Color background, int radius) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(background);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), radius, radius));
                
                g2.dispose();
            }
        };
        
        panel.setOpaque(false);
        return panel;
    }
    
    /**
     * Creates a coin icon for points display
     */
    public static ImageIcon createCoinIcon() {
        int size = 24;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw golden coin
        g2d.setColor(new Color(255, 215, 0)); // Gold color
        g2d.fillOval(0, 0, size, size);
        
        // Draw highlight
        g2d.setColor(new Color(255, 235, 100));
        g2d.fillOval(size/4, size/4, size/2, size/2);
        
        // Draw border
        g2d.setColor(new Color(180, 150, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(1, 1, size-2, size-2);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Brightens a color by the specified factor
     */
    private static Color brighten(Color color, float factor) {
        int r = Math.min(255, (int)(color.getRed() * (1 + factor)));
        int g = Math.min(255, (int)(color.getGreen() * (1 + factor)));
        int b = Math.min(255, (int)(color.getBlue() * (1 + factor)));
        return new Color(r, g, b);
    }
}