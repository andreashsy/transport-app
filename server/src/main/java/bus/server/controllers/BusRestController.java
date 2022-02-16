package bus.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.server.services.BusService;
import bus.server.services.VersionService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class BusRestController {

    @Autowired
    BusService busService;

    @Autowired
    VersionService versionService;
    
    @GetMapping(path="/BusStop/{busstopId}")
    public ResponseEntity<String> getBusStopArrival(@PathVariable String busstopId) {
        String jsonData = busService.getBusStopById(busstopId);
        return ResponseEntity.ok(jsonData);
    }

    @GetMapping(path="/BusStops")
    public ResponseEntity<String> getAllBusStops() {
        String jsonData = busService.getAllBusStops();
        return ResponseEntity.ok(jsonData);
    }

    @GetMapping(path="/version")
    public ResponseEntity<String> getVersion() {
        String version = this.versionService.getVersion();
        JsonObject jo = Json.createObjectBuilder()
            .add("version", version)
            .build();
        return ResponseEntity.ok(jo.toString());
    }
}
