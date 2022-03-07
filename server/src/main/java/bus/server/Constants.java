package bus.server;

public class Constants {
    public static final String KEY_LTA = System.getenv("LTA_DATAMALL_API");
    public static final String KEY_FIREBASE = System.getenv("FIREBASE_SERVER_KEY");
    public static final String KEY_JWT_SIGN = System.getenv("JWT_SIGNKEY");
    public static final String KEY_DO_ACCESS = System.getenv("DO_ACCESS_KEY");
    public static final String KEY_DO_PRIVATE = System.getenv("DO_SECRET_KEY");
    public static final String TOKEN_TELEGRAM_BOT = System.getenv("TELEGRAM_BOT_TOKEN");
    public static final String URL_BUS_STOP_BY_ID = "http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2";
    public static final String URL_ALL_BUS_STOPS = "http://datamall2.mytransport.sg/ltaodataservice/BusStops";
    public static final String URL_FIREBASE = "https://fcm.googleapis.com/fcm/send";
    public static final String APP_VERSION = "v1.05";
}