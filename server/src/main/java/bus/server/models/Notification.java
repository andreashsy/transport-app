package bus.server.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class Notification {
    private String cronExpression;
    private String username;
    private String clientToken;
    private String busStopCode;
    private String messageBody;
    private String messageTitle;
    private String taskId;
    private static final Logger logger = Logger.getLogger(Notification.class.getName());

    public Notification() {
        generateId();
    }

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

    public static Notification populateFromClient(String jsonString) {
        Notification notification = new Notification();
        
        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
            
            String time = data.getString("time");
            String dayOfWeek = data.getString("dayOfWeek");
            String cronString = toCronExpression(time, dayOfWeek);
            notification.setCronExpression(cronString);
            notification.setBusStopCode(data.getString("busStopCode"));
            notification.setUsername(data.getString("username"));
            
        } catch (IOException e) {
            notification = new Notification();
            logger.log(Level.SEVERE, "IOError while parsing JSON String: " + e);
        }
        return notification;
    }

    private static String toCronExpression(String time, String dayOfWeek) {
        String cronTime = parseTimeToCron(time);
        String cronDay = parseDayOfWeektoCron(dayOfWeek);

        return cronTime + cronDay;
    }

    private static String parseTimeToCron(String time) {
        String minutes = time.split(":")[1];
        String hour = time.split(":")[0];
        return minutes + " " + hour;
    }

    private static String parseDayOfWeektoCron(String dayOfWeek) {
        String cronDay;
        switch (dayOfWeek) {
            case "weekdays":
                cronDay = " * * 1-5";
                break;

            case "weekends":
                cronDay = " * * 6,0";
                break;

            case "everyday":
                cronDay = " * * *";
                break;

            default:
                throw new IllegalArgumentException("Invalid day of week");
        }
        return cronDay;
    }

    private void generateId() {
        String id = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        setTaskId(id); 
    }

    public static Notification populateFromRowSet(SqlRowSet rs) {
        Notification notification = new Notification();
        notification.setTaskId(rs.getString("task_id"));
        notification.setUsername(rs.getString("username"));
        notification.setCronExpression(rs.getString("cron_time"));
        notification.setBusStopCode(rs.getString("bus_stop_id"));
        return notification;
    }

    public static Notification populateFromClientForDelete(String jsonString) {
        Notification notification = new Notification();
        
        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
            
            notification.setCronExpression(data.getString("dayOfWeek"));
            notification.setBusStopCode(data.getString("busStopCode"));
            
        } catch (IOException e) {
            notification = new Notification();
            logger.log(Level.SEVERE, "IOError while parsing JSON String: " + e);
        }
        return notification;
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

    public String getBusStopCode() {
        return this.busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

}
