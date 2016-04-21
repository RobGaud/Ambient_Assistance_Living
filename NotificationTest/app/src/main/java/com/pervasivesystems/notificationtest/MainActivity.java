package com.pervasivesystems.notificationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void notification(View v){
       id = Utils.Notification(MainActivity.this);
    }
    public void deleteNot(View v){
        Utils.deleteNotification(id,MainActivity.this);
    }
}
