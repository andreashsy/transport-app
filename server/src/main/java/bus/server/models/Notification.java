package bus.server.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Notification {
    private String cronExpression;
    private String username;
    private String clientToken;
    private String messageBody;
    private String messageTitle;
    private final Logger logger = Logger.getLogger(Notification.class.getName());

    public JsonObject generateNotification() {
        JsonObject jo = Json.createObjectBuilder()
            .add("notification", 
             Json.createObjectBuilder()
                .add("title", messageTitle)
                .add("body", messageBody))
            .add("to", clientToken)
            .build();
        logger.log(Level.INFO, "Notification Generated! " + jo.toString());
        return jo;
    }

    public String getCronExpression() {
        return this.cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientToken() {
        return this.clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getMessageBody() {
        return this.messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageTitle() {
        return this.messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }


}
