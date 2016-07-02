package request.blt.permission;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import utils.AppConstants;

/**
 * Created by Andrea on 19/04/2016.
 *
 * This class is used to check and request bluetooth permission after the app opening.
 */
public class BluetoothPermission {
    final private String TAG_DEBUG ="BLT_PERMISSION";
    final private int REQUEST_ENABLE_BT = 125;
   private AppCompatActivity activity;

    public BluetoothPermission(AppCompatActivity a){
      activity = a;
    }

    //this function it must put in onResume method, it
    //checks if the app has the BLT's permission, and in the end
    //checks if the BLT is open with the call enableBLT()
    public void onResumeEnableBluetoothPermission(){
        int hasWriteContactsPermission = hasPermission();
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            //shouldShowRequestPermissionRationale() = If this function is called on pre-M, it will always return false.
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                //If this function is called on pre-M, OnRequestPermissionsResultCallback
                // will be suddenly called with correct PERMISSION_GRANTED or PERMISSION_DENIED result.

                /*ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_ENABLE_BT);*/
                enableBLT();
                Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG," Build.VERSION.SDK_INT < Build.VERSION_CODES.M");

            }else{
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    showMessageOKCancel("You need to allow access for BLT scanning on Android 6.0 and above.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activity,
                                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                            REQUEST_ENABLE_BT);
                                }

                            });
                    Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");
                }
            }
        }else {
            enableBLT();
        }

    }

    //return from  enableBluetoothPermission()
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,"PERMESSO DATO per i permessi!!!");
                } else {
                    // Permission Denied
                    Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "PERMESSO NEGATO per i permessi!!!");
                    //Toast.makeText(activity, "PERMISSION_GRANTED Denied", Toast.LENGTH_SHORT).show();
                    this.showMessageOK("Permissions denied: the app will be closed.",
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.finish();
                                }
                            }
                    );
                }
                break;
            default:
                activity.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    private void enableBLT(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else{

        }
    }
    //return from  startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT) in enableBLT()
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == activity.RESULT_OK) {
                // The user open the bluethoot.
                // The Intent's data Uri identifies which contact was selected.
                Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,"L utente ha dato il permesso per ACCENDERE il BLT");

                // Do something with the contact here (bigger example below)
            }else{ //if(requestCode == RESULT_CANCELED){
                Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,"L utente non ha dato il permesso");
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                //.setView()
                .setMessage(message)
               .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void showMessageOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                //.setView()
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    public int hasPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                return PackageManager.PERMISSION_DENIED;
            }else {
                return PackageManager.PERMISSION_GRANTED;
            }
        }else {
            return ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }
}
