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
}
