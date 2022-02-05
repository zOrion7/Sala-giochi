package com.makeitsimple.salagiochi.MeteorDodge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import java.util.Random;
import com.makeitsimple.salagiochi.R;

public class Enemy {
    private Bitmap bitmap;
    private Bitmap bitmapDestroied;
    private int x;
    private int y;
    private double time = 0;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private int speedX;
    private int speedY;
    private Rect detectCollision;

    Enemy(Context context, int screenX, int screenY){
        bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.meteor);
        bitmapDestroied= BitmapFactory.decodeResource(context.getResources(), R.drawable.meteor_destroied);
        maxX= screenX;
        maxY= screenY-20;
        minX= 0;
        minY= 0;
        Random generator = new Random();
        speedX = generator.nextInt(6)+3;
        speedY = generator.nextInt(6)+3;
        x= generator.nextInt(maxX-bitmap.getWidth());
        /*y viene impostato a minY in modo da evitare spawn dal basso*/
        y= minY;
        detectCollision= new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(){
        detectCollision.left= x;
        detectCollision.top= y;
        detectCollision.right= x+ bitmap.getWidth()-50;
        detectCollision.bottom= y+ bitmap.getHeight()-50;
        /*la variabile time riceve il valore nel momento in cui vi è l'hit con il player*/
        if(time!=0){
            if(System.currentTimeMillis()-time>100) {
                bitmap= bitmapDestroied;
                y+=15;
                if(y>=maxY-bitmap.getHeight()){
                    y=maxY*2;
                    time= 0;
                }
            }
        }else{
            x+= speedX;
            y+= speedY;
            bounce();
        }
    }
    private void bounce() {
        if(x<minX||x>maxX-bitmap.getWidth())
            speedX *=-1;
        if(y<minY||y>maxY-bitmap.getHeight())
            speedY *=-1;
    }
    void enemyDead(double time){
        this.time= time;
    }
    void increaseSpeed(){
        speedX+= speedX>0? 3:-3;
        speedY+= speedY>0? 3:-3;
    }
    //getters
    public Rect getDetectCollision() {  return detectCollision; }
    public Bitmap getBitmap() { return bitmap;  }

    public int getX() { return x;   }
    public int getY() { return y;   }
}
