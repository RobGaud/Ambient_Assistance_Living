package compass;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.content.Context;
import android.widget.TextView;
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
public class CompassOld  implements SensorEventListener  {

    private static final String TAG_DEBUG = "CompassOld";
    // device sensor manager
    private SensorManager mSensorManager;

    private Navigation activity;
    private ImageView iv=null;
    private TextView tv=null;

    private boolean follow;
    private float direction;
    private float range=10.0f;
    private Node currentNode;

    // record the compass picture angle turned
    private float currentDegree = 0f;
    private static float degree;
    private static final int TIMEOUT = 6000; // Expressed in milliseconds

    // To do navigation periodically
    private Handler handler;
    private Runnable runnableCode;
    private Node currentDestination;
    private String lastDetectedDestination;

    /* For debug purposes
    //initialize the class
    public CompassOld(Context context, Navigation n, ImageView i, TextView tv){
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        this.activity = n;
        this.tv = tv;
        iv = i;
        follow = false;
    }
    */

    //initialize the class
    public CompassOld(Context context, Navigation n){
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        this.activity = n;
        follow = false;
    }

    public float getCurrentDegree() {
        return currentDegree;
    }

    public float getRange() {
        return range;
    }



    //put in onResume
    public void registerListener(){
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    //put in onPause
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

        // get the angle around the z-axis rotated
        degree = Math.round(event.values[0]);

        if(tv!=null)
            tv.setText("degree "+(degree));
        if(iv!=null){
            // create a rotation animation (reverse turn degree degrees)
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);
            // how long the animation will take place
            ra.setDuration(210);
            // set the animation after the end of the reservation status
            ra.setFillAfter(true);
            // Start the animation
            iv.startAnimation(ra);
        }

        currentDegree = -degree;

        //Check whether the user has something in front of him
        Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,"follow: "+follow);
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
        direction = degree;
        handler = new Handler();
        currentDestination = e.getNodeTo();

        // Define the code block to be executed
        runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "Called on main thread");
                if(follow) {
                    adjustDirection(CompassOld.degree, direction);
                }

                // Repeat this the same runnable code block again another 2 seconds
                handler.postDelayed(runnableCode, TIMEOUT);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }

    public void unsetDirection(){
        follow = false;
        if( handler != null )
            handler.removeCallbacks(runnableCode);
        currentDestination=null;
    }

    public void setCurrentNode(Node n){ this.currentNode = n; }

    public Node getCurrentNode(){ return this.currentNode; }


    /**
     * Given the current position and the orientation of the user,
     * this method finds out what the user is facing to and inform Navigation activity,
     * so that the GUI can change properly.
     */
    public void checkDirection(){
        boolean found=false;
        ListIterator<Edge> directions = currentNode.getEdges().listIterator();
        while( directions.hasNext() ) {
            Edge e = directions.next();
            float direction = e.getDirection();
            float currentDegree = this.currentDegree;
            float range = this.range;
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "checkDirection: degree= "+degree+" direction: "+direction);

            if( checkIfInRange(degree, direction - range, direction + range) ){
                Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "checkDirection: nodeTo="+e.getNodeTo().getAudio()+"\n");
                found = true;
                if(!e.getNodeTo().getAudio().equals(lastDetectedDestination)) {
                    lastDetectedDestination = e.getNodeTo().getAudio();
                    activity.changeStatus(e);
                }
            }
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG, "---------------------");
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
     * @param direction desired direction
     */
    public void adjustDirection(float degree, float direction){

        if( checkIfInRange(degree, direction-range, direction+range) ){
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                    "AdjustDirection: informa l'utente che è nella giusta direzione");
            Toast.makeText(activity.getApplicationContext(),
                    AppConstants.CORRECT_DIRECTION+currentDestination.getAudio(),
                    Toast.LENGTH_SHORT).show();
        }
        else if( checkIfInRange(degree, (direction+180)%360-range, (direction+180+range)%360) ){
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                    "AdjustDirection: informa l'utente che è nella direzione opposta");
            Toast.makeText(activity.getApplicationContext(),
                    AppConstants.OPPOSITE_DIRECTION, Toast.LENGTH_SHORT).show();
        }
        else if( checkIfInRange(degree, (direction+180)%360+range, direction-range)){
            Log.d(AppConstants.TAG_DEBUG_APP+TAG_DEBUG,
                    "AdjustDirection: informa l'utente che è a sinistra della giusta direzione");
            Toast.makeText(activity.getApplicationContext(),
                   AppConstants.TOO_LEFT_DIRECTION+currentDestination.getAudio(), Toast.LENGTH_SHORT).show();
        }
        else if( checkIfInRange(degree, direction+range, (direction+180)%360+range) ){
            Log.d(TAG_DEBUG, "AdjustDirection: informa l'utente che è a destra della giusta direzione");
            Toast.makeText(activity.getApplicationContext(),
                    AppConstants.TOO_RIGHT_DIRECTION+currentDestination.getAudio(), Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d(TAG_DEBUG, "adjustDirection: caso non previsto. degree="+degree+", direction="+direction);
        }
    }

    /**
     * @return true if orientation <= rightBound and orientation >= leftBound,
     *         keeping in mind that degrees are mod 360.
     */
    private boolean checkIfInRange(float orientation, float leftBound, float rightBound){

        if(leftBound <= 0) {
            if( (orientation-360 >= leftBound) || (orientation<=rightBound) )
                return true;
            else
                return false;
        }
        else if( rightBound%360 < leftBound ){
            if( (orientation>=leftBound && orientation < 360) || (orientation>=0 && orientation<rightBound%360) )
                return true;
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
            //Log.d(TAG_DEBUG, "checkIfInRange: caso non previsto." +
            //  "leftBound="+leftBound+", rightBound="+rightBound);
            return false;
        }
    }
}