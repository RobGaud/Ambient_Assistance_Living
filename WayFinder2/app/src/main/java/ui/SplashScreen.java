package ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.estimote.sdk.Region;
import com.pervasivesystems.compasstest.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import graph.Edge;
import graph.Node;
import map.persistence.DBHelper;
import request.blt.permission.BluetoothPermission;
import utils.ConnectionDetector;
import map.request.Request;

public class SplashScreen extends AppCompatActivity {
    private static String TAG_DEBUG_APP="BLIND_";
    private static String TAG_DEBUG="SplashScreen";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    final Context context = this;

    private static final String URL_GET_MAPS = "http://beaconcontrolflow.altervista.org/WayFinder/getAllMaps.php";

    BluetoothPermission bp;
    private ConnectionDetector cd;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This line is commented because we want to use a style for this splashscreen
        // setContentView(R.layout.activity_splash_screen);
        //TODO remove while cleaning up
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("dbVersion", 0);
        editor.apply();

        cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (cd.isConnectingToInternet()) {
            dbHelper = new DBHelper(getApplicationContext());

            // Call the database to check dbversion and update it if needed.
            Log.d(TAG_DEBUG_APP + TAG_DEBUG, "Preparing the request.");
            /*SharedPreferences*/ prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            int dbVersion = prefs.getInt("dbVersion", 0);
            Request request = new Request(URL_GET_MAPS, "dbVersion", ""+dbVersion, dbHelper, this);
            request.getRequest();
            Log.d(TAG_DEBUG_APP + TAG_DEBUG, "Request prepared.");

        }else{
            // Internet Connection is not present
            this.setNoConnectionDialog();
            // stop executing code by return
            return;
        }

        setDialog();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        bp.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bp.onActivityResult(requestCode, resultCode, data);
    }
    */
    public void setDialog(){
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("Informations");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Welcome to WayFinder.\n\n" +
                "Before starting, please ensure to have Google Talkback service active.\n"
                +"\nNow, you need to recalibrate the compass inside your phone: " +
                "to do this, make a complete circle with it. Press \"Ok\" " +
                "when you've done.\n");

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BeaconSearch.class);
                dialog.dismiss();
                startActivity(intent);
                finish();
            }
        });

        dialog.show();
    }

    public void setNoConnectionDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("No Connection detected");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Welcome to WayFinder.\n\n" +
                "Before starting, please ensure to have Internect Connectivity.\n"
                +"When you'll press \"OK\", the app will be closed." +
                "Turn on WiFi or Mobile connection and open the app again.\n");

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)context).finish();
            }
        });

    }

    public void updateVersion(int newDbVersion){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("dbVersion", newDbVersion);
        editor.apply();

        //Debug stuff let's see the map returned by DBHelper
        dbHelper = new DBHelper(getApplicationContext());
        HashMap<Region, Node> map = dbHelper.getMap(62887);
        Log.d(TAG_DEBUG_APP+TAG_DEBUG, "map returned, starting printing it.");
        Set<Region> s = map.keySet();
        Collection<Node> c = map.values();
        Iterator<Region> it1 = s.iterator();
        Iterator<Node> it2 = c.iterator();
        while(it1.hasNext()){
            Region r = it1.next();
            Node   n = it2.next();
            List<Edge> l = n.getEdges();
            Iterator<Edge> it3 = l.iterator();
            Log.d(TAG_DEBUG_APP+TAG_DEBUG, "region  = "+r.getMajor()+", "+r.getMinor()+".");
            Log.d(TAG_DEBUG_APP+TAG_DEBUG, "node    = "+n.getAudio()+", "+n.getSteps()+", "+n.getCategory()+".");
            while(it3.hasNext()){
                Edge e = it3.next();
                Log.d(TAG_DEBUG_APP+TAG_DEBUG, "   edge = "+e.getNodeFrom()+", "+e.getNodeTo()
                        +", "+e.getDirection()+", "+e.getDistance());
            }

        }
        Log.d(TAG_DEBUG_APP+TAG_DEBUG, "map print ended.");
    }
}
