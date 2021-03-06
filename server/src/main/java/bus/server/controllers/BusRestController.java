package bus.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.server.models.BusStop;
import bus.server.services.BusStopService;
import bus.server.utilities.BusHelper;
import static bus.server.Constants.*;

import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class BusRestController {
    BusHelper busHelper = new BusHelper();

    @Autowired
    BusStopService busStopService;

    @GetMapping(path="/BusStop/{busstopId}")
    public ResponseEntity<String> getBusStopArrival(@PathVariable String busstopId) {
        String jsonData = busHelper.getBusStopById(busstopId);
        return ResponseEntity.ok(jsonData);
    }

    @GetMapping(path="/BusStops")
    public ResponseEntity<String> getAllBusStops() {
        List<BusStop> busStops = busHelper.getAllBusStops();
        String jsonData = busHelper.busStopToString(busStops);
        return ResponseEntity.ok(jsonData);
    }

    @GetMapping(path="/version")
    public ResponseEntity<String> getVersion() {
        String version = APP_VERSION;
        JsonObject jo = Json.createObjectBuilder()
            .add("version", version)
            .build();
        return ResponseEntity.ok(jo.toString());
    }
}
