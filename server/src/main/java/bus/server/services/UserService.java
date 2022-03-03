package bus.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bus.server.models.User;
import bus.server.repositories.UserRepository;

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


}
