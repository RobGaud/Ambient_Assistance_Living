package ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.pervasivesystems.compasstest.R;

import compass.CompassOld;
import request.blt.permission.BluethootPermission;

public class MainActivity extends AppCompatActivity{

    // define the display assembly compass picture
    private ImageView image;
    private TextView tvHeading;
    private CompassOld cn;
    BluethootPermission bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView that will tell the user what degree is he heading
        tvHeading = (TextView) findViewById(R.id.tvHeading);
        cn = new CompassOld(this,image,tvHeading);
        cn.setDirection(0);

        bp = new BluethootPermission(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cn.registerListener();
        bp.onResumeEnableBluetoothPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cn.unregisterListener();
    }


}