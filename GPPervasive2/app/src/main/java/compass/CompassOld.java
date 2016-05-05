package compass;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.renderscript.Float2;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

/**
 * Created by Andrea on 14/04/2016.
 */
public class CompassOld  implements SensorEventListener  {

    private static final String TAG_DEBUG = "BLIND_CompassOld";
    // device sensor manager
    private SensorManager mSensorManager;

    private Navigation activity;
    private ImageView iv=null;
    private TextView tv=null;

    private boolean follow=false;
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

    //initialize the class
    public CompassOld(Context context, Navigation n, ImageView i, TextView tv){
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        this.activity = n;
        this.tv = tv;
        iv = i;
    }
    //initialize the class
    public CompassOld(Context context, Navigation n){
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        this.activity = n;
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
        //Log.d("provaaa","follow: "+follow);
        if( currentNode != null && !follow){
            this.checkDirection();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

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
                Log.d("Handlers", "Called on main thread");
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
        follow=false;
        if( handler != null )
            handler.removeCallbacks(runnableCode);
        currentDestination=null;
    }

    public void setCurrentNode(Node n){ this.currentNode = n; }

    public Node getCurrentNode(){ return this.currentNode; }

    // Metodo per vedere cosa ha di fronte (quando non siamo in navigazione)
    public void checkDirection(){
        boolean found=false;
        ListIterator<Edge> directions = currentNode.getEdges().listIterator();
        while( directions.hasNext() ) {
            Edge e = directions.next();
            float direction = e.getDirection();
            float currentDegree = this.currentDegree;
            float range = this.range;
            Log.d(TAG_DEBUG, "checkDirection: degree= "+degree+" direction: "+direction);

            if( checkIfInRange(degree, direction - range, direction + range) ){
                Log.d(TAG_DEBUG, "checkDirection: nodeTo="+e.getNodeTo().getAudio()+"\n");
                found = true;
                if(!e.getNodeTo().getAudio().equals(lastDetectedDestination)) {
                    lastDetectedDestination = e.getNodeTo().getAudio();
                    activity.changeStatus(e);
                }
            }
            Log.d(TAG_DEBUG, "---------------------");
        }
        if(!found && lastDetectedDestination != null){
            lastDetectedDestination = null;
            activity.changeStatus(null);

        }

    }

    // Metodo per correggere la direzione dell'utente (in navigazione)
    // Nota: per informare l'utente potremmo usare il metodo changeStatus di Navigation
    public void adjustDirection(float degree, float direction){

        if( checkIfInRange(degree, direction-range, direction+range) ){
            Log.d(TAG_DEBUG, "AdjustDirection: informa l'utente che è nella giusta direzione");
            Toast.makeText(activity.getApplicationContext(),
                    "Keep walking on this direction to reach "+currentDestination.getAudio(),
                    Toast.LENGTH_SHORT).show();

        }
        else if( checkIfInRange(degree, (direction+180)%360-range, (direction+180+range)%360) ){
            Log.d(TAG_DEBUG, "AdjustDirection: informa l'utente che è nella direzione opposta");
            Toast.makeText(activity.getApplicationContext(),
                    "You are walking in the opposite direction.", Toast.LENGTH_SHORT).show();

        }
        else if( checkIfInRange(degree, (direction+180)%360+range, direction-range)){
            Log.d(TAG_DEBUG, "AdjustDirection: informa l'utente che è a sinistra della giusta direzione");
            Toast.makeText(activity.getApplicationContext(),
                    "Turn right to reach "+currentDestination.getAudio(), Toast.LENGTH_SHORT).show();

        }
        else if( checkIfInRange(degree, direction+range, (direction+180)%360+range) ){
            Log.d(TAG_DEBUG, "AdjustDirection: informa l'utente che è a destra della giusta direzione");
            Toast.makeText(activity.getApplicationContext(),
                    "Turn left to reach "+currentDestination.getAudio(), Toast.LENGTH_SHORT).show();
        }
        else{
            Log.d(TAG_DEBUG, "adjustDirection: caso non previsto. degree="+degree+", direction="+direction);
        }
    }

    // Metodo che restituisce true se orientation è compreso tra leftBound e rightBound,
    // Tenendo conto del fatto che gli angoli sono in mod 360.
    private boolean checkIfInRange(float orientation, float leftBound, float rightBound){
        //Log.d(TAG_DEBUG, "checkIfInRange: orientation");
        if(leftBound <= 0) { // Esempio: direction=10
            if( (orientation-360 >= leftBound) || (orientation<=rightBound) )
                return true;
            else
                return false;
        }
        else if( rightBound%360 < leftBound ){ //Esempio: direction=350
            if( (orientation>=leftBound && orientation < 360) || (orientation>=0 && orientation<rightBound%360) )
                return true;
            else
                return false;
        }
        else if(leftBound*rightBound > 0 && leftBound < rightBound){ // Caso generale
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

/**
 * N.B.: Roberto ha dato per scontato che le direzioni non si sovrappongano,
 *       quando stiamo controllando cosa l'utente ha di fronte.
 */

/**
 * OSS: compassOld ha due costruttori: uno in cui passiamo anche imageview e text(della bussola e
 * gradi)per noi, per debugging.
 * Il secondo costruttore invece no, poi in onEventChanged e text e bussolaImage sono a null non fa
 * null
 *
 *
 */