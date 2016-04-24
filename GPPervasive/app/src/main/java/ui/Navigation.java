package ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.pervasivesystems.compasstest.R;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;

import compass.CompassOld;
import graph.Edge;
import graph.GraphMap;
import graph.Node;
import service.BeaconService;

public class Navigation extends AppCompatActivity {

    private static final String TAG_DEBUG = "Navigation";
    private  BeaconManager beaconManager;

    // Graphical stuff
    private ImageView compassImage;
    private Button navigationButton;
    private TextView positionText;

    // Navigation stuff
    private Node currentNode;
    private Node currentDirection;
    private boolean onNavigation;
    private static LinkedHashMap<Region, Node> DIAGList;
    private static GraphMap graph;

    private CompassOld compass = new CompassOld(this.getApplicationContext(),
                                                this, compassImage, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        onNavigation = false;
        currentNode = null;     // Forse ce lo facciamo passare dalla precedente Activity?
        currentDirection = null;

        String UUID = getIntent().getExtras().getString("Region");
        if(UUID != null){
            Log.d(TAG_DEBUG,"UUID passato: "+UUID);
        }else{
            Log.d(TAG_DEBUG,"Errore, parametro non passato");
        }

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
                    currentNode = graph.getNodeFromBeacon(detectedBeacon);
                    if(currentNode==null) {
                        Log.d(TAG_DEBUG, "ERROR: DETECTED BEACON IS NOT ASSOCIATED TO ANY NODE");
                    }
                    /*  TODO
                        Interagisci col Compass per sapere se di fronte ha qualcosa
                    */
                    compass.setCurrentNode(currentNode);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //stoppo il servizio
        stopService(new Intent(getBaseContext(), BeaconService.class));
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



    public void changeStatus(Node possibleDirection){
        //TODO
    }
}
