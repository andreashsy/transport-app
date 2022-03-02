package bus.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bus.server.models.User;
import bus.server.repositories.BusStopRepository;

@Service
public class UserService {
    @Autowired
    BusStopRepository busStopRepository;

    public boolean addUser(String jsonString) {
        User user = User.populateFromJsonString(jsonString);
        if (!busStopRepository.doesUserExist(user)) {
            busStopRepository.addUser(user);
            return true;
        }
        return false;
    }


}
