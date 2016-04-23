package ui;

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

import compass.CompassOld;
import graph.GraphMap;
import graph.Node;

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

    private CompassOld compass = new CompassOld(this.getApplicationContext(), compassImage, null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        onNavigation = false;
        currentNode = null;     // Forse ce lo facciamo passare dalla precedente Activity?
        currentDirection = null;


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
                    //TODO
                }
            }
        });
    }
}
