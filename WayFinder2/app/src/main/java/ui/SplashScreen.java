package ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pervasivesystems.compasstest.R;

import map.persistence.DBHelper;
import request.blt.permission.BluetoothPermission;
import utils.AppConstants;
import utils.ConnectionDetector;
import map.request.Request;

/** The SplashScreen activity is displayed after the app opening. It checks internet connectivity and
 *  gives very basic information to properly use te app.
 */
public class SplashScreen extends AppCompatActivity {
    private static String TAG_DEBUG="SplashScreen";
    final Context context = this;



    BluetoothPermission bp;
    private ConnectionDetector cd;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (cd.isConnectingToInternet()) {
            dbHelper = new DBHelper(getApplicationContext());

            // Call the database to check dbversion and update it if needed.
            Log.d(AppConstants.TAG_DEBUG_APP + TAG_DEBUG, "Preparing the request.");

            SharedPreferences prefs = getSharedPreferences(AppConstants.MY_PREFS_NAME, MODE_PRIVATE);
            int dbVersion = prefs.getInt("dbVersion", 0);
            Request request = new Request(AppConstants.URL_GET_MAPS, "dbVersion", ""+dbVersion, dbHelper, this);
            request.getRequest();

            Log.d(AppConstants.TAG_DEBUG_APP + TAG_DEBUG, "Request prepared.");

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
        text.setText(AppConstants.SPLASH_MESSAGE);

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
        text.setText(AppConstants.NO_CONNECTION_MESSAGE);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AppCompatActivity)context).finish();
            }
        });

    }

    public void updateVersion(int newDbVersion){
        SharedPreferences prefs = getSharedPreferences(AppConstants.MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("dbVersion", newDbVersion);
        editor.apply();

        /*
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
        */
    }
}
