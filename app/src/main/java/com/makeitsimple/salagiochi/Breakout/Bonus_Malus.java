package com.makeitsimple.salagiochi.Breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import com.makeitsimple.salagiochi.R;

import java.util.Random;

import static org.sufficientlysecure.htmltextview.HtmlTextView.TAG;

public class Bonus_Malus {

    // Display size
    float xScreen;
    float yScreen;
    public float padding;

    // Coordinates
    float x;
    float y;
    float minY;

    // ANIMATIONS
    private Animation _100pts;
    private AnimationManager animationManager;

    private Bitmap bitmap;
    private RectF detectCollision;
    private float length;
    private float height;
    private boolean destroyed;
    int bonus_malus_selected;

    public Bonus_Malus(Context context, float screenX, float screenY){

        padding = Brick.getPadding();
        Log.d(TAG, "Bonus_malus Padding = " + padding);

        // Sprite
        Bitmap _100pts_1 = BitmapFactory.decodeResource(context.getResources(), R.drawable._100pts_1);
        Bitmap _100pts_2 = BitmapFactory.decodeResource(context.getResources(),R.drawable._100pts_2);
        Bitmap _100pts_3 = BitmapFactory.decodeResource(context.getResources(),R.drawable._100pts_3);
        Bitmap _100pts_4 = BitmapFactory.decodeResource(context.getResources(),R.drawable._100pts_4);
        Bitmap _100pts_5 = BitmapFactory.decodeResource(context.getResources(), R.drawable._100pts_5);
        Bitmap _100pts_6 = BitmapFactory.decodeResource(context.getResources(),R.drawable._100pts_6);
        Bitmap _100pts_7 = BitmapFactory.decodeResource(context.getResources(),R.drawable._100pts_7);

        // Display size
        minY = screenY/3 + 5*padding;

        // Random bonus/malus
        Random random = new Random();
        bonus_malus_selected = random.nextInt(20);

        Log.d("RANDOM", "random = " + bonus_malus_selected);

        switch(bonus_malus_selected){
            case 0:     // 50 pts
            case 1:
            case 2:
            case 3:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable._50pts);
                break;

            case 4:     // 100 pts
            case 5:
            case 6:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable._100pts_4);
                // Animations
                _100pts = new Animation(new Bitmap[]{_100pts_1, _100pts_2, _100pts_3, _100pts_4, _100pts_5, _100pts_6, _100pts_7},0.7f);
                animationManager= new AnimationManager(new Animation[]{_100pts});
                break;

            case 7:     // 250 pts
            case 8:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable._250pts);
                break;

            case 9:     // 500 pts
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable._500pts);
                break;

            case 10:    // LARGE
            case 11:
            case 12:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bonus_large);
                break;

            case 13:     // TIGHT
            case 14:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.malus_tight);
                break;

            case 15:    // SLOW
            case 16:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.malus_slow);
                break;

            case 17:    // FAST
            case 18:
            case 19:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bonus_fast);
                break;
        }

        // Values
        length = screenX/8;
        height = screenY/20;

        // Coordinates   =   min + random * max
        x = random.nextFloat() * (screenX - length);  // minX = 0
        y = minY + random.nextFloat() * (screenY/2 + height - minY);
        detectCollision = new RectF(x, y, x + length, y + height);

    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public int getBonus_malus_selected(){
        return bonus_malus_selected;
    }

    public RectF getDetectCollision(){
        return detectCollision;
    }

    public void _100_pts_animation(Canvas canvas) {
        animationManager.playAnim(0);
        animationManager.update();
        animationManager.draw(canvas, detectCollision);
    }
}
