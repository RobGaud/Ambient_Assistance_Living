package compass;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.renderscript.Float2;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.content.Context;
import android.widget.TextView;

import java.util.ListIterator;

import graph.Edge;
import graph.Node;
import ui.Navigation;

/**
 * Created by Andrea on 14/04/2016.
 */
public class CompassOld  implements SensorEventListener  {

    private static final String TAG_DEBUG = "CompassOld";
    // device sensor manager
    private SensorManager mSensorManager;

    private Navigation activity;
    private ImageView iv;
    private TextView tv=null;

    private boolean follow=false;
    private float direction;
    private float range;
    private Node currentNode;

    // record the compass picture angle turned
    private float currentDegree = 0f;
    private static float degree;
    private static final int TIMEOUT = 3000; // Expressed in milliseconds

    // To do navigation periodically
    private Handler handler;
    private Runnable runnableCode;

    //initialize the class
    public CompassOld(Context context, Navigation n, ImageView i, TextView tv){
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        this.activity = n;
        this.tv = tv;
        iv = i;
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

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        degree = Math.round(event.values[0]);
        /*
        if(tv!=null)
            tv.setText("Heading: " + Float.toString(degree) + " degrees"+
                        "\ncurrentDegree: "+ Float.toString(currentDegree)+ " degrees");
        */
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

        //it means that we have to check that the current degree doesn't exceed
        //a certain range

        // Check user's direction
        //float difference = degree-direction;
        /*
        if(follow) {
            this.adjustDirection(degree, direction);
        }
        */

        // Start the animation
        iv.startAnimation(ra);
        currentDegree = -degree;

        //Check whether the user has something in front of him
        if( currentNode != null ){
            this.checkDirection();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    public void setDirection(float degree){
        follow = true;
        direction = degree;
        handler = new Handler();

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

    private void unsetDirection(){follow=false;}

    public void setCurrentNode(Node n){ this.currentNode = n; }

    // Metodo per vedere cosa ha di fronte (quando non siamo in navigazione)
    public void checkDirection(){
        ListIterator<Edge> directions = currentNode.getEdges().listIterator();
        while( directions.hasNext() ) {

            Edge e = directions.next();
            float direction = e.getDirection();
            float currentDegree = this.currentDegree;
            float range = this.range;

            if (currentDegree >= direction - range && currentDegree <= direction + range) {
                //TODO interagisci con la Navigation
                activity.changeStatus(e);
            }
        }
    }

    // Metodo per correggere la direzione dell'utente (in navigazione)
    // Nota: per informare l'utente potremmo usare il metodo changeStatus di Navigation
    public void adjustDirection(float degree, float direction) {
        if( checkIfInRange(degree, direction-range, direction+range) ){
            //TODO informa l'utente che è nella giusta direzione
        }
        else if( checkIfInRange(degree, (direction+180)%360-range, (direction+180)%360+range) ){
            //TODO informa l'utente che è nella direzione opposta
        }
        else if( checkIfInRange(degree, (direction+180)%360+range, direction-range)){
            //TODO informa l'utente che è a sinistra della direzione giusta
        }
        else if( checkIfInRange(degree, direction+range, (direction+180)%360+range) ){
            //TODO informa l'utente che è a destra della direzione giusta
        }
        else{
            Log.d(TAG_DEBUG, "adjustDirection: caso non previsto. degree="+degree+", direction="+direction);
        }


    }

    // Metodo che restituisce true se orientation è compreso tra leftBound e rightBound,
    // Tenendo conto del fatto che gli angoli sono in mod 360.
    private boolean checkIfInRange(float orientation, float leftBound, float rightBound){
        if(leftBound <= 0) { // Esempio: direction=10
            if( (orientation-360 >= leftBound) || (orientation<=rightBound) )
                return true;
            else
                return false;
        }
        else if( rightBound%360 < leftBound ){ //Esempio: direction=350
            if( (orientation>=leftBound && orientation <= 0) || (orientation>=0 && orientation<rightBound) )
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
            Log.d(TAG_DEBUG, "checkIfInRange: caso non previsto." +
                             "leftBound="+leftBound+", rightBound="+rightBound);
            return false;
        }
    }

}
