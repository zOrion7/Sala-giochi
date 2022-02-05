package com.makeitsimple.salagiochi.FlappyPlanet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.makeitsimple.salagiochi.R;

import static android.content.Context.SENSOR_SERVICE;


// @@@@@@@@@@@ implements SensorEventListener @@@@@@@@@@@
public class Player implements SensorEventListener {

    private Bitmap bitmap;


    //cordinate
    private int x;
    private int y;

    private int speed;

    private final int GRAVITY = -20;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 50;

    private boolean boosting;


    //limito l'asse y
    private int minY;
    private int maxY;

    private Rect detectCollision;

    // @@@@@@@@@@@
    SensorManager sensorManager;


    // @@@@@ VARIABILI DEL SENSORE @@@@@@@@
    int sensorX;
    int sensorY;



    boolean s=false;

    public Player(Context context, int screenX, int screenY){
        x = 75;
        y = 50;
        speed = 1;

        //ottiene il bitmap dalle risorse Drawable
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playerflappyplanet);

        minY= 0;
        maxY = screenY - bitmap.getHeight() ;

        boosting=false;

        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener(this, sensorManager.getDefaultSensor
                (Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }



    //aggiorna cordinate giocatore

    public void update(){

        Log.d("VELOCITA PLAYER",""+speed);

        //if the ship is boosting
        if (boosting) {
            //speeding up the ship
            speed += 70;
        } else {
            //slowing down if not boosting
            speed -= 3;
        }
        //controlling the top speed
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        //if the speed is less than min speed
        //controlling it so that it won't stop completely
        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        //moving the ship down
        y -= speed + GRAVITY;

        //but controlling it also so that it won't go off the screen
        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }





      //@@@@@@@@ CAMBIO LA POSIZIONE DELLO SPRITE @@@@@@@@@
       // x+=sensorX;
       // y+=sensorY;


        //il rettangolo per la collisione è attaccato allo sprite.
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth()-25;
        detectCollision.bottom = y + bitmap.getHeight()-25;

    }


    //@@@@@@@@ PRENDO X,Y DEL SENSORE
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        sensorX-= (int)sensorEvent.values[0];
        sensorY= (int)sensorEvent.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public void setBoosting() {
        boosting = true;
    }
    public void stopBoosting() {
        boosting = false;
    }

    public boolean isBoosting(){
        return boosting;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public int getSpeed(){
        return speed;
    }


}
