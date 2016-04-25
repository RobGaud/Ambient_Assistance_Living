package service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
