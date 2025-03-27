import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a user account in the game system
 * Now with space-themed profile features
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private int totalPoints;
    private Map<String, Integer> gameScores;
    private SpaceProfile spaceProfile;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.totalPoints = 0;
        this.gameScores = new HashMap<>();
        this.spaceProfile = new SpaceProfile();
        
        // Unlock first achievement
        this.spaceProfile.unlockAchievement("First Contact");
    }
    
    public String getUsername() {
        return username;
    }
    
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    public int getTotalPoints() {
        return totalPoints;
    }
    
    public void addPoints(int points) {
        this.totalPoints += points;
        
        // Make sure space profile is initialized
        getSpaceProfile().updateRank(totalPoints);
        
        // Check for point-based achievements
        if (totalPoints >= 1000) {
            getSpaceProfile().unlockAchievement("Galactic Collector");
        }
        
        if (getSpaceProfile().getCurrentRank() == SpaceProfile.SpaceRank.ADMIRAL 
                || getSpaceProfile().getCurrentRank().ordinal() > SpaceProfile.SpaceRank.ADMIRAL.ordinal()) {
            getSpaceProfile().unlockAchievement("Admiral's Distinction");
        }
        
        // Check if all other achievements are unlocked for the Legend achievement
        boolean allOthersUnlocked = true;
        for (SpaceProfile.Achievement achievement : getSpaceProfile().getAchievements()) {
            if (!achievement.isUnlocked() && !achievement.getName().equals("Space Legend")) {
                allOthersUnlocked = false;
                break;
            }
        }
        
        if (allOthersUnlocked) {
            getSpaceProfile().unlockAchievement("Space Legend");
        }
    }
    
    public void updateGameScore(String gameName, int score) {
        // Only update if it's a new high score
        if (!gameScores.containsKey(gameName) || score > gameScores.get(gameName)) {
            gameScores.put(gameName, score);
            
            // Record as mission completion in space profile
            getSpaceProfile().completeMission();
            
            // Check for game-specific achievements
            if (gameName.equals("Memory Game") && score >= 500) {
                getSpaceProfile().unlockAchievement("Memory Master");
            }
            
            if (gameName.equals("Snake Game") && score >= 150) {
                getSpaceProfile().unlockAchievement("Serpent Navigator");
            }
        }
    }
    
    public int getGameScore(String gameName) {
        return gameScores.getOrDefault(gameName, 0);
    }
    
    public Map<String, Integer> getAllGameScores() {
        return new HashMap<>(gameScores);
    }
    
    public SpaceProfile getSpaceProfile() {
        // Handle legacy users who don't have a space profile yet
        if (spaceProfile == null) {
            spaceProfile = new SpaceProfile();
            // Unlock first achievement
            spaceProfile.unlockAchievement("First Contact");
            // Update rank based on current points
            spaceProfile.updateRank(totalPoints);
        }
        return spaceProfile;
    }
    
    public String getCurrentRankTitle() {
        return getSpaceProfile().getCurrentRank().getTitle();
    }
    
    public int getUnlockedAchievementsCount() {
        return getSpaceProfile().getUnlockedAchievementsCount();
    }
    
    public int getTotalAchievementsCount() {
        return getSpaceProfile().getAchievements().size();
    }
}