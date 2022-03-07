package bus.server.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bus.server.models.BusStop;
import bus.server.models.Notification;
import bus.server.models.User;
import bus.server.repositories.UserRepository;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public boolean addUser(String jsonString) {
        User user = User.populateFromJsonString(jsonString);
        if (!userRepository.doesUserExist(user)) {
            userRepository.addUser(user);
            return true;
        }
        return false;
    }

    public boolean updateToken(String jsonString) {
        User user = User.populateFromJsonString(jsonString);
        return userRepository.updateToken(user);
    }

    public boolean addFavourite(String username, String busStopCode) {
        if (!userRepository.doesFavouriteBusStopExist(username, busStopCode)) {
            userRepository.addFavourite(username, busStopCode);
            return true;
        }
        return false;
    }

    public Optional<List<BusStop>> getFavouriteBusStops(String username) {
        Optional<List<BusStop>> opt = userRepository.getFavouriteBusStops(username);
        return opt;
    }

    public static Optional<String> stringifyBusStop(Optional<List<BusStop>> busStops) {
        if (busStops.isPresent()) {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            for (BusStop busStop: busStops.get()) {
                JsonObject joo = Json.createObjectBuilder()
                .add("BusStopCode", busStop.getBusStopCode())
                .add("RoadName", busStop.getRoadName())
                .add("Description", busStop.getRoadName())
                .build();
                jab.add(joo);
            }
            JsonObject jo = Json.createObjectBuilder()
                .add("favourites", jab)
                .build();
            return Optional.of(jo.toString());
        }
        return Optional.empty();
    }

    public boolean deleteFavouriteBusStop(String username, String busStopCode) {
        return userRepository.deleteFavouriteBusStop(username, busStopCode);
    }

    public boolean addNotifcation(Notification notification) {
        if (!userRepository.doesNotificationExist(notification)) {
            return userRepository.addNotification(notification);
        }
        return false;
    }

    public Optional<String> getNotifications(String username) {
        Optional<List<Notification>> opt = userRepository.getNotifcations(username);
        if (opt.isPresent()) {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            for (Notification notification: opt.get()) {
                notification.generateTimeAndDay();
                JsonObject joNotification = Json.createObjectBuilder()
                    .add("busStopCode", notification.getBusStopCode())
                    .add("dayOfWeek", notification.getDayOfWeek())
                    .add("cronString", notification.getCronExpression())
                    .add("time", notification.getTime())
                    .build();
                jab.add(joNotification);
            }
            JsonObject jo = Json.createObjectBuilder()
                .add("notifications", jab)
                .build();
            return Optional.of(jo.toString());
        }
        return Optional.empty();
    }

    public boolean deleteNotification(Notification notification) {
        return userRepository.deleteNotification(notification);
    }

    public String getNotificationId(Notification notification) {
        Optional<String> opt = userRepository.getNotificationId(notification);
        if (opt.isPresent()) {
            return opt.get();
        }
        return "";
    }

    public String getFirebaseToken(String username) {
        Optional<String> opt = userRepository.getFirebaseToken(username);
        if (opt.isPresent()) {
            return opt.get();
        }
        return "";
    }

    public Optional<List<BusStop>> searchBusStops(String query) {
        return userRepository.searchBusStops(query);
    }

    public boolean setTelegramUsername(String username, String telegramUsername) {
        return userRepository.setTelegramUsername(username, telegramUsername);
    }

    public Optional<String> getUsernameFromTelegramUsername(String telegramUsername) {
        return userRepository.getUsernameFromTelegramUsername(telegramUsername);
    }

    public boolean doesTelegramUserExist(String telegramUsername) {
        return userRepository.doesTelegramUserExist(telegramUsername);
    }
}
