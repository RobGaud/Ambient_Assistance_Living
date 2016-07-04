package service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;

import ui.BeaconSearch;
import ui.Navigation;
import utils.AppConstants;

/**
 * Created by Andrea on 6/30/16.
 *
 * BeaconService implements the background service used to check whether the user entered into a
 * building or not. Basically, it periodically checks whether there is a beacon with the WayFinder
 * tag, and if a beacon is detected, the user receives a notification.
 * In particular if clicked, the Navigation activity is open, so that the user can immediately
 * start using WayFinder.
 */
public class BeaconService extends Service {
    private final String TAG_DEBUG = "BEACONSERVICE";

    private BeaconManager beaconManager;
    private Context context;
    private  Service service;

    /* Variable numInstances is used to keep track of the number of active instances of the service
     * (clearly we want only one).
     */
    public static int numInstances = 0;

    // Variable n is used to limit the number of notifications
    private Notification notificationEnter = null;
    private Notification notificationExit  = null;

    private int notEnter_ID = 3;
    private int notExit_ID  = 4;
    public Region region =  new Region("monitored region",UUID.fromString(AppConstants.UUID_String), null, null);

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "onCreate() Service Started");

        if(numInstances == 0){
            initializedBeaconManager();
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "created==0, we have called initializedBeaconManager()");
            numInstances++;
        }else{
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "created!=0");
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "onStartCommand() Service Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationEnter = null;
        notificationExit  = null;

        numInstances = 0;
        // Removing the notification
        utils.Utils.deleteNotification(notEnter_ID, service);
        utils.Utils.deleteNotification(notExit_ID,  service);

        Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "onDestroy()-Service Destroyed");
        beaconManager.stopMonitoring(region);
        beaconManager.disconnect();
    }

    private void initializedBeaconManager(){
        Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "initializedBeaconManager() ");

        service = this;
        context = getApplicationContext();
        beaconManager = new BeaconManager(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(region);
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "onEnteredRegion");
                if(notificationEnter == null){
                    Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "n == null");

                    // Delete any previous notification about region entering, and create the new one.
                    utils.Utils.deleteNotification(notExit_ID ,service);
                    notificationEnter = utils.Utils.Notification(notEnter_ID ,service, Navigation.class,
                            "Beacon Service", AppConstants.NOTIFICATION_ENTER_MESSAGE
                                    +AppConstants.UUID_String, AppConstants.UUID_String);
                }else{
                    Log.d(TAG_DEBUG, "n != null,onEnteredRegion(), c'è già la notifica");
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "onExitedRegion");

                if(notificationExit == null) {
                    Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "n1 == null");

                    // Delete any previous notification about region exiting, and create the new one.
                    utils.Utils.deleteNotification(notEnter_ID ,service);
                    notificationExit = utils.Utils.Notification(notExit_ID , service, BeaconSearch.class,
                            "Beacon Service", AppConstants.NOTIFICATION_EXIT_MESSAGE +
                                    AppConstants.UUID_String, AppConstants.UUID_String);
                }else{
                    Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                            "n1 != null,onExitedRegion(), c'è già la notifica");
                }
            }
        });
    }
}

/**
 *	Osservazioni importanti:
 *		1)	La variabile Notification n mi serve per non spare mille notifiche, perchè ho notato che
 *        	onEnterRegion qualche volta viene chiamata a buffo e quindi ripresentava la stessa notifica
 *
 *		2)	La variabile created tiene conto di quante instance del service sono state create, questo perchè
 *   		non vogliamo avere più di una attiva al momento. E non vogliamo che ogni volta faccia ripartire
 *   		il beaconManager. Più che altro non vogliamo instanziare il BeaconManager ogni volta che si fa
 *   		startService
 *
 *		3)	Quando esci dalla regione devi togliere la notifica perchè senno se si clicca si manda a navigation,
 *  		ma è incoerente->sono uscito dalla regione!
 */
