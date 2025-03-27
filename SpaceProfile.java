import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Space-themed profile extension for users
 */
public class SpaceProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Space ranks that users can achieve
    public enum SpaceRank {
        CADET("Space Cadet", 0),
        PILOT("Space Pilot", 100),
        CAPTAIN("Space Captain", 300),
        COMMANDER("Space Commander", 600),
        ADMIRAL("Space Admiral", 1000),
        EXPLORER("Galactic Explorer", 1500),
        HERO("Galactic Hero", 2000);
        
        private final String title;
        private final int pointsRequired;
        
        SpaceRank(String title, int pointsRequired) {
            this.title = title;
            this.pointsRequired = pointsRequired;
        }
        
        public String getTitle() {
            return title;
        }
        
        public int getPointsRequired() {
            return pointsRequired;
        }
        
        public static SpaceRank getRankForPoints(int points) {
            SpaceRank highestRank = CADET;
            for (SpaceRank rank : values()) {
                if (points >= rank.getPointsRequired()) {
                    highestRank = rank;
                } else {
                    break;
                }
            }
            return highestRank;
        }
    }
    
    // Space achievements
    public static class Achievement implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private String name;
        private String description;
        private boolean unlocked;
        
        public Achievement(String name, String description) {
            this.name = name;
            this.description = description;
            this.unlocked = false;
        }
        
        public String getName() {
            return name;
        }
        
        public String getDescription() {
            return description;
        }
        
        public boolean isUnlocked() {
            return unlocked;
        }
        
        public void unlock() {
            this.unlocked = true;
        }
    }
    
    // Space profile properties
    private String spaceshipName;
    private String homeWorld;
    private List<Achievement> achievements;
    private SpaceRank currentRank;
    private int missionCount;
    
    public SpaceProfile() {
        this.spaceshipName = generateRandomSpaceshipName();
        this.homeWorld = generateRandomPlanet();
        this.achievements = createDefaultAchievements();
        this.currentRank = SpaceRank.CADET;
        this.missionCount = 0;
    }
    
    public void updateRank(int totalPoints) {
        this.currentRank = SpaceRank.getRankForPoints(totalPoints);
    }
    
    public void completeMission() {
        this.missionCount++;
        
        // Check for mission-based achievements
        if (missionCount == 5) {
            unlockAchievement("Space Voyager");
        }
        if (missionCount == 10) {
            unlockAchievement("Mission Specialist");
        }
        if (missionCount == 25) {
            unlockAchievement("Veteran Explorer");
        }
    }
    
    public void unlockAchievement(String name) {
        for (Achievement achievement : achievements) {
            if (achievement.getName().equals(name) && !achievement.isUnlocked()) {
                achievement.unlock();
                break;
            }
        }
    }
    
    public String getSpaceshipName() {
        return spaceshipName;
    }
    
    public void setSpaceshipName(String spaceshipName) {
        this.spaceshipName = spaceshipName;
    }
    
    public String getHomeWorld() {
        return homeWorld;
    }
    
    public void setHomeWorld(String homeWorld) {
        this.homeWorld = homeWorld;
    }
    
    public SpaceRank getCurrentRank() {
        return currentRank;
    }
    
    public int getMissionCount() {
        return missionCount;
    }
    
    public List<Achievement> getAchievements() {
        return new ArrayList<>(achievements);
    }
    
    public int getUnlockedAchievementsCount() {
        int count = 0;
        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked()) {
                count++;
            }
        }
        return count;
    }
    
    // Generate random spaceship name for new accounts
    private String generateRandomSpaceshipName() {
        String[] prefixes = {"Stellar", "Cosmic", "Nova", "Astro", "Galactic", "Nebula", "Solar", "Lunar"};
        String[] suffixes = {"Voyager", "Explorer", "Discovery", "Falcon", "Phoenix", "Horizon", "Endeavor", "Pioneer"};
        
        Random random = new Random();
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        
        return prefix + " " + suffix;
    }
    
    // Generate random home planet for new accounts
    private String generateRandomPlanet() {
        String[] planets = {
            "Terra Nova", "Zephyria", "Aquarius Prime", "Nebulon", "Celestia", 
            "Astralis", "Orion's Haven", "Proxima Centauri b", "Luminara", "Andromeda's Edge"
        };
        
        Random random = new Random();
        return planets[random.nextInt(planets.length)];
    }
    
    // Create default achievement list
    private List<Achievement> createDefaultAchievements() {
        List<Achievement> defaultAchievements = new ArrayList<>();
        
        defaultAchievements.add(new Achievement("First Contact", "Play your first game"));
        defaultAchievements.add(new Achievement("Space Voyager", "Complete 5 missions (games)"));
        defaultAchievements.add(new Achievement("Mission Specialist", "Complete 10 missions"));
        defaultAchievements.add(new Achievement("Veteran Explorer", "Complete 25 missions"));
        defaultAchievements.add(new Achievement("Memory Master", "Score 500+ in Memory Game"));
        defaultAchievements.add(new Achievement("Serpent Navigator", "Score 150+ in Snake Game"));
        defaultAchievements.add(new Achievement("Galactic Collector", "Earn 1000 points total"));
        defaultAchievements.add(new Achievement("Admiral's Distinction", "Reach Admiral rank"));
        defaultAchievements.add(new Achievement("Space Legend", "Unlock all other achievements"));
        
        return defaultAchievements;
    }
}