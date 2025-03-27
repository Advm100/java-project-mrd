import javax.swing.*;

/**
 * Main application class that serves as the entry point
 */
public class MiniGamesApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Start with the login screen
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
    
    public static void addPoints(int points) {
        // Now points are managed by UserManager
        UserManager.getInstance().addPointsToCurrentUser(points);
    }
    
    public static int getTotalPoints() {
        // Get points from the current user
        if (UserManager.getInstance().getCurrentUser() != null) {
            return UserManager.getInstance().getCurrentUser().getTotalPoints();
        }
        return 0;
    }
}