package bus.server.repositories;

public class SQL {
    public static final String SQL_ADD_USER = "insert into users (username, password, notification_token) values (?, sha1(?), ?)";
    public static final String SQL_DELETE_USER = "delete from users where username = ?";
    public static final String SQL_UPDATE_FIREBASE_TOKEN = "update users set notification_token = ? where username = ?";
    public static final String SQL_CHECK_IF_USER_EXISTS = "select count(*) as count from users where username=?";
    public static final String SQL_SELECT_USER_BY_USERNAME = "select * from users where username = ?";
    public static final String SQL_COMPARE_PASSWORDS_BY_USERNAME = "select count(*) as user_count from users where username = ? and password = sha1(?)";
    public static final String SQL_ADD_FAVOURITE_BUS_STOP = "insert into bus_stops_favourites (bus_stop_id, username) values (?, ?)";
    public static final String SQL_CHECK_IF_FAVOURITE_BUS_STOP_EXISTS = "select count(*) as count from bus_stops_favourites where bus_stop_id = ? and username = ?";
    public static final String SQL_GET_FAVOURITE_BUS_STOPS = "select bs.bus_stop_id as id, bs.road_name as road_name, bs.description as description from bus_stop as bs inner join bus_stops_favourites as fav on bs.bus_stop_id = fav.bus_stop_id where fav.username = ?";
    public static final String SQL_DELETE_FAVOURITE_BUS_STOP = "delete from bus_stops_favourites where bus_stop_id = ? and username = ?";
    public static final String SQL_ADD_NOTIFICATION = "insert into notifications (task_id, username, cron_time, bus_stop_id) values (?, ?, ?, ?)";
    public static final String SQL_CHECK_IF_NOTIFICATION_EXISTS = "select count(*) as count from notifications where username = ? and cron_time = ? and bus_stop_id = ?";
    public static final String SQL_GET_NOTIFICATIONS = "select * from notifications where username = ?";
    public static final String SQL_DELETE_NOTIFICATION = "delete from notifications where username = ? and cron_time = ? and bus_stop_id = ?";
    public static final String SQL_GET_NOTIFICATION_ID = "select task_id from notifications where username = ? and cron_time = ? and bus_stop_id = ?";
    public static final String SQL_GET_FIREBASE_TOKEN = "select notification_token as token from users where username = ?";
    public static final String SQL_DELETE_ALL_BUS_STOPS = "delete from bus_stop";
    public static final String SQL_ADD_BUS_STOP = "insert into bus_stop (bus_stop_id, road_name, description) values (?, ?, ?)";
    public static final String SQL_SEARCH_BUS_STOPS = "select * from bus_stop where road_name like lower(?) or description like lower(?)";
    public static final String SQL_UPDATE_TELEGRAM_USERNAME = "update users set telegram_username = ? where username = ?";
    public static final String SQL_GET_USERNAME_FROM_TELEGRAM_USERNAME = "select username from users where telegram_username = ?";
    public static final String SQL_DOES_TELEGRAM_USER_EXIST = "select count(*) as count from users where telegram_username = ?";
}
