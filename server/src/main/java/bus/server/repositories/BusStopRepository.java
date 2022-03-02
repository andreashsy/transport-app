package bus.server.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import bus.server.models.User;
import static bus.server.repositories.SQL.*;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@Repository
public class BusStopRepository {
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
            SQL_UPDATE_TOKEN, 
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
        return count == 1;
    }
}
