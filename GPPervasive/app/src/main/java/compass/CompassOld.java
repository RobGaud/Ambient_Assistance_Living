package compass;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

    public float getCurrentDegree() {
        return currentDegree;
    }

    public float getRange() {
        return range;
    }

    // record the compass picture angle turned
    private float currentDegree = 0f;

    //initialize the class
    public CompassOld(Context context, Navigation n, ImageView i, TextView tv){
        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager)context.getSystemService(context.SENSOR_SERVICE);
        this.activity = n;
        this.tv = tv;
        iv = i;
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
        float degree = Math.round(event.values[0]);
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
        float difference = degree-direction;
        this.adjustDirection(difference);

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
                Node possibleDestination = e.getNodeTo();
                activity.changeStatus(possibleDestination);
            }
        }
    }

    // Metodo per correggere la direzione dell'utente (in navigazione)
    public void adjustDirection(float difference){
        //TODO metti la modifica che avevi fatto.
        if(follow){
            String msg;
            if(difference>0){
                msg = "sei a dx della direction";
            }else if(difference<0){
                msg = "sei a sx della direction";
            }else{
                msg = "sei giusto";
            }
            //tv.append("\ndirection: "+direction+"\nmsg: "+msg);
        }
    }

}
