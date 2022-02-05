package com.makeitsimple.salagiochi.FlappyPlanet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.makeitsimple.salagiochi.R;

import java.util.Random;

public class Friend {


    private Bitmap bitmap;

    //cordinate
    private int x;
    private int y;


    private int speed =1;


    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    private Rect detectCollision;

    public Friend(Context context, int screenX, int screenY){

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pianeta2);

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        //coordinate casuali
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();


        detectCollision = new Rect(x,y, bitmap.getWidth(), bitmap.getHeight());

    }

    public void update(int playerSpeed) {
        // si muove da destra a sinistra
        x -= playerSpeed;
        x -= speed;
        //se raggiunge il margine sinistro, riparte dal margine destro.
        if (x < minX - bitmap.getWidth()) {


            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }


        //rappresento il rettangolo
        detectCollision.left = x+5;
        detectCollision.top = y+5;
        detectCollision.right = x+bitmap.getWidth()-15;  //-20 per rendere il rect più piccolo.
        detectCollision.bottom = y+bitmap.getHeight()-15;

    }

    //per cambiare posizione dopo la collisione
    public void setX(int x){
        this.x = x;
    }

    public Rect getDetectCollision(){
        return detectCollision;
    }

    //getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

}
