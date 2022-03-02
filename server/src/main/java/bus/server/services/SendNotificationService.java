package bus.server.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

import bus.server.models.Notification;

import static bus.server.Constants.*;

@Service
public class SendNotificationService {
    private final Logger logger = Logger.getLogger(SendNotificationService.class.getName());
    private String headerKey;

    public SendNotificationService() {
        if ((KEY_FIREBASE!= null) && (KEY_FIREBASE.trim().length() > 0)) {
            this.headerKey = KEY_FIREBASE;
            logger.log(Level.INFO, ">>> FIREBASE KEY SET!");
        } else {
            this.headerKey = "API KEY NOT FOUND";
            logger.log(Level.INFO, ">>> ERROR!! FIREBASE KEY NOT FOUND!");
        }
    }
    
    public String sendNotification(Notification notification) {
        String url = UriComponentsBuilder
        .fromUriString(URL_FIREBASE)
        .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "key=" + this.headerKey);

        RequestEntity<String> req = RequestEntity
            .post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .headers(headers)
            .body(notification.generateNotification().toString(), String.class);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);

        return resp.getBody();
    }
}
