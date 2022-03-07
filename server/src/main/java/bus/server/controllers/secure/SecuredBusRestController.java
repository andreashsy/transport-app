package bus.server.controllers.secure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.server.models.BusStop;
import bus.server.models.Notification;
import bus.server.models.NotificationTask;
import bus.server.services.AuthenticateService;
import bus.server.services.BusStopService;
import bus.server.services.TaskSchedulingService;
import bus.server.services.UserService;
import bus.server.utilities.BusHelper;
import bus.server.utilities.HttpResponseHelper;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path="/api/secure", produces = MediaType.APPLICATION_JSON_VALUE)
public class SecuredBusRestController {
    private final Logger logger = Logger.getLogger(SecuredBusRestController.class.getName());
    HttpResponseHelper httpResponseHelper = new HttpResponseHelper();
    BusHelper busHelper = new BusHelper();

    @Autowired
    BusStopService busStopService;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticateService authService;

    @Autowired
    TaskSchedulingService taskSchedulingService;

    @PostMapping(path="/favourite")
    public ResponseEntity<String> saveFavourite(
        @RequestBody String body,
        @RequestHeader String username
    ) {
        logger.log(Level.INFO, "username: %s, request body: %s".formatted(username, body));
        boolean favouriteAdded = userService.addFavourite(username, body);
   
        return ResponseEntity.ok(
            httpResponseHelper.jsonifyString(
                "is delete successful?", String.valueOf(favouriteAdded)));
    }

    @PatchMapping(path="/firebasetoken")
    public ResponseEntity<String> updateFirebaseToken(
        @RequestBody String body
    ) {
        boolean isTokenUpdated = userService.updateToken(body);
        return ResponseEntity.ok(
            httpResponseHelper.jsonifyString(
                "is update successful?", String.valueOf(isTokenUpdated)));
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
            httpResponseHelper.jsonifyString(
                "is delete successful?", String.valueOf(isDeleted)));
    }

    @PostMapping(path="/notification")
    public ResponseEntity<String> addNotifcation(
        @RequestBody String reqBody, 
        @RequestHeader String username
        ) {
        Notification notification = Notification.populateFromClient(reqBody);
        notification.setFirebaseToken(userService.getFirebaseToken(username));
        NotificationTask notificationTask = new NotificationTask(notification);

        taskSchedulingService.scheduleATask(notification.getTaskId(), notificationTask, notification.getCronExpression());

        boolean isAdded = userService.addNotifcation(notification);
        return ResponseEntity.ok(
            httpResponseHelper.jsonifyString(
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

    @DeleteMapping(path="/notification/{busStopCode}")
    public ResponseEntity<String> deleteNotification(
        @RequestHeader String username,
        @PathVariable String busStopCode,
        @RequestHeader String cronString
    ) {
        Notification notification = new Notification();
        notification.setUsername(username);
        notification.setBusStopCode(busStopCode);
        notification.setCronExpression(cronString);
        String idToBeDeleted = userService.getNotificationId(notification);
        notification.setTaskId(idToBeDeleted);

        taskSchedulingService.removeScheduledTask(notification.getTaskId());
        boolean isDeleted = userService.deleteNotification(notification);
       
        return ResponseEntity.ok(
            httpResponseHelper.jsonifyString(
                "is delete successful?", String.valueOf(isDeleted)));
    }

    @GetMapping(path="/updateBusStopDatabase")
    public ResponseEntity<String> updateBusStopDatabase() {
        List<BusStop> busStops = busHelper.getAllBusStops();
        boolean isUpdated = busStopService.updateBusStops(busStops);
        return ResponseEntity.ok(
            httpResponseHelper.jsonifyString(
                "result", String.valueOf(isUpdated)));
    }
}
