package bus.server.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bus.server.services.UserService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path="/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {
    private final Logger logger = Logger.getLogger(UserRestController.class.getName());
    @Autowired
    UserService userService;

    //request body should be json -> {"username": "<testuser>", "password": "<testpassword>", "notificationToken": "<testToken>"}
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody String body) {
        logger.log(Level.INFO, "UserRestController hit, requestbody: " + body);
        Boolean userAdded = userService.addUser(body);
        JsonObject jo = Json.createObjectBuilder()
            .add("user registration success?", userAdded)
            .build();
        return ResponseEntity.ok(jo.toString());
    }
}
