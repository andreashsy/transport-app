package bus.server.repositories;

public class SQL {
    public static final String SQL_ADD_USER = "insert into users (username, password, notification_token) values (?, ?, ?)";
    public static final String SQL_DELETE_USER = "delete from users where username = ?";
    public static final String SQL_UPDATE_TOKEN = "update users set notification_token = ? where username = ?";
    public static final String SQL_CHECK_IF_USER_EXISTS = "select count(*) as count from users where username=?";
}