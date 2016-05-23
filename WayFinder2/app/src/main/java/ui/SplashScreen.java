package ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pervasivesystems.compasstest.R;

import map.persistence.DBHelper;
import request.blt.permission.BluetoothPermission;
import service.BeaconService;
import utils.ConnectionDetector;
import map.request.Request;

public class SplashScreen extends AppCompatActivity {
    private String TAG_DEBUG="SplashScreen";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    final Context context = this;

    private final String URL_GET_MAPS = "";

    BluetoothPermission bp;
    private ConnectionDetector cd;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This line is commented because we want to use a style for this splashscreen
        // setContentView(R.layout.activity_splash_screen);

        cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        if (cd.isConnectingToInternet()) {
            dbHelper = new DBHelper(getApplicationContext());

            //TODO call the database to check dbversion and update it if needed.
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            int dbVersion = prefs.getInt("dbVersion", 0);
            Request request = new Request(URL_GET_MAPS, "dbVersion", ""+dbVersion);
            request.getRequest();

            /*SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putInt("dbVersion", 1);
            editor.apply();

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String restoredText = prefs.getString("text", null);
            if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
            int idName = prefs.getInt("idName", 0); //0 is the default value.
            }
            */

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
}
