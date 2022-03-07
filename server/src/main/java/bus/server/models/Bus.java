package bus.server.models;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Bus {
    LocalDateTime estimatedArrival;
    String arrivalTime;
    String Load;

    public void calculateArrivalTime() {
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), this.estimatedArrival);
        if (minutes >= 1) {
            this.setArrivalTime(String.valueOf(minutes) + " min");
        } else {
            this.setArrivalTime("Arriving");
        }
        
    }

    public LocalDateTime getEstimatedArrival() {
        return this.estimatedArrival;
    }

    public void setEstimatedArrival(LocalDateTime estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }

    public String getLoad() {
        return this.Load;
    }

    public void setLoad(String Load) {
        this.Load = Load;
    }

    public String getArrivalTime() {
        return this.arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
}
