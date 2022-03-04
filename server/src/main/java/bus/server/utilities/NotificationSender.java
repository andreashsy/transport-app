package bus.server.utilities;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.logging.Level;
import java.util.logging.Logger;

import bus.server.models.Notification;

import static bus.server.Constants.*;

public class NotificationSender {
    private final Logger logger = Logger.getLogger(NotificationSender.class.getName());
    private String headerKey;
    private Notification notification;

    public NotificationSender(Notification notification) {
        this.notification = notification;
        if ((KEY_FIREBASE!= null) && (KEY_FIREBASE.trim().length() > 0)) {
            this.headerKey = KEY_FIREBASE;
            logger.log(Level.INFO, ">>> FIREBASE KEY SET!");
        } else {
            this.headerKey = "API KEY NOT FOUND";
            logger.log(Level.WARNING, ">>> ERROR!! FIREBASE KEY NOT FOUND!");
        }
    }
    
    public String sendNotification() {
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
