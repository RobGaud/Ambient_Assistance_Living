package ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pervasivesystems.compasstest.R;

import service.BeaconService;

public class Navigation2 extends AppCompatActivity {
    private final String TAG_DEBUG="Navigation2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation2);
        String UUID = getIntent().getExtras().getString("Region");
        if(UUID != null){
            Log.d(TAG_DEBUG,"UUID passato: "+UUID);
        }else{
            Log.d(TAG_DEBUG,"Errore, parametro non passato");
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG_DEBUG,"onResume()");
        //stoppo il servizio
        stopService(new Intent(getBaseContext(), BeaconService.class));
    }
}

