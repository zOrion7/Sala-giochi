package com.makeitsimple.salagiochi.Breakout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;
import android.content.Context;

import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.makeitsimple.salagiochi.R;

import static org.sufficientlysecure.htmltextview.HtmlTextView.TAG;


public class Ball {

    private Bitmap ball_bmp;
    private RectF detectCollision;

    //coordinates
    private float x;
    private float y;

    //screen coordinates
    private float screenX;
    private float screenY;

    //ball values
    private float xSpeed;
    int max_xSpeed = 10;
    int min_xSpeed = 3;
    private float ySpeed;
    int boost;
    private float ballWidth, ballHeight;

    // Variable needed to revert ySpeed (ball bounce)
    private boolean revert;

    private Paddle paddle;

    public Ball(Context context, float screenX, float screenY, Paddle paddle){

        // Import Objects/variables
        this.paddle = paddle;
        this.screenX = screenX;
        this.screenY = screenY;

        // Assign bitmap to ball
        ball_bmp= BitmapFactory.decodeResource(context.getResources(), R.drawable.breakout_ball);

        // Assign values to ball
        ballWidth = screenX/22;
        ballHeight = ballWidth;
        Log.d(TAG, "Ball width = " + ballWidth + "   height = " + ballHeight);

        //Set ball coordinates (centre of screen for x and 4/5 screen for y)
        x = screenX/2 - ballWidth/2;          // Lato SINISTRO
        y = screenY - screenY/5 - ballHeight/2;       // Lato SUPERIORE

        Log.d(TAG, "Ball: x = " + x + "  y = " + y);

        // Starting speed
        xSpeed = 5;
        ySpeed = -7;
        boost = 0;

        // Creating a detectCollision around the ball to manage collisions
        detectCollision = new RectF(x, y, x + ballWidth, y + ballHeight);

    }

    public float getX (){
        return x;
    }
    public float getY(){
        return y;
    }

    public float getBallWidth(){
        return ballWidth;
    }

    public float getBallHeight(){
        return ballHeight;
    }

    public void update(long fps){

        revert = false;
        // Paddle bounce
        if (ySpeed>0
           && detectCollision.bottom>=paddle.getDetectColission().top
           && detectCollision.left>=paddle.getDetectColission().left - ballWidth/2
           && detectCollision.right<=paddle.getDetectColission().right + ballWidth/2
           && detectCollision.top<=paddle.getDetectColission().top) {
            GameView.last_brick_hit_row = GameView.i+1;
            GameView.last_brick_hit_column = GameView.k+1;
            setXspeed((((x + ballWidth/2)-(paddle.getX() + paddle.getLength()/2))*4)/(paddle.getLength()/2));
            revert = true;
        }

        // Display limit bounce
        if(detectCollision.left<=0 || detectCollision.right>=screenX){
            reverseXSpeed();
            GameView.last_brick_hit_row = GameView.i+1;
            GameView.last_brick_hit_column = GameView.k+1;
        }
        if(detectCollision.top<=0) {
            reverseYSpeed();
            GameView.last_brick_hit_row = GameView.i+1;
            GameView.last_brick_hit_column = GameView.k+1;
        } else if(y>screenY){
            reset();
            GameView.updateLives();
            paddle.reset(screenX, screenY);
            GameView.last_brick_hit_row = GameView.i+1;
            GameView.last_brick_hit_column = GameView.k+1;
        }

        // Set new ball position
        x += xSpeed;
        y += ySpeed;

        // detectCollision useful for ball collision
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + ballWidth;
        detectCollision.bottom = y + ballHeight;

        if(revert) reverseYSpeed();
    }

    public void reverseYSpeed(){
        ySpeed = - ySpeed;
    }

    public void reverseXSpeed(){
        xSpeed = - xSpeed;
    }

    private void setXspeed(float paddlePart){

        Log.d("POOP", "vecchia xSpeed = " + xSpeed);

        if (xSpeed > 0){        // The ball is going right
            xSpeed += paddlePart;
            if(xSpeed > max_xSpeed) xSpeed = max_xSpeed;
            else if(xSpeed < min_xSpeed) xSpeed = min_xSpeed;
        }
        else{   // xSpeed < 0   The ball is going left
            xSpeed += paddlePart;
            if(xSpeed < -max_xSpeed) xSpeed = -max_xSpeed;
            else if(xSpeed > -min_xSpeed) xSpeed = -min_xSpeed;
        }
    }

    public void reset(){
        this.x = screenX/2 - ballWidth/2;
        this.y = screenY - screenY/5 - ballHeight/2;
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + ballWidth;
        detectCollision.bottom = y + ballHeight;
        xSpeed = 5 + boost;
        ySpeed = -7 - boost;
        GameView.setPaused(true);
    }

    public Bitmap getBitmap(){
        return ball_bmp;
    }

    public RectF getDetectCollision(){
        return detectCollision;
    }

    public float getXSpeed(){
        return xSpeed;
    }

    public float getYSpeed (){
        return ySpeed;
    }

}