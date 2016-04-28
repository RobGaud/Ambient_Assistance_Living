package ui;

import android.content.Intent;
import android.content.pm.PackageManager;
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

import compass.CompassOld;
import graph.Edge;
import graph.GraphMap;
import graph.Node;
import request.blt.permission.BluetoothPermission;
import service.BeaconService;

public class Navigation extends AppCompatActivity {

    private static final String TAG_DEBUG = "BLIND_Navigation";
    private  BeaconManager beaconManager;
    private static String UUID;
    private static Region region;
    BluetoothPermission bp;

    // Graphical stuff
    private ImageView compassImage;
    private Button navigationButton;
    private TextView positionText,degreeText;

    // Navigation stuff
    private Node currentNode;
    private Node currentDirection;
    private boolean onNavigation;
    private static LinkedHashMap<Region, Node> DIAGList;
    private static GraphMap graph;

    private AppCompatActivity appCompatActivity;
    private CompassOld compass ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        compassImage = (ImageView)findViewById(R.id.compass_image);
        navigationButton = (Button)findViewById(R.id.navigation_button);
        positionText = (TextView) findViewById(R.id.position_info);
        degreeText = (TextView)findViewById(R.id.degree);

        compass = new CompassOld(this.getApplicationContext(),this, compassImage, degreeText);

        onNavigation = false;
        currentNode = null;     // Forse ce lo facciamo passare dalla precedente Activity?
        currentDirection = null;
        bp = new BluetoothPermission(this);
        beaconManager = new BeaconManager(this);

        appCompatActivity = this;

        UUID = getIntent().getExtras().getString("Region");
        if(UUID != null){
            Log.d(TAG_DEBUG,"UUID passato: "+UUID);
            region = new Region("DIAG department", java.util.UUID.fromString(UUID), null, null);
        }else{
            Log.d(TAG_DEBUG,"Errore, parametro non passato");
            //TODO porco zio esci! ESCI!!!
        }


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

        //stoppo il servizio
        stopService(new Intent(getBaseContext(), BeaconService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG_DEBUG,"onPause()");
        compass.unregisterListener();

        if(bp.hasPermission() == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG_DEBUG,"abbiamo i permessi");
            beaconManager.stopRanging(region);
            beaconManager.disconnect();

            startService(new Intent(getBaseContext(), BeaconService.class));
        }else{
            Log.d(TAG_DEBUG,"non abbiamo i permessi?!!");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        bp.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bp.onActivityResult(requestCode, resultCode, data);
    }

    /*** Spostato in CompassOld ***
     public void notifyDirection(Node currentNode, Navigation a){
        if( currentNode == null ){
            Log.d(TAG_DEBUG, "Ma come, un nodo vuoto?");
            return;
        }
        ListIterator<Edge> directions = currentNode.getEdges().listIterator();
        while( directions.hasNext() ){

            Edge e = directions.next();
            float direction = e.getDirection();
            float currentDegree = compass.getCurrentDegree();
            float range = compass.getRange();

            if(currentDegree >= direction-range && currentDegree <= direction+range){
                a.changeStatus();

            }
            Node possibleDestination = e.getNodeTo();

        }
    }
    */
    // Metodo usato per informare l'utente della nuova direzione
    public void changeStatus(final Edge possibleDirection){
        //TODO
        // Cambia testo bottone
        // Cambia listener del bottone
        if(possibleDirection==null) {
            navigationButton.setText("In this direction you cannot reach anything ");
            navigationButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO avvisare che non c'è nessuna direction
                            Toast.makeText(appCompatActivity,
                                    "Turn around to find new directions.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }else {
            setButtonStartNavigation(possibleDirection.getNodeTo().getAudio(), possibleDirection.getDirection());
        }
    }

    private void setButtonStartNavigation(final String directionName, final float directionDegrees){
        navigationButton.setText("In this direction you can reach " + directionName+".");
        navigationButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(appCompatActivity,
                                "Navigation started", Toast.LENGTH_LONG).show();
                        compass.unsetDirection();
                        compass.setDirection(directionDegrees);
                        setButtonStopNavigation(directionName);
                        // TODO modificare testo e listener per disattivare la navigazione
                    }
                }
        );
    }

    private void setButtonStopNavigation(String directionName){
        navigationButton.setText("You are walking towards "+directionName+". Click here to stop the navigation.");
        navigationButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(appCompatActivity,
                                "Navigation ended.", Toast.LENGTH_LONG).show();
                        compass.unsetDirection();
                    }
                }
        );
    }

    private void initializeRanging(){
        if(bp.hasPermission()== PackageManager.PERMISSION_GRANTED) {
            DIAGList = new LinkedHashMap();
            final GraphMap graph = new GraphMap(DIAGList);
            // In generale, dovremmo scaricare la mappa dal server;
            // Qui, sappiamo già di che edificio parliamo (il DIAG).
            graph.inizializateMaps();

            beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                @Override
                public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                    if (!list.isEmpty()) {
                        Beacon nearestBeacon = list.get(0);
                        Region detectedBeacon = new Region("Detected Beacon",
                                nearestBeacon.getProximityUUID(),
                                nearestBeacon.getMajor(),
                                nearestBeacon.getMinor());
                        Log.d(TAG_DEBUG," "+detectedBeacon.toString());
                        currentNode = graph.getNodeFromBeacon(detectedBeacon);
                        if(currentNode==null) {
                            Log.d(TAG_DEBUG, "ERROR: DETECTED BEACON IS NOT ASSOCIATED TO ANY NODE");
                            //TODO return?
                        }
                        /*  TODO
                            Interagisci col Compass per sapere se di fronte ha qualcosa
                        */
                        if( compass.getCurrentNode() != null && !currentNode.equals(compass.getCurrentNode())){
                            // Se stava camminando verso una direzione, e l'ha raggiunta,
                            // Dobbiamo fermare la navigazione.
                            compass.unsetDirection();
                        }
                        else
                            compass.setCurrentNode(currentNode);

                        //Update the GUI
                        if(currentNode!=null)
                         positionText.setText("You are in "+currentNode.getAudio());
                        else
                            positionText.setText("No beacon detected");

                        // TODO come faccio partire l'audio associato?
                    }
                }
            });
        }else{
            Log.d(TAG_DEBUG,"non abbiamo i  permessi");
        }
    }
}
