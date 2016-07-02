package utils;

/**
 * Created by roberto on 6/30/16.
 *
 * As the name suggests, this class contains some constants used across the app.
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
    public static final String TOO_LEFT_DIRECTION  = "Turn right to reach ";
    public static final String TOO_RIGHT_DIRECTION = "Turn left to reach ";
    public static final String OPPOSITE_DIRECTION  = "You are walking in the opposite direction.";

    // BeaconService notification texts
    public static final String NOTIFICATION_ENTER_MESSAGE = "You entered into region: ";
    public static final String NOTIFICATION_EXIT_MESSAGE  = "You left the region: ";

    public static final String UUID_String = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";

    // Navigation GUI texts
    public static final String NAVIGATION_BUTTON_NOTHING    = "In this direction you cannot reach anything.";
    public static final String NAVIGATION_BUTTON_SOMETHING  = "In this direction you can reach ";
    public static final String NAVIGATION_BUTTON_IN_NAVIG_1 = "You are walking towards ";
    public static final String NAVIGATION_BUTTON_IN_NAVIG_2 = ". Click here to stop the navigation.";
    public static final String NAVIGATION_BUTTON_WAITING    = "Wait, we are looking for another beacon..";
    public static final String NAVIGATION_TOAST_STARTED  = "Navigation started";
    public static final String NAVIGATION_TEXT_NO_BEACON = "No beacon detected";
    public static final String NAVIGATION_TEXT_POSITION  = "You are in ";
    public static final String NAVIGATION_TEXT_STEPS_1   = "There are ";
    public static final String NAVIGATION_TEXT_STEPS_2   = " steps";
    public static final String NAVIGATION_TEXT_NO_STEPS  = "";

}
