package com.makeitsimple.salagiochi.MeteorDodge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.makeitsimple.salagiochi.R;

public class Player implements SensorEventListener{
    //coordinates
    private int x;
    private int y;
    private int sensorX;
    private int sensorCustomValue;
    //jump
    private boolean jump;
    //screen bound
    private int minY,maxY;
    private int minX,maxX;
    //hit
    private boolean hit;
    private double temp;
    private Rect detectCollision;
    private Bitmap idleState;

    private AnimationManager animationManager;

    Player(Context context, int screenX, int screenY, int value){

        idleState= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_idle);
        x= screenX/2;
        y= screenY;
        maxY= screenY - idleState.getHeight()-5;
        maxX= screenX - idleState.getWidth();
        minX= 0;
        minY= 0;
        sensorCustomValue= value;
        SensorManager sensorManager = (SensorManager)
                context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_GAME);
        }

        detectCollision= new Rect(x, y, idleState.getWidth(),idleState.getHeight());
        Bitmap walkR1= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_walk1);
        Bitmap walkR2= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_walk2);
        Bitmap walkL1= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_walk1_left);
        Bitmap walkL2= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_walk2_left);
        Bitmap jumpL= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_jump_left);
        Bitmap jumpR= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_jump);
        Bitmap hurtL= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_hurt1_left);
        Bitmap hurtL2= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_hurt2_left);
        Bitmap hurtR= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_hurt1);
        Bitmap hurtR2= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_hurt2);

        Animation idle = new Animation(new Bitmap[]{idleState}, 2);
        Animation walkRight = new Animation(new Bitmap[]{walkR1, walkR2}, 0.4f);
        Animation walkLeft = new Animation(new Bitmap[]{walkL1, walkL2}, 0.4f);
        Animation jumpLeft = new Animation(new Bitmap[]{jumpL}, 2);
        Animation jumpRight = new Animation(new Bitmap[]{jumpR}, 2);
        Animation hurtLeft = new Animation(new Bitmap[]{hurtL, hurtL2}, 0.2f);
        Animation hurtRight = new Animation(new Bitmap[]{hurtR, hurtR2}, 0.2f);
        animationManager= new AnimationManager(new Animation[]{idle, walkRight, walkLeft, jumpRight, jumpLeft, hurtRight, hurtLeft});
    }

    void draw(Canvas canvas){
        animationManager.draw(canvas,detectCollision);
    }

    public void update(){
        int state=0;
        int gravity=30;
        float oldLeft= detectCollision.left;
        if(y!=maxY) state=3;
        x+=sensorX;
        if(jump){
            y-= gravity;
        }else y+= gravity-5;

        if(x<=minX) x= minX;
        if(x>=maxX) x= maxX;
        if(y<=minY) y= minY;
        if(y>=maxY) y= maxY;

        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + idleState.getWidth();
        detectCollision.bottom = y + idleState.getHeight();

        //Modifichiamo lo stato dell'animazione in base alla posizione del rect collegato al bitmap
        if(detectCollision.left-oldLeft>2){
            state= 1;
            if(y!=maxY&&!hit)state= 3;
            if(hit) {
                state = 5;
                if (System.currentTimeMillis()- temp >350) {
                    hit = false;
                }
            }
        }
        else if(detectCollision.left-oldLeft<-2){
            state= 2;
            if(y!=maxY&&!hit)state= 4;
            if(hit) {
                state = 6;
                if (System.currentTimeMillis()- temp >350) {
                    hit = false;
                }
            }
        }
        animationManager.playAnim(state);
        animationManager.update();
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        sensorX= (int)event.values[1]*(8+sensorCustomValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {    }

    //Setter
    void setJump(boolean jump) {
        this.jump = jump;
    }
    void playerHit( double time){
        this.hit= true;
        this.temp= time;
    }
    //Getters
    Rect getDetectCollision() {  return detectCollision; }
}