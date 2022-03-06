package bus.server.models;

public class BusService {
    String serviceNumber;
    Bus nextBus;
    Bus nextBus2;
    Bus nextBus3;


    public String getServiceNumber() {
        return this.serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public Bus getNextBus() {
        return this.nextBus;
    }

    public void setNextBus(Bus nextBus) {
        this.nextBus = nextBus;
    }

    public Bus getNextBus2() {
        return this.nextBus2;
    }

    public void setNextBus2(Bus nextBus2) {
        this.nextBus2 = nextBus2;
    }

    public Bus getNextBus3() {
        return this.nextBus3;
    }

    public void setNextBus3(Bus nextBus3) {
        this.nextBus3 = nextBus3;
    }


    
}
