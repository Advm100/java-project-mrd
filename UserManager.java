import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages user accounts including registration, login, and persistence
 */
public class UserManager {
    private static final String USER_DATA_FILE = "user_data.ser";
    
    private Map<String, User> users;
    private User currentUser;
    
    // Singleton pattern
    private static UserManager instance;
    
    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }
    
    private UserManager() {
        users = new HashMap<>();
        loadUsers();
    }
    
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false; // User already exists
        }
        
        User newUser = new User(username, password);
        users.put(username, newUser);
        saveUsers();
        return true;
    }
    
    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logoutUser() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void addPointsToCurrentUser(int points) {
        if (currentUser != null) {
            currentUser.addPoints(points);
            saveUsers();
        }
    }
    
    public void updateGameScore(String gameName, int score) {
        if (currentUser != null) {
            currentUser.updateGameScore(gameName, score);
            saveUsers();
        }
    }
    
    private void loadUsers() {
        try {
            File file = new File(USER_DATA_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                users = (Map<String, User>) ois.readObject();
                ois.close();
                fis.close();
            }
        } catch (Exception e) {
            System.out.println("Error loading user data: " + e.getMessage());
            // If there's an error, start with empty user map
            users = new HashMap<>();
        }
    }
    
    private void saveUsers() {
        try {
            FileOutputStream fos = new FileOutputStream(USER_DATA_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
            oos.close();
            fos.close();
        } catch (Exception e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    public void saveUserData() {
        saveUsers();
    }
}
