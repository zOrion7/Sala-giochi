package com.makeitsimple.salagiochi.Breakout;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;

import static org.sufficientlysecure.htmltextview.HtmlTextView.TAG;

public class Brick {

    private RectF detectCollision;

    private boolean isVisible;
    public static float padding;

    int hit = 0;

    public Brick(Context context, int row, int column, float screenX, float screenY, int total_rows, int total_columns){


        padding = screenX*0.0055f;
        Log.d(TAG, "Brick Padding = " + padding);
        float scaled_width = (screenX - 7*padding)/total_columns;
        float scaled_height = (screenY/3)/total_rows;

        isVisible = true;

        detectCollision = new RectF(scaled_width*column + (column+1)*padding, scaled_height*row + (row+1)*padding, (scaled_width*(column+1)) + (column+1)*padding, (scaled_height*(row+1)) + (row+1)*padding);
    }

    public void hit(){ hit++; }


    public RectF getdetectCollision(){
        return detectCollision;
    }

    public static float getPadding(){ return padding; }

}