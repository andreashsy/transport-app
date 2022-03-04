package bus.server;

public class Constants {
    public static final String KEY_LTA = System.getenv("LTA_DATAMALL_API");
    public static final String KEY_FIREBASE = System.getenv("FIREBASE_SERVER_KEY");
    public static final String URL_BUS_STOP_BY_ID = "http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2";
    public static final String URL_ALL_BUS_STOPS = "http://datamall2.mytransport.sg/ltaodataservice/BusStops";
    public static final String URL_FIREBASE = "https://fcm.googleapis.com/fcm/send";
    public static final String DEMO_CLIENT_TOKEN = "eDcDqksJwSAaGioVB-h8hZ:APA91bEg7OJMF7CTmWJr1tQQ2c0Xchld9fb0HoMmTitw_1WRHrWSn2BdE7djO6c76CX3lqwaUHay2dKQ9SQs24vt2rY5MKbDmGQs48DWE-SN9S6CHiJ1DZKZdfw5G2uxXDZoiB_Icidk";
    public static final String APP_VERSION = "v0.01";
    public static final String TOKEN_TELEGRAM_BOT = System.getenv("TELEGRAM_BOT_TOKEN");
}