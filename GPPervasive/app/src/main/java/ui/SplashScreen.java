package ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pervasivesystems.compasstest.R;

import request.blt.permission.BluethootPermission;
import service.BeaconService;

public class SplashScreen extends AppCompatActivity {
    BluethootPermission bp;
    private String TAG_DEBUG="SplashScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screem);
        //startService(new Intent(getBaseContext(), BeaconService.class));
        bp = new BluethootPermission(SplashScreen.this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        bp.onResumeEnableBluetoothPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        bp.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        bp.onActivityResult(requestCode,resultCode,data);
    }
}
