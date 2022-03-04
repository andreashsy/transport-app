package bus.server.models;

import org.springframework.beans.factory.annotation.Configurable;

import bus.server.utilities.BusHelper;
import bus.server.utilities.NotificationSender;

@Configurable
public class NotificationTask implements Runnable {
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
        notificationSender.sendNotification();
    }

}


