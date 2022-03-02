package bus.server.services;

import org.springframework.stereotype.Service;

@Service
public class NotificationService implements Runnable {
    @Override
    public void run() {
        System.out.println("Sending notification...");
    }
}
