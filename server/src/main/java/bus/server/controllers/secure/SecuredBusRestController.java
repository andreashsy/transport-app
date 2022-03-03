package bus.server.controllers.secure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.server.services.AuthenticateService;
import bus.server.services.HttpResponseService;
import bus.server.services.UserService;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path="/secure", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecuredBusRestController {
    private final Logger logger = Logger.getLogger(SecuredBusRestController.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    AuthenticateService authService;

    @Autowired
    HttpResponseService httpResponseService;

    @PostMapping(path="/favourite/{username}")
    public ResponseEntity<String> saveFavourite(
        @RequestBody String body,
        @PathVariable String username
    ) {
        logger.log(Level.INFO, "username: %s, request body: %s".formatted(username, body));
        boolean favouriteAdded = userService.addFavourite(username, body);
   
        return ResponseEntity.ok(
            httpResponseService.jsonifyString(
                "is delete successful?", String.valueOf(favouriteAdded)));
    }

    @GetMapping(path="/favourite/{username}")
    public ResponseEntity<String> getFavourites(
        @PathVariable String username,
        @RequestHeader String authorization
        ) {        
        Optional<String> opt = userService.getFavouriteBusStops(username);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.ok("{}");
    }

    @DeleteMapping(path="/favourite/{username}/{busStopCode}")
    public ResponseEntity<String> deleteFavourite(
        @PathVariable String username,
        @PathVariable String busStopCode
    ) {
        boolean isDeleted = userService.deleteFavouriteBusStop(username, busStopCode);
       
        return ResponseEntity.ok(
            httpResponseService.jsonifyString(
                "is delete successful?", String.valueOf(isDeleted)));
    }
}
