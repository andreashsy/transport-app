package bus.server.repositories;

public class SQL {
    public static final String SQL_ADD_USER = "insert into users (username, password, notification_token) values (?, sha1(?), ?)";
    public static final String SQL_DELETE_USER = "delete from users where username = ?";
    public static final String SQL_UPDATE_TOKEN = "update users set notification_token = ? where username = ?";
    public static final String SQL_CHECK_IF_USER_EXISTS = "select count(*) as count from users where username=?";
    public static final String SQL_SELECT_USER_BY_USERNAME = "select * from users where username = ?";
    public static final String SQL_COMPARE_PASSWORDS_BY_USERNAME = "select count(*) as user_count from users where username = ? and password = sha1(?)";
    public static final String SQL_ADD_FAVOURITE_BUS_STOP = "insert into bus_stops_favourites (bus_stop_id, username) values (?, ?)";
    public static final String SQL_CHECK_IF_FAVOURITE_BUS_STOP_EXISTS = "select count(*) as count from bus_stops_favourites where bus_stop_id = ? and username = ?";
    public static final String SQL_GET_FAVOURITE_BUS_STOPS = "select bus_stop_id from bus_stops_favourites where username = ?";
}
