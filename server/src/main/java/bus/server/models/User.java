package bus.server.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class User {
    private static final Logger logger = Logger.getLogger(User.class.getName());
    String username;
    String password;
    String notificationToken;

    public static User populateFromJsonString(String jsonString) {
        User user = new User();
        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();

            user.setUsername(data.getString("username"));
            user.setPassword(data.getString("password"));
            user.setNotificationToken(data.getString("notificationToken"));
            
        } catch (IOException e) {
            user = new User();
            logger.log(Level.SEVERE, "IOError while parsing JSON String: " + e);
        }
        return user;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNotificationToken() {
        return this.notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    
}
