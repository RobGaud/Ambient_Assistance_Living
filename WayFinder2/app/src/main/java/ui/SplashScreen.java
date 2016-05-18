package ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pervasivesystems.compasstest.R;

import request.blt.permission.BluetoothPermission;
import service.BeaconService;

public class SplashScreen extends AppCompatActivity {
    BluetoothPermission bp;
    private String TAG_DEBUG="SplashScreen";
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This line is commented because we want to use a style for this splashscreen
        // setContentView(R.layout.activity_splash_screem);
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
}
