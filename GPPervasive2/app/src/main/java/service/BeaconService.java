package service;

/**
 * Created by Andrea on 18/04/2016.
 */
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

import java.util.List;
import java.util.UUID;

import ui.BeaconSearch;
import ui.Navigation2;


public class BeaconService extends Service {
    private BeaconManager beaconManager;
    private final String TAG_DEBUG = "BEACONSERVICE";
    private Context context;
    private  Service service;
    public static int created=0;
    private Notification n=null,n1=null;
    private int id_not=3,id_not1=4;
    public Region  region =  new Region("monitored region",UUID.fromString(UUID_String), null, null);
    private static final String UUID_String = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_DEBUG, "onCreate() Service Started");
        //Toast.makeText(this, "onCreate() Service Started", Toast.LENGTH_SHORT).show();
        if(created == 0){
            initializedBeaconManager();
            //Toast.makeText(this, "created==0, we have called initializedBeaconManager()", Toast.LENGTH_SHORT).show();
            Log.d(TAG_DEBUG, "created==0, we have called initializedBeaconManager()");
            created++;
        }else{
            Log.d(TAG_DEBUG, "created!=0");
            //Toast.makeText(this, "created!=0", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Toast.makeText(this, "onStartCommand() Service Started", Toast.LENGTH_SHORT).show();
        Log.d(TAG_DEBUG, "onStartCommand() Service Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        n = null;
        n1 = null;
        created = 0;
        //tolgo la notifica di enter
        utils.Utils.deleteNotification(id_not,service);
        utils.Utils.deleteNotification(id_not1,service);

        Log.d(TAG_DEBUG, "onDestroy() Service Destroyed");
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        beaconManager.stopMonitoring(region);
        beaconManager.disconnect();
    }

    private void initializedBeaconManager(){
        Log.d(TAG_DEBUG, "initializedBeaconManager() ");
        //Toast.makeText(this, "initializedBeaconManager()", Toast.LENGTH_SHORT).show();
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
                Log.d(TAG_DEBUG, "onEnteredRegion");
               // Toast.makeText(context, "onEnteredRegion", Toast.LENGTH_SHORT).show();
                if(n == null){
                    Log.d(TAG_DEBUG, "n == null");
                    utils.Utils.deleteNotification(id_not1,service);
                    n = utils.Utils.Notification(id_not,service, Navigation2.class,
                            "Beacon Service", "sei dentro alla regione: "+UUID_String,UUID_String);
                }else{
                    Log.d(TAG_DEBUG, "n != null,onEnteredRegion(), c'è già la notifica");
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
                Log.d(TAG_DEBUG, "onExitedRegion");
                //Toast.makeText(context, "onExitedRegion", Toast.LENGTH_SHORT).show();
                if(n1 == null) {
                    Log.d(TAG_DEBUG, "n1 == null");
                    utils.Utils.deleteNotification(id_not,service);
                    n1 = utils.Utils.Notification(id_not1, service, BeaconSearch.class, "Beacon Service",
                            "sei uscito dalla regione: " + UUID_String,UUID_String);
                }else{
                    Log.d(TAG_DEBUG, "n1 != null,onExitedRegion(), c'è già la notifica");

                }

            }
        });
    }
}

/**
 Osservazioni importanti:
 1) La variabile Notification n mi serve per non spare mille notifiche, perchè ho notato che
    onEnterRegion qualche volta viene chiamata a buffo e quindi ripresentava la stessa notifica

 2) La variabile created tiene conto di quante instance del service sono state create, questo perchè
    non vogliamo avere più di una attiva al momento. E non vogliamo che ogni volta faccia ripartire
    il beaconManager. Più che altro non vogliamo instanziare il BeaconManager ogni volta
 **/