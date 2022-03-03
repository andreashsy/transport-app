package bus.server.controllers.secure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.server.services.UserService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path="/secure", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecuredBusRestController {
    private final Logger logger = Logger.getLogger(SecuredBusRestController.class.getName());

    @Autowired
    UserService userService;

    @PostMapping(path="/favourite/{username}")
    public ResponseEntity<String> saveFavourite(
        @RequestBody String body,
        @PathVariable String username
    ) {
        logger.log(Level.INFO, "username: %s, request body: %s".formatted(username, body));
        boolean favouriteAdded = userService.addFavourite(username, body);
        JsonObject jo = Json.createObjectBuilder()
            .add("is favourite added?", favouriteAdded)
            .build();

        return ResponseEntity.ok(jo.toString());
    }

    @GetMapping(path="/favourite/{username}")
    public ResponseEntity<String> getFavourites(@PathVariable String username) {
        Optional<String> opt = userService.getFavouriteBusStops(username);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.ok("{}");
    }
}
