package bus.server.utilities;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class HttpResponseHelper {
    
    public String jsonifyString(String title, String body) {
        JsonObject jo = Json.createObjectBuilder()
            .add(title, body)
            .build();
        return jo.toString();
    }
}
