package bus.server.controllers.secure;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path="/secure", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecuredBusRestController {
    private final Logger logger = Logger.getLogger(SecuredBusRestController.class.getName());

    @PostMapping(path="/favourite/{username}")
    public ResponseEntity<String> saveFavourite(
        @RequestBody String body,
        @PathVariable String username
    ) {
        logger.log(Level.INFO, "username: %s, request body: %s".formatted(username, body));
        return ResponseEntity.ok("{}");
    }
}
