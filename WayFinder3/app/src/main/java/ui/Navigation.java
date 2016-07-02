package ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.estimote.sdk.connection.internal.BluetoothService;
import com.pervasivesystems.compasstest.R;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import audio.ManageAudio;
import compass.CompassOld;
import graph.Edge;
import graph.GraphMap;
import graph.Node;
import map.persistence.DBHelper;
import request.blt.permission.BluetoothPermission;
import service.BeaconService;
import utils.AppConstants;

public class Navigation extends AppCompatActivity {

    private static final String TAG_DEBUG = "Navigation";

    private Resources res;

    private  BeaconManager beaconManager;
    private static String UUID;
    private static Region region;
    BluetoothPermission bp;
    final Context context = this;

    // Graphical stuff
    //private ImageView compassImage;
    private Button navigationButton;
    private TextView positionText, degreeText, stepsText;

    // Navigation stuff
    private Node currentNode;
    private String lastNode="";
    private Node currentDirection;
    private boolean onNavigation;
    private DBHelper dbHelper;
    private static LinkedHashMap<Region, Node> buildingMap;
    private static GraphMap graph;

    private AppCompatActivity appCompatActivity;
    private CompassOld compass ;
    private ManageAudio manageAudio = null;
    int[] tracks = new int[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation2);

        res = getResources();
        //compassImage = (ImageView)findViewById(R.id.compass_image);
        navigationButton = (Button)findViewById(R.id.navigation_button);
        positionText = (TextView) findViewById(R.id.position_info);
        stepsText = (TextView) findViewById(R.id.Steps);
        //degreeText = (TextView)findViewById(R.id.degree);

        compass = new CompassOld(this.getApplicationContext(),this);

        onNavigation = false;
        currentNode = null;     // Forse ce lo facciamo passare dalla precedente Activity?
        currentDirection = null;
        bp = new BluetoothPermission(this);
        beaconManager = new BeaconManager(this);

        dbHelper = new DBHelper(getApplicationContext());

        appCompatActivity = this;

        UUID = getIntent().getExtras().getString("Region");
        if(UUID != null){
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,"UUID passato: "+UUID);
            region = new Region("DIAG department", java.util.UUID.fromString(UUID), null, null);
        }else{
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,"Errore, parametro non passato");
        }

        tracks[0] = R.raw.tethys;
        manageAudio= new ManageAudio(tracks,getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();

        compass.registerListener();

        bp.onResumeEnableBluetoothPermission();
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                //START RANGING
                beaconManager.startRanging(region);
            }
        });

        initializeRanging();

        // Once WayFinder is on foreground, we don't need the service anymore. Hence, we can stop it.
        stopService(new Intent(getBaseContext(), BeaconService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,"onPause()");
        compass.unregisterListener();

        if(bp.hasPermission() == PackageManager.PERMISSION_GRANTED){
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,"abbiamo i permessi");
            beaconManager.stopRanging(region);
            beaconManager.disconnect();

            startService(new Intent(getBaseContext(), BeaconService.class));
        }else{
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,"non abbiamo i permessi?!!");
        }
        statusButton=false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        bp.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bp.onActivityResult(requestCode, resultCode, data);
    }

    // Metodo usato per informare l'utente della nuova direzione
    public void changeStatus(final Edge possibleDirection){
        if(possibleDirection == null) {
            navigationButton.setText(AppConstants.NAVIGATION_BUTTON_NOTHING);
            dir=" ";
            navigationButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(appCompatActivity,
                                    "Turn around to find new directions.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }else {
            setButtonStartNavigation(possibleDirection,possibleDirection.getNodeTo().getAudio(), possibleDirection.getDirection());
        }
    }

    private boolean statusButton = false;
    String dir="";
    //false=no navigation, true= navigation

    private void setButtonStartNavigation(final Edge e,final String directionName, final float directionDegrees){
        //Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "setButtonStartNavigation");
        navigationButton.setText(String.format(
                res.getString(R.string.navigation_button_something), directionName));
        if(!dir.equals(directionName)) {
            // If we found a new direction, we keep track of it, and we notify the user.
            manageAudio.play();
            dir = directionName;
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, directionName);
        }
        navigationButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!statusButton){//statusButton==false
                            Toast.makeText(appCompatActivity,
                                    AppConstants.NAVIGATION_TOAST_STARTED, Toast.LENGTH_LONG).show();
                            //compass.unsetDirection();
                            compass.setDirection(directionDegrees,e);
                            statusButton = true;
                            navigationButton.setText(String.format(
                                    res.getString(R.string.navigation_button_in_navig), directionName));

                        }else{
                            Toast.makeText(appCompatActivity,
                                    "Navigation ended.", Toast.LENGTH_LONG).show();
                            compass.unsetDirection();
                            statusButton = false;
                            navigationButton.setText(AppConstants.NAVIGATION_BUTTON_WAITING);
                        }
                    }
                }
        );
    }

    private void initializeRanging(){
        if(bp.hasPermission()== PackageManager.PERMISSION_GRANTED) {

            beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                @Override
                public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                    if (!list.isEmpty()) {
                        Beacon nearestBeacon = list.get(0);
                        Region detectedBeacon = new Region("Detected Beacon",
                                nearestBeacon.getProximityUUID(),
                                nearestBeacon.getMajor(),
                                nearestBeacon.getMinor());
                        Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, " "+detectedBeacon.toString());
                        // If we have not already initialized the map, we've to do it.
                        if(buildingMap == null){
                            buildingMap = dbHelper.getMap(nearestBeacon.getMajor());
                            graph = new GraphMap(buildingMap);
                        }
                        //refresh informations about the Navigationbutton's text
                        if(graph.getNodeFromBeacon(detectedBeacon)!=null) {
                            if(currentNode!=null) {
                                if (!currentNode.equals(graph.getNodeFromBeacon(detectedBeacon))) {
                                    //changeStatus(null);
                                    Log.d(AppConstants.TAG_DEBUG_APP + TAG_DEBUG, " if( compass.getCurrentNode() != null && !currentNode.equals(compass.getCurrentNode()))");
                                    navigationButton.setText("Waiting");
                                    navigationButton.setOnClickListener(
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Toast.makeText(appCompatActivity,
                                                            "Waiting and turn around to find new directions.",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                    );
                                }
                            }
                        }
                        currentNode = graph.getNodeFromBeacon(detectedBeacon);
                        if(currentNode==null) {
                            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "ERROR: DETECTED BEACON IS NOT ASSOCIATED TO ANY NODE");
                            //TODO return?
                        }
                        /*  TODO
                            Interagisci col Compass per sapere se di fronte ha qualcosa
                        */
                        if(currentNode!=null) {
                            if (compass.getCurrentNode() != null && !currentNode.equals(compass.getCurrentNode())) {
                                // Se stava camminando verso una direzione, e l'ha raggiunta,
                                // Dobbiamo fermare la navigazione.
                                statusButton=false;
                                compass.unsetDirection();
                            }

                            compass.setCurrentNode(currentNode);
                        }

                        //Update the GUI
                        if(currentNode!=null) {
                            //String text = "";
                            String s = "";
                            if(currentNode.getSteps()!=0) {
                                //text = "" + currentNode.getSteps();
                                //s = "\n and there are\n "+text+" steps";

                                // This is a parametric resource. This instruction replaces the commented one.
                                stepsText.setText(String.format(res.getString(R.string.navigation_steps_text),
                                                                currentNode.getSteps()));
                                //stepsText.setText(AppConstants.NAVIGATION_TEXT_STEPS_1 + text +
                                //                  AppConstants.NAVIGATION_TEXT_STEPS_2);
                            }else{
                                stepsText.setText(AppConstants.NAVIGATION_TEXT_NO_STEPS);
                            }

                            positionText.setText(String.format(res.getString(R.string.navigation_position_text),
                                                 currentNode.getAudio()));
                            //positionText.setText(AppConstants.NAVIGATION_TEXT_POSITION +
                            //                     currentNode.getAudio());
                            if(!lastNode.equals(currentNode.getAudio())) {
                                if(currentNode.getSteps()!=0)
                                    setDialog(currentNode.getAudio()+s);
                                else
                                    setDialog(currentNode.getAudio());
                                lastNode = currentNode.getAudio();
                            }
                        }else {
                            positionText.setText(AppConstants.NAVIGATION_TEXT_NO_BEACON);
                        }
                    }
                }
            });
        }else{
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "non abbiamo i permessi");
        }
    }

    @Override
    public void onBackPressed() {
        // your code. In our case, it's a don't care.
    }

    public void setDialog(String s){
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_position);
        dialog.setTitle("Informations");

        // set the custom dialog components - text, image and
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText(String.format(res.getString(R.string.navigation_dialog_text), s));
        //text.setText( "\n You are in "+s+" \n");


        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}


