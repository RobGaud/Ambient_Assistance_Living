package ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pervasivesystems.compasstest.R;

import request.blt.permission.BluetoothPermission;
import service.BeaconService;

public class BeaconSearch extends AppCompatActivity {

    BluetoothPermission bp;
    private String TAG_DEBUG="BeaconSearch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_search);

        bp = new BluetoothPermission(BeaconSearch.this);
        stopService(new Intent(getBaseContext(), BeaconService.class));
    }

    @Override
    protected void onResume(){
        super.onResume();
        bp.onResumeEnableBluetoothPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        bp.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bp.onActivityResult(requestCode, resultCode, data);
    }

    //TODO Logica del monitoring
    //TODO Chiamata/chiusura del servizio in background

}
