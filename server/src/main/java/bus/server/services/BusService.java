package bus.server.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import bus.server.models.BusStop;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

import static bus.server.Constants.*;


@Service
public class BusService {
    private final Logger logger = Logger.getLogger(BusService.class.getName());
    private String accountKey;

    public BusService() {
        if ((KEY_LTA != null) && (KEY_LTA.trim().length() > 0)) {
            this.accountKey = KEY_LTA;
            logger.log(Level.INFO, ">>> LTA KEY SET!");
        } else {
            this.accountKey = "API KEY NOT FOUND";
            logger.log(Level.INFO, ">>> ERROR!! LTA KEY NOT FOUND!");
        }
    }

    public String getBusStopById(String id) {
        String url = UriComponentsBuilder
            .fromUriString(URL_BUS_STOP_BY_ID)
            .queryParam("BusStopCode", id)
            .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("AccountKey", this.accountKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(url, HttpMethod.GET, entity, String.class);

        return resp.getBody();
    }

    public String getAllBusStops() {
        List<BusStop> busStops = new LinkedList<BusStop>();
        int loopCount = 1;
        ResponseEntity<String> resp = new ResponseEntity<String>(HttpStatus.I_AM_A_TEAPOT);

        while (loopCount <= 20) {
            String url = "";

            if (loopCount == 1) {
            url = UriComponentsBuilder
                .fromUriString(URL_ALL_BUS_STOPS)
                .toUriString();
            } else {
            url = UriComponentsBuilder
                .fromUriString(URL_ALL_BUS_STOPS)
                .queryParam("$skip", (loopCount - 1)*500)
                .toUriString();
            }
            System.out.println("Url is: " + url);
            HttpHeaders headers = new HttpHeaders();
            headers.set("AccountKey", this.accountKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate template = new RestTemplate();
            resp = template.exchange(url, HttpMethod.GET, entity, String.class);

            try (InputStream is = new ByteArrayInputStream(resp.toString().replaceFirst("<200,", "").getBytes())) {
                JsonReader reader = Json.createReader(is);
                JsonObject data = reader.readObject();
                
                JsonArray jsonBusStops = data.getJsonArray("value");
                for (JsonValue jsonBusStop:jsonBusStops) {
                    BusStop busStop = new BusStop();
                    JsonObject jo = jsonBusStop.asJsonObject();
                    busStop.setBusStopCode(jo.getString("BusStopCode"));
                    busStop.setRoadName(jo.getString("RoadName"));
                    busStop.setDescription(jo.getString("Description"));
                    busStop.setLatitude(jo.getJsonNumber("Latitude").toString());
                    busStop.setLongitude(jo.getJsonNumber("Longitude").toString());
                    busStops.add(busStop);
                }
                loopCount ++;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "ERROR: " + e);
            }

            if (busStops.size() % 500 != 0) {
                break;
            }
        }

        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (BusStop busStop: busStops) {
            JsonObject jo1 = Json.createObjectBuilder()
                .add("BusStopCode", busStop.getBusStopCode())
                .add("RoadName", busStop.getRoadName())
                .add("Description", busStop.getDescription())
                .add("Latitude", busStop.getLatitude())
                .add("Longitude", busStop.getLongitude())
                .build();
            jab.add(jo1);
        }
        JsonArray ja = jab.build();

        return ja.toString();
    }
}
