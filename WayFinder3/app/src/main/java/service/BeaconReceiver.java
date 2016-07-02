package service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Andrea on 18/04/2016.
 * This class is used to start the background service at phone's startup.
 */
public class BeaconReceiver extends BroadcastReceiver {
    public BeaconReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)){
            Intent serviceIntent = new Intent(context, BeaconService.class);
            context.startService(serviceIntent);
        }
    }
}
