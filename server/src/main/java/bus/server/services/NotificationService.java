package bus.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bus.server.models.Notification;
import static bus.server.Constants.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Date;

@Service
public class NotificationService implements Runnable {
    private final Logger logger = Logger.getLogger(NotificationService.class.getName());

    @Autowired
    SendNotificationService sendNotificationService;

    @Override
    public void run() {
        logger.log(Level.INFO, "Sending notification...");
        Notification notification = new Notification();
        notification.setClientToken(DEMO_CLIENT_TOKEN);
        notification.setMessageBody("demo message body, time in ms: " + new Date().getTime());
        notification.setMessageTitle("demo message title");
        sendNotificationService.sendNotification(notification);
    }
}
