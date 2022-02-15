package bus.server.models;

public class BusStop {
    private String busStopCode;
    private String roadName;
    private String description;
    private String latitude;
    private String longitude;

    public String getBusStopCode() {return this.busStopCode;}
    public void setBusStopCode(String BusStopCode) {this.busStopCode = BusStopCode;}

    public String getRoadName() {return this.roadName;}
    public void setRoadName(String roadName) {this.roadName = roadName;}    

    public String getDescription() {return this.description;}
    public void setDescription(String description) {this.description = description;} 

    public String getLatitude() {return this.latitude;}
    public void setLatitude(String latitude) {this.latitude = latitude;}

    public String getLongitude() {return this.longitude;}
    public void setLongitude(String longitude) {this.longitude = longitude;}

}
