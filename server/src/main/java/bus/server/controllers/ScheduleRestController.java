package bus.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.server.services.NotificationService;
import bus.server.services.TaskSchedulingService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleRestController {

    @Autowired
    NotificationService notificationService;

    @Autowired
    TaskSchedulingService taskSchedulingService;
    
    @PostMapping(path="/schedule")
    public ResponseEntity<String> setSchedule() {
        taskSchedulingService.scheduleATask("12345678", notificationService, "* * * * * *");

        JsonObject jo = Json.createObjectBuilder()
            .add("message", "schedule set!")
            .build();
        return ResponseEntity.ok(jo.toString());
    }
}
