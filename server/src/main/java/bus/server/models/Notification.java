package bus.server.models;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class Notification {
    private String dayOfWeek;
    private String time;
    private String cronExpression;
    private String username;
    private String firebaseToken;
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
            .add("to", firebaseToken)
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
        return "1 " + minutes + " " + hour;
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

    public void generateTimeAndDay() {
        String cronDay = this.cronExpression.split(" ")[5];
        switch (cronDay) {
            case "1-5":
                this.dayOfWeek = "weekdays";
                break;
            case "6,0":
                this.dayOfWeek = "weekends";
                break;
            case "*":
                this.dayOfWeek = "everday";
                break;
            default:
                throw new IllegalArgumentException("Invalid cron expression");
        }

        this.time = this.cronExpression.split(" ")[2] + ":" + this.cronExpression.split(" ")[1];
    }

    public static String parseJsonNotification(String jsonString) {
        String messageBody = "";
        List<BusService> busServices = new LinkedList<BusService>();
        try (InputStream is = new ByteArrayInputStream(jsonString.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();

            
            JsonArray services = data.getJsonArray("Services");

            for (JsonValue jv:services) {
                JsonObject busServiceObj = jv.asJsonObject();
                BusService busService = new BusService();
                String serviceNumber = busServiceObj.getString("ServiceNo");
                busService.setServiceNumber(serviceNumber);

                JsonObject nextBusObj = busServiceObj.getJsonObject("NextBus");
                JsonObject nextBus2Obj = busServiceObj.getJsonObject("NextBus2");
                JsonObject nextBus3Obj = busServiceObj.getJsonObject("NextBus3");

                Bus nextBus = new Bus();
                Bus nextBus2 = new Bus();
                Bus nextBus3 = new Bus();

                nextBus.setLoad(nextBusObj.getString("Load"));
                nextBus2.setLoad(nextBus2Obj.getString("Load"));
                nextBus3.setLoad(nextBus3Obj.getString("Load"));

                LocalDateTime estArr = LocalDateTime.parse(nextBusObj.getString("EstimatedArrival"));
                LocalDateTime estArr2 = LocalDateTime.parse(nextBus2Obj.getString("EstimatedArrival"));
                LocalDateTime estArr3 = LocalDateTime.parse(nextBus3Obj.getString("EstimatedArrival"));

                nextBus.setEstimatedArrival(estArr);
                nextBus2.setEstimatedArrival(estArr2);
                nextBus3.setEstimatedArrival(estArr3);

                busService.setNextBus(nextBus);
                busService.setNextBus2(nextBus2);
                busService.setNextBus3(nextBus3);

                busServices.add(busService);
            }

            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOError while parsing JSON String: " + e);
            return "error";
        }

        for (BusService busSvc:busServices) {
            messageBody += "Service %s: %s mins, %s mins, %s mins ".formatted(
                busSvc.serviceNumber, 
                busSvc.getNextBus().estimatedArrival.toString(), 
                busSvc.getNextBus2().estimatedArrival.toString(),
                busSvc.getNextBus3().estimatedArrival.toString());
        }
        System.out.println(messageBody);
        return messageBody;
    }

    public String getDayOfWeek() {
        return this.dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getFirebaseToken() {
        return this.firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
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
