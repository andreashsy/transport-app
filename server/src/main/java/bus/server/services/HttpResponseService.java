package bus.server.services;

import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class HttpResponseService {
    
    public String jsonifyString(String title, String body) {
        JsonObject jo = Json.createObjectBuilder()
            .add(title, body)
            .build();
        return jo.toString();
    }
}
