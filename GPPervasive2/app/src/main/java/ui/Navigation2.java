package ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.pervasivesystems.compasstest.R;

import request.blt.permission.BluetoothPermission;
import service.BeaconService;

public class Navigation2 extends AppCompatActivity {
    private final String TAG_DEBUG="Navigation2";
    private final String TAG_DEBUG_APP = "BLIND_";
    BluetoothPermission bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation2);
        String UUID = getIntent().getExtras().getString("Region");
        if(UUID != null){
            Log.d(TAG_DEBUG_APP+TAG_DEBUG,"UUID passato: "+UUID);
            TextView tv = (TextView)findViewById(R.id.editText);
            tv.setText(UUID);

        }else{
            Log.d(TAG_DEBUG_APP+TAG_DEBUG,"Errore, parametro non passato");
        }
        bp = new BluetoothPermission(Navigation2.this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG_DEBUG,"onResume()");
        bp.onResumeEnableBluetoothPermission();
        //stoppo il servizio
        stopService(new Intent(getBaseContext(), BeaconService.class));
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG_DEBUG_APP+TAG_DEBUG,"onPause()");
        //faccio partire il servizio, se ho già ottenuto i permessi, facciamo un check:
        if (bp.hasPermission() == PackageManager.PERMISSION_GRANTED) {
            //se vado in onPause perchè ho trovato un beacon non serve che faccio partire il servizio
            startService(new Intent(getBaseContext(), BeaconService.class));
        }
    }
}

