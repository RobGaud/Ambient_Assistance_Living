package utils;

/**
 * Created by roberto on 6/30/16.
 */
public class AppConstants {

    // This message is displayed into the SplashScreenActivity
    public static final String SPLASH_MESSAGE = "Welcome to WayFinder.\n\n" +
            "Before starting, please ensure to have Google Talkback service active.\n"
            +"\nNow, you need to recalibrate the compass inside your phone: " +
            "to do this, make a complete circle with it. Press \"Ok\" " +
            "when you've done.\n";

    // This message is displayed into the SplashScreenActivity, if no connection is detected
    public static final String NO_CONNECTION_MESSAGE = "Welcome to WayFinder.\n\n" +
            "Before starting, please ensure to have Internect Connectivity.\n"
            +"When you'll press \"OK\", the app will be closed." +
            "Turn on WiFi or Mobile connection and open the app again.\n";

    // This tag is used for logging purposes
    public static final String TAG_DEBUG_APP="BLIND_";

    // This string represents the name of the SharedPreferences object used into the app
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    // This URL is used to connect to the database
    public static final String URL_GET_MAPS = "http://beaconcontrolflow.altervista.org/WayFinder/getAllMaps.php";


    //Compass constants
    public static final String CORRECT_DIRECTION   = "Keep walking on this direction to reach ";

    public static final String TOO_LEFT_DIRECTION  =  "Turn right to reach ";

    public static final String TOO_RIGHT_DIRECTION =  "Turn left to reach ";

    public static final String OPPOSITE_DIRECTION = "You are walking in the opposite direction.";

}
