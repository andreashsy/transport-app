package bus.server.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bus.server.models.BusStop;
import bus.server.repositories.UserRepository;

@Service
public class BusStopService {
    @Autowired
    UserRepository userRepository;

    public boolean updateBusStops(List<BusStop> busStops) {
        Optional<String> opt = userRepository.deleteAllBusStops();
        int[] added = userRepository.batchaddBusStop(busStops);
        boolean isBatchSuccessful = !IntStream.of(added).anyMatch(x -> x == 0);
        return (isBatchSuccessful);
    }

}
