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

    String demo = """
    {"Message": "DEMOOOOO", "odata.metadata":"http://datamall2.mytransport.sg/ltaodataservice/$metadata#BusArrivalv2/@Element","BusStopCode":"20239","Services":[{"ServiceNo":"143M","Operator":"TTS","NextBus":{"OriginCode":"28009","DestinationCode":"28009","EstimatedArrival":"2022-02-16T04:55:06+08:00","Latitude":"1.3186863333333334","Longitude":"103.74820733333334","VisitNumber":"1","Load":"SDA","Feature":"WAB","Type":"SD"},"NextBus2":{"OriginCode":"28009","DestinationCode":"28009","EstimatedArrival":"2022-02-16T05:18:17+08:00","Latitude":"0","Longitude":"0","VisitNumber":"1","Load":"LSD","Feature":"WAB","Type":"SD"},"NextBus3":{"OriginCode":"","DestinationCode":"","EstimatedArrival":"","Latitude":"","Longitude":"","VisitNumber":"","Load":"","Feature":"","Type":""}}]} """;

    @Autowired
    BusService busService;

    @Autowired
    VersionService versionService;
    
    @GetMapping(path="/BusStop/{busstopId}")
    public ResponseEntity<String> getBusStopArrival(@PathVariable String busstopId) {
        //String jsonData = busService.getBusStopById(busstopId);
        //return ResponseEntity.ok(jsonData);

        return ResponseEntity.ok(this.demo);
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
