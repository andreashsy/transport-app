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

import bus.server.models.Notification;
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

    @PostMapping(path="/favourite")
    public ResponseEntity<String> saveFavourite(
        @RequestBody String body,
        @RequestHeader String username
    ) {
        logger.log(Level.INFO, "username: %s, request body: %s".formatted(username, body));
        boolean favouriteAdded = userService.addFavourite(username, body);
   
        return ResponseEntity.ok(
            httpResponseService.jsonifyString(
                "is delete successful?", String.valueOf(favouriteAdded)));
    }

    @GetMapping(path="/favourite")
    public ResponseEntity<String> getFavourites(
        @RequestHeader String username
        ) {        
        Optional<String> opt = userService.getFavouriteBusStops(username);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.ok("{}");
    }

    @DeleteMapping(path="/favourite/{busStopCode}")
    public ResponseEntity<String> deleteFavourite(
        @RequestHeader String username,
        @PathVariable String busStopCode
    ) {
        boolean isDeleted = userService.deleteFavouriteBusStop(username, busStopCode);
       
        return ResponseEntity.ok(
            httpResponseService.jsonifyString(
                "is delete successful?", String.valueOf(isDeleted)));
    }

    @PostMapping(path="/notification")
    public ResponseEntity<String> addNotifcation(
        @RequestBody String reqBody, 
        @RequestHeader String username
        ) {
        logger.log(Level.INFO, username + " " + reqBody);
        Notification notification = Notification.populateFromClient(reqBody);
        
        boolean isAdded = userService.addNotifcation(notification);
        return ResponseEntity.ok(
            httpResponseService.jsonifyString(
                "is add successful?", String.valueOf(isAdded)));
    }

    @GetMapping(path="/notification")
    public ResponseEntity<String> getNotifcations(
        @RequestHeader String username
        ) {
        Optional<String> opt = userService.getNotifications(username);
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get());
        }
        return ResponseEntity.ok("{}");
        }

    @DeleteMapping(path="/notification/{busStopCode}/{cronString}")
    public ResponseEntity<String> deleteNotification(
        @RequestHeader String username,
        @PathVariable String busStopCode,
        @PathVariable String cronString
    ) {
        Notification notification = new Notification();
        notification.setUsername(username);
        notification.setBusStopCode(busStopCode);
        notification.setCronExpression(cronString);
        boolean isDeleted = userService.deleteNotification(notification);
       
        return ResponseEntity.ok(
            httpResponseService.jsonifyString(
                "is delete successful?", String.valueOf(isDeleted)));
    }
}
