package com.makeitsimple.salagiochi.Breakout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import com.makeitsimple.salagiochi.R;

import static org.sufficientlysecure.htmltextview.HtmlTextView.TAG;

public class Paddle {

    // Display size
    float xScreen;
    float yScreen;

    // RectF is an object that holds four coordinates - just what we need
    private RectF detectCollision;
    private RectF detectCollision_tight;
    private RectF detectCollision_large;

    // How long and high our paddle will be
    private float length;
    private float height;
    float length_tight;
    float length_large;
    private float standardSpeed;
    private float slowSpeed;
    private float fastSpeed;
    private long speed_cooldown = 15 *1000;   // 15 secs
    private long speed_cooldown_check;
    private int edit_cooldown = 15 * 1000;  // 15 secs
    private long edit_cooldown_check;
    private int edit;   // 0 = tight paddle       1 = large paddle

    // X is the far left of the rectangle which forms our paddle
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speed that the paddle will move
    private float paddleSpeed;

    // Which ways can the paddle move
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    // Is the paddle moving and in which direction
    private int paddleMoving = STOPPED;

    // Sprite
    Bitmap standard1_bmp;
    Bitmap standard2_bmp;
    Bitmap tight_bmp;
    Bitmap large_bmp;

    // Animation
    private Animation standard;
    private Animation tight;
    private Animation large;
    private AnimationManager animationManager;

    // This the the constructor method
    // When we create an object from this class we will pass
    // in the screen width and height
    public Paddle(Context context, float screenX, float screenY){

        // Display size
        xScreen = screenX;
        yScreen = screenY;

        standard1_bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle_standard1);
        standard2_bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.paddle_standard2);
        tight_bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.paddle_tight);
        large_bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.paddle_large);

        // Animations
        standard= new Animation(new Bitmap[]{standard1_bmp, standard2_bmp},0.5f);
        tight= new Animation(new Bitmap[]{tight_bmp},2);
        large= new Animation(new Bitmap[]{large_bmp},2);

        animationManager= new AnimationManager(new Animation[]{standard, tight, large});

        // 390 pixels wide and 40 pixels high
        length = xScreen/4;
        height = yScreen/13;
        length_tight = length*0.5f;
        length_large = length*1.5f;

        // Start paddle in roughly the screen centre
        x = screenX / 2 - length/2;
        y = screenY - screenY/5.6f;

        // Rect useful for collision and screen limit detect
        detectCollision = new RectF(x, y, x + length, y + height);
        detectCollision_tight = new RectF(x, y, x + length*0.5f, y + height);
        detectCollision_large = new RectF(x, y, x + length*1.5f, y + height);

        // How fast is the paddle in pixels per second
        standardSpeed = screenX/2.7f;
        slowSpeed = screenX/3.5f;
        fastSpeed = screenX/1.7f;
        paddleSpeed = standardSpeed;

        // Set standard paddle length
        edit = 0;
    }

    // This is a getter method to make the rectangle that
    // defines our paddle available in BreakoutView class
    public RectF getDetectColission(){
        if(edit == 1)  return detectCollision_tight;
        else if(edit == 2)   return detectCollision_large;
        else return detectCollision;
    }

    public float getLength(){
        switch (edit){
            case 0:
                return length;
            case 1:
                return length_tight;
            case 2:
                return length_large;
        }
        // useless but needed
        return length;
    }

    public float getX(){
        return x;
    }

    // This method will be used to change/set if the paddle is going left, right or nowhere
    public void setMovementState(int state){
        paddleMoving = state;
    }

    // This update method will be called from update in BreakoutView
    // It determines if the paddle needs to move and changes the coordinates
    // contained in rect if necessary
    public void update(long fps, int state){
        if(System.currentTimeMillis() - speed_cooldown_check >= speed_cooldown) paddleSpeed = standardSpeed;
        if(paddleMoving == LEFT){
            if(x<=0)    x=0;
                else    x = x - paddleSpeed / fps;
        }else if(paddleMoving == RIGHT){
            switch (edit){
                case 0:
                    if(x+length>=xScreen) x = xScreen - length;
                    else    x = x + paddleSpeed / fps;
                    break;

                case 1:
                    if(x+length_tight>=xScreen) x = xScreen - length_tight;
                    else    x = x + paddleSpeed / fps;
                    break;

                case 2:
                    if(x+length_large>=xScreen) x = xScreen - length_large;
                    else    x = x + paddleSpeed / fps;
                    break;
            }
        }

        setDetectCollision();

        animationManager.playAnim(state);
        animationManager.update();
    }

    public void setPaddleSpeed(int x) {
        switch (x) {
            case 0: // Slow paddle
                paddleSpeed = slowSpeed;
                break;
            case 1: // Fast paddle
                paddleSpeed = fastSpeed;
                break;
        }
        speed_cooldown_check = System.currentTimeMillis();
    }

    public void edit(int edit){
        this.edit = edit;
        edit_cooldown_check = System.currentTimeMillis();
    }

    public void draw(Canvas canvas){
        // Check if paddle has been modified by bonus/malus
        if(System.currentTimeMillis() - edit_cooldown_check < edit_cooldown){
            switch (edit){
                case 1:
                    canvas.drawBitmap(tight_bmp, null, detectCollision_tight, null);
                    break;
                case 2:
                    canvas.drawBitmap(large_bmp, null, detectCollision_large, null);
                    break;
            }
        }else{
            animationManager.draw(canvas, detectCollision);
            edit = 0;
        }
    }

    public void reset(float screenX, float screenY){
        x = screenX/2 - length/2;
        setDetectCollision();
    }

    public void setDetectCollision(){
        detectCollision.left = x;
        detectCollision.right = x + length;
        detectCollision_tight.left = x;
        detectCollision_tight.right = x + length_tight;
        detectCollision_large.left = x;
        detectCollision_large.right = x + length_large;
    }
}
