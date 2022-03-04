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

    public boolean addFavourite(String username, String busStopCode) {
        if (!userRepository.doesFavouriteBusStopExist(username, busStopCode)) {
            userRepository.addFavourite(username, busStopCode);
            return true;
        }
        return false;
    }

    public Optional<String> getFavouriteBusStops(String username) {
        Optional<List<BusStop>> opt = userRepository.getFavouriteBusStops(username);

        if (opt.isPresent()) {
            JsonArrayBuilder jab = Json.createArrayBuilder();
            for (BusStop busStop: opt.get()) {
                jab.add(busStop.getBusStopCode());
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
                JsonObject joNotification = Json.createObjectBuilder()
                    .add("busStopCode", notification.getBusStopCode())
                    .add("cronString", notification.getCronExpression())
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


}
