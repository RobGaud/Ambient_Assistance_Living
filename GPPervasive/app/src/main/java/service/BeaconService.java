package service;

/**
 * Created by Andrea on 18/04/2016.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;
import java.util.UUID;


public class BeaconService extends Service {
    private BeaconManager beaconManager;
    private final String TAG_DEBUG = "BEACONSERVICE";
    private Context context;
    private static int created=0;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_DEBUG, "onCreate() Service Started");
        Toast.makeText(this, "onCreate() Service Started", Toast.LENGTH_LONG).show();
        if(created == 0){
            initializedBeaconManager();
            created++;
        }else{
            Log.d(TAG_DEBUG, "created!=0");
            Toast.makeText(this, "created!=0", Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand() Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG_DEBUG, "onStartCommand() Service Started");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG_DEBUG, "onDestroy() Service Destroyed");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private void initializedBeaconManager(){
        Log.d(TAG_DEBUG, "initializedBeaconManager() ");
        Toast.makeText(this, "initializedBeaconManager()", Toast.LENGTH_SHORT).show();

        context = getApplicationContext();
        beaconManager = new BeaconManager(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region("monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 61272, 53723));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d(TAG_DEBUG, "onEnteredRegion");
                Toast.makeText(context, "onEnteredRegion", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onExitedRegion(Region region) {
                // could add an "exit" notification too if you want (-:
                Log.d(TAG_DEBUG, "onExitedRegion");
                Toast.makeText(context, "onExitedRegion", Toast.LENGTH_LONG).show();


            }
        });
    }
}