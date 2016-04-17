package com.pervasivesystems.compasstest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    // define the display assembly compass picture
    private ImageView image;
    private TextView tvHeading;
    private CompassOld cn;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        cn.registerListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cn.unregisterListener();
    }


}