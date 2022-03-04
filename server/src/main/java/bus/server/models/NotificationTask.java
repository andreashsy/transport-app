package bus.server.models;

import bus.server.utilities.BusHelper;
import bus.server.utilities.NotificationSender;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NotificationTask implements Runnable {
    private static final Logger logger = Logger.getLogger(NotificationTask.class.getName());
    Notification notification;
    BusHelper busHelper = new BusHelper();

    public NotificationTask(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void run() {
        notification.setMessageTitle("Bus Stop data for id: " + notification.getBusStopCode());
        notification.setMessageBody(busHelper.getBusStopById(notification.getBusStopCode()));
        NotificationSender notificationSender = new NotificationSender(notification);
        String resp = notificationSender.sendNotification();
        logger.log(Level.INFO, "Notification sent, response from FCM server is: " + resp);
    }

}


