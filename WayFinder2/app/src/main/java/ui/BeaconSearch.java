package ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.nfc.Tag;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.pervasivesystems.compasstest.R;

import java.util.List;
import java.util.UUID;

import request.blt.permission.BluetoothPermission;
import service.BeaconService;

public class BeaconSearch extends AppCompatActivity{

    BluetoothPermission bp;
    private final String TAG_DEBUG_APP = "BLIND_";
    private String TAG_DEBUG = "BEACONSEARCH";
    private BeaconManager beaconManager;
    private static final String UUID_String = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private Region  region =  new Region("monitored region",UUID.fromString(UUID_String), null, null);
    private AppCompatActivity appCompatActivity;
    private boolean need_service = true;

    // Animation stuff
    RotateAnimation animFadein;
    ImageView rangeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG_DEBUG_APP+TAG_DEBUG,"onCreate()");
        setContentView(R.layout.activity_beacon_search);

        ImageView logo = (ImageView)findViewById(R.id.imageView2);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this.getApplicationContext(),
                R.anim.rotate);
        logo.startAnimation(rotateAnimation);
        Log.d(TAG_DEBUG_APP+TAG_DEBUG, "animation started");

        bp = new BluetoothPermission(BeaconSearch.this);
        appCompatActivity = this;

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG_DEBUG_APP+TAG_DEBUG,"onResume()");
        bp.onResumeEnableBluetoothPermission();
        //stoppo il servizio
        stopService(new Intent(getBaseContext(), BeaconService.class));
        initializedBeaconManager();
        Toast.makeText(appCompatActivity, "We are locating you position ", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG_DEBUG_APP+TAG_DEBUG,"onPause()");
        //faccio partire il servizio, se ho già ottenuto i permessi, facciamo un check:
        if (bp.hasPermission() == PackageManager.PERMISSION_GRANTED) {
            //se il servizio è attivo bisogna stoppare il monitoring
            beaconManager.stopMonitoring(region);
            beaconManager.disconnect();
            //se vado in onPause perchè ho trovato un beacon non serve che faccio partire il servizio
            if(need_service)
                startService(new Intent(getBaseContext(), BeaconService.class));
        }
    }
    private void initializedBeaconManager() {
        if (bp.hasPermission() == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG_DEBUG_APP+TAG_DEBUG, "initializedBeaconManager() ");
            //attivo il monitoring
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
                    Log.d(TAG_DEBUG_APP+TAG_DEBUG, "onEnteredRegion"+region.getProximityUUID());
                    // Toast.makeText(context, "onEnteredRegion", Toast.LENGTH_SHORT).show();

                    //TODO chiama navigaziotion, passanto la region con cui sei entrato. Stringa UUID
                    Intent Navigation = new Intent(appCompatActivity, Navigation.class);
                    Navigation.putExtra("Region", region.getProximityUUID()+"");
                    //Toast.makeText(appCompatActivity, "Position found", Toast.LENGTH_LONG).show();
                    need_service = false;
                    appCompatActivity.startActivity(Navigation);
                }

                @Override
                public void onExitedRegion(Region region) {
                    // could add an "exit" notification too if you want (-:
                    Log.d(TAG_DEBUG_APP+TAG_DEBUG, "onExitedRegion");

                    //TODO mmm, non so, forse neanche ci serve
                }
            });
        }else{
            Log.d(TAG_DEBUG_APP+TAG_DEBUG,"NON ABBIAMO I PERMESSI PER IL BLT, DO ANNAMO?");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        bp.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bp.onActivityResult(requestCode, resultCode, data);
    }

    /* AnimationListener methods
    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }
    */
    //TODO Logica del monitoring
    //TODO Chiamata/chiusura del servizio in background

    /**
     * need_service serve perchè se vado in onPuase perchè ho trovato un beacon e chiamo Navigation
     *              non serve far partire il servizio
     *
     *
     */

}
