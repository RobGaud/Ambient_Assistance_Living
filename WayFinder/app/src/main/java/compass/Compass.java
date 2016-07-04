package compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;

import java.util.ListIterator;

import graph.Edge;
import graph.Node;
import ui.Navigation;
import utils.AppConstants;

/**
 * Created by Andrea on 14/04/2016.
 *
 * The Compass class is used to check user's direction and to navigate him/her into the building.
 * It uses the magnetometer inside the phone to get the its orientation.
 */
public class Compass implements SensorEventListener  {

    private static final String TAG_DEBUG = "Compass";
    // device sensor manager
    private SensorManager mSensorManager;

    private Navigation activity;

    private boolean follow;
    private float destinationDirection;
    private Node currentNode;

    // record the compass picture angle turned
    //private float currentDegree = 0f;
    private static float degree;
    //private static final int TIMEOUT = 6000; // Expressed in milliseconds

    // To do navigation periodically
    private Handler handler;
    private Runnable runnableCode;
    private Node currentDestination;
    private String lastDetectedDestination;

    //initialize the class
    public Compass(Context context, Navigation n){
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        this.activity = n;
        follow = false;
    }

    public void setCurrentNode(Node n){ this.currentNode = n; }

    public Node getCurrentNode(){ return this.currentNode; }

    /**
     * This method is called when the app goes in foreground.
     */
    public void registerListener(){
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * This method is called when the app goes in background.
     */
    public void unregisterListener(){
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);
        unsetDirection();
    }

    /**
     * This method is automatically called by the underlying system
     * to get the current direction of the phone.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // Get the angle around the z-axis rotated
        degree = Math.round(event.values[0]);
        //currentDegree = -degree;

        //Check whether the user has something in front of him
        if( currentNode != null && !follow){
            this.checkDirection();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { /* not in use */ }

    /**
     * When the user set a destination node, the setDirection method is called.
     * It is used to set the current edge he walking on, and the ideal direction he should have to
     * reach the opposite node. These values will be used by the checkDirection method.
     * @param degree ideal direction to reach the destination
     * @param e      the edge on which is walking
     */
    public void setDirection(float degree,Edge e){
        follow = true;
        destinationDirection = degree;
        handler = new Handler();
        currentDestination = e.getNodeTo();

        // Define the code block to be executed
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                if(follow) {
                    adjustDirection(Compass.degree, destinationDirection);
                }

                // Repeat this the same runnable code block again after the timeout.
                handler.postDelayed(runnableCode, AppConstants.TIMEOUT);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }

    /**
     * This method is used either when the user stops the navigation,
     * or when he reaches his destination.
     */
    public void unsetDirection(){
        follow = false;
        if( handler != null )
            handler.removeCallbacks(runnableCode);
        currentDestination=null;
    }

    /**
     * Given the current position and the orientation of the user,
     * this method finds out what the user is facing to and inform Navigation activity,
     * so that the GUI can change properly.
     */
    public void checkDirection(){
        boolean found=false;
        ListIterator<Edge> directionsIterator = currentNode.getEdges().listIterator();
        while( directionsIterator.hasNext() ) {
            Edge e = directionsIterator.next();
            float possibleDirection = e.getDirection();
            float range = AppConstants.DIRECTION_RANGE;

            if( checkIfInRange(degree, possibleDirection - range, possibleDirection + range) ){
                found = true;
                if(!e.getNodeTo().getAudio().equals(lastDetectedDestination)) {
                    lastDetectedDestination = e.getNodeTo().getAudio();
                    activity.changeStatus(e);
                }
            }
        }
        if(!found && lastDetectedDestination != null){
            lastDetectedDestination = null;
            activity.changeStatus(null);
        }
    }

    /**
     * adjustDirection checks whether the user is walking in the correct direction,
     * and correct his/her direction if needed.
     * @param degree user's current direction
     * @param idealDirection desired direction
     */
    public void adjustDirection(float degree, float idealDirection){
        float range  = AppConstants.DIRECTION_RANGE;
        int   round  = AppConstants.ROUND_ANGLE;
        int straight = AppConstants.STRAIGHT_ANGLE;

        if( checkIfInRange(degree, idealDirection-range, idealDirection+range) ){
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                    "AdjustDirection: informa l'utente che è nella giusta direzione");
            Toast.makeText(activity.getApplicationContext(),
                    AppConstants.CORRECT_DIRECTION+currentDestination.getAudio(),
                    Toast.LENGTH_SHORT).show();
        }
        else if( checkIfInRange(degree,
                    (idealDirection+straight)%round-range,
                    (idealDirection+straight+range)%round) )
        {
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                    "AdjustDirection: informa l'utente che è nella direzione opposta");
            Toast.makeText(activity.getApplicationContext(),
                    AppConstants.OPPOSITE_DIRECTION, Toast.LENGTH_SHORT).show();
        }
        else if( checkIfInRange(degree,
                    (idealDirection+straight)%round+range, idealDirection-range))
        {
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                    "AdjustDirection: informa l'utente che è a sinistra della giusta direzione");
            Toast.makeText(activity.getApplicationContext(),
                   AppConstants.TOO_LEFT_DIRECTION+currentDestination.getAudio(), Toast.LENGTH_SHORT).show();
        }
        else if( checkIfInRange(degree,
                    idealDirection+range, (idealDirection+straight)%round+range) )
        {
            Log.d(TAG_DEBUG, "AdjustDirection: informa l'utente che è a destra della giusta direzione");
            Toast.makeText(activity.getApplicationContext(),
                    AppConstants.TOO_RIGHT_DIRECTION+currentDestination.getAudio(), Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d(TAG_DEBUG, "adjustDirection: caso non previsto." +
                    "degree="+degree+", direction="+idealDirection);
        }
    }

    /**
     * @return true if orientation <= rightBound and orientation >= leftBound,
     *         keeping in mind that degrees are mod 360.
     */
    private boolean checkIfInRange(float orientation, float leftBound, float rightBound){

        if(leftBound <= 0) {
            if( (orientation-AppConstants.ROUND_ANGLE >= leftBound) || (orientation<=rightBound) )
                return true;
            else
                return false;
        }
        else if( rightBound%AppConstants.ROUND_ANGLE < leftBound ){
            if( (orientation>=leftBound && orientation < AppConstants.ROUND_ANGLE) ||
                    (orientation>=0 && orientation<rightBound%AppConstants.ROUND_ANGLE) ){
                return true;
            }
            else
                return false;
        }
        else if(leftBound*rightBound > 0 && leftBound < rightBound){
            if(orientation>=leftBound && orientation <= rightBound)
                return true;
            else
                return false;
        }
        else{
            Log.d(TAG_DEBUG, "checkIfInRange: caso non previsto." +
              "leftBound="+leftBound+", rightBound="+rightBound);
            return false;
        }
    }
}