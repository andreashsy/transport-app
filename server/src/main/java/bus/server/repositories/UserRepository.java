package bus.server.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import bus.server.models.BusStop;
import bus.server.models.Notification;
import bus.server.models.User;
import static bus.server.repositories.SQL.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@Repository
public class UserRepository {
    private static final Logger logger = Logger.getLogger(UserRepository.class.getName());

    @Autowired
    JdbcTemplate template;

    public boolean addUser(User user) {
        final int usersAdded = template.update(
            SQL_ADD_USER,
            user.getUsername(),
            user.getPassword(),
            user.getNotificationToken());
        return usersAdded > 0;
    }

    public boolean deleteUser(User user) {
        final int usersDeleted = template.update(
            SQL_DELETE_USER, 
            user.getUsername());
        return usersDeleted > 0;
    }

    public boolean updateToken(User user) {
        final int usersChanged = template.update(
            SQL_UPDATE_FIREBASE_TOKEN, 
            user.getNotificationToken(),
            user.getUsername());
        return usersChanged > 0;
    }

    public boolean doesUserExist(User user) {
        int count = 0;
        final SqlRowSet rs = template.queryForRowSet(SQL_CHECK_IF_USER_EXISTS, user.getUsername());
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count > 0;
    }

    public Optional<User> findUserByName(String username) {
		final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME, username);
		if (rs.next())
			return Optional.of(User.populateFromRowSet(rs));
		return Optional.empty();
	}

	public boolean validateUser(String username, String password) {
		final SqlRowSet rs = template.queryForRowSet(SQL_COMPARE_PASSWORDS_BY_USERNAME, username, password);
		if (!rs.next())
			return false;

		return rs.getInt("user_count") > 0;
	}

    public boolean addFavourite(String username, String busStopCode) {
        final int favouritesAdded = template.update(SQL_ADD_FAVOURITE_BUS_STOP, busStopCode, username);
        return favouritesAdded > 0;
    }

    public boolean doesFavouriteBusStopExist(String username, String busStopCode) {
        int count = 0;
        final SqlRowSet rs = template.queryForRowSet(SQL_CHECK_IF_FAVOURITE_BUS_STOP_EXISTS, busStopCode, username);
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count > 0;
    }

    public Optional<List<BusStop>> getFavouriteBusStops(String username) {
        List<BusStop> busStops = new LinkedList<BusStop>();
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_FAVOURITE_BUS_STOPS, username);
		while (rs.next()) {
            BusStop busStop = new BusStop();
            busStop.setBusStopCode(rs.getString("bus_stop_id"));
            busStops.add(busStop);
			
        }
        return Optional.of(busStops);
    } 

    public boolean deleteFavouriteBusStop(String username, String busStopCode) {
        final int busStopsDeleted = template.update(
            SQL_DELETE_FAVOURITE_BUS_STOP, 
            busStopCode, 
            username);

        return busStopsDeleted > 0;
    }

    public boolean doesNotificationExist(Notification notification) {
        int count = 0;
        final SqlRowSet rs = template.queryForRowSet(
            SQL_CHECK_IF_NOTIFICATION_EXISTS, 
            notification.getUsername(), 
            notification.getCronExpression(), 
            notification.getBusStopCode());
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count > 0;
    }

    public boolean addNotification(Notification notification) {
        final int notificationsAdded = template.update(
            SQL_ADD_NOTIFICATION, 
            notification.getTaskId(), 
            notification.getUsername(), 
            notification.getCronExpression(), 
            notification.getBusStopCode());
        return notificationsAdded > 0;
    }

    public Optional<List<Notification>> getNotifcations(String username) {
        List<Notification> notifications = new LinkedList<Notification>();
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_NOTIFICATIONS, username);
		while (rs.next()) {
            Notification notification = Notification.populateFromRowSet(rs);
            notifications.add(notification);
        }
        return Optional.of(notifications);
    }

    public boolean deleteNotification(Notification notification) {
        final int notificationsDeleted = template.update(
            SQL_DELETE_NOTIFICATION, 
            notification.getUsername(),
            notification.getCronExpression(),
            notification.getBusStopCode());
        return notificationsDeleted > 0;
    }

    public Optional<String> getNotificationId(Notification notification) {
        String id = "";
        final SqlRowSet rs = template.queryForRowSet(
            SQL_GET_NOTIFICATION_ID, 
            notification.getUsername(), 
            notification.getCronExpression(), 
            notification.getBusStopCode());
        while (rs.next()) {
            id = rs.getString("task_id");
        }
        return Optional.of(id);
    }

    public Optional<String> getFirebaseToken(String username) {
        String token = "";
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_FIREBASE_TOKEN, username);
        while (rs.next()) {
            token = rs.getString("token");
        }
        return Optional.of(token);
    }

    public Optional<String> deleteAllBusStops() {
        final int numDeleted = template.update(SQL_DELETE_ALL_BUS_STOPS);
        return Optional.of(String.valueOf(numDeleted));
    }

    public int[] batchaddBusStop(List<BusStop> busStops) {
        List<Object[]> params = busStops.stream()
            .map(bs -> new Object[]{bs.getBusStopCode(), bs.getRoadName(), bs.getDescription()})
            .collect(Collectors.toList());
        
        int added[] = template.batchUpdate(SQL_ADD_BUS_STOP, params);

        return added;
    }

    public Optional<List<BusStop>> searchBusStops(String query) {
        String q = "%" + query + "%";
        List<BusStop> busStops = new LinkedList<BusStop>();
        final SqlRowSet rs = template.queryForRowSet(SQL_SEARCH_BUS_STOPS, q, q);
		while (rs.next()) {
            BusStop busStop = new BusStop();
            busStop.setBusStopCode(rs.getString("bus_stop_id"));
            busStop.setDescription(rs.getString("description"));
            busStop.setRoadName(rs.getString("road_name"));
            busStops.add(busStop);
        }
        if (busStops.size() < 1) {return Optional.empty();}
        return Optional.of(busStops);
    } 

    public Optional<String> getUsernameFromTelegramUsername(String telegramUsername) {
        final SqlRowSet rs = template.queryForRowSet(SQL_GET_USERNAME_FROM_TELEGRAM_USERNAME, telegramUsername);
        if (rs.next()) {
            return Optional.of(rs.getString("username"));
        }
        return Optional.empty();
    }   

    public boolean setTelegramUsername(String username, String telegramUsername) {
        final int recordsUpdated = template.update(
            SQL_UPDATE_TELEGRAM_USERNAME, telegramUsername, username);
        return recordsUpdated > 0;
    }

    public boolean doesTelegramUserExist(String telegramUsername) {
        int count = 0;
        final SqlRowSet rs = template.queryForRowSet(SQL_DOES_TELEGRAM_USER_EXIST, telegramUsername);
        while (rs.next()) {
            count = rs.getInt("count");
        }
        return count > 0;
    }
}
