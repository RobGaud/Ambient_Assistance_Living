package ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import request.blt.permission.BluetoothPermission;
import service.BeaconService;

public class SplashScreen extends AppCompatActivity {
    BluetoothPermission bp;
    private String TAG_DEBUG="SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This line is commented because we want to use a style for this splashscreen
        // setContentView(R.layout.activity_splash_screem);

        //startService(new Intent(getBaseContext(), BeaconService.class));

        Intent intent = new Intent(this, BeaconSearch.class);
        startActivity(intent);
        finish();
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
}
