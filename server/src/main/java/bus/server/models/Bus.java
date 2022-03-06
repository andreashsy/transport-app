package bus.server.models;

import java.time.LocalDateTime;

public class Bus {
    LocalDateTime estimatedArrival;
    String Load;

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
    
}
