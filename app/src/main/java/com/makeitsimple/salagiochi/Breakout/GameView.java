package com.makeitsimple.salagiochi.Breakout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.RectF;


import androidx.annotation.LongDef;
import androidx.core.content.res.ResourcesCompat;

import com.makeitsimple.salagiochi.ActivityCondivise.GameOver;
import com.makeitsimple.salagiochi.R;

import java.util.ArrayList;

import static org.sufficientlysecure.htmltextview.HtmlTextView.TAG;

class GameView extends SurfaceView implements Runnable {

    // CONTEXT
    Context context;

    // THREAD
    private Thread gameThread = null;

    // SURFACEHOLDER
    private SurfaceHolder surfaceHolder;

    // PADDLE
    private Paddle paddle;
    private Bitmap fase1;     // Animation

    // BALL
    private Ball ball;

    // BRICKS
    private int rows = 4;  // row
    private int columns = 5;  // column
    private Brick[][] brick;
    private static int[] levels_bricks_Bitmap = new int[] {R.drawable.brick_1, R.drawable.brick_2, R.drawable.brick_3, R.drawable.brick_4, R.drawable.brick_5, R.drawable.brick_6, R.drawable.brick_7, R.drawable.brick_8, R.drawable.brick_9, R.drawable.brick_10};
    private static int[] levels_bricks_broken_Bitmap = new int[] {R.drawable.brick_1_broken, R.drawable.brick_2_broken, R.drawable.brick_3_broken, R.drawable.brick_4_broken, R.drawable.brick_5_broken, R.drawable.brick_6_broken, R.drawable.brick_7_broken, R.drawable.brick_8_broken, R.drawable.brick_9_broken, R.drawable.brick_10_broken};
    private static Bitmap brick_bitmap;
    private static Bitmap brick_broken_bitmap;
    int bricks_deleted = 0; // bricks eliminated
    int level;
    static int last_brick_hit_row, last_brick_hit_column;
    // real because the method RectF.intersect() moves the RectF to thhe collision's position.
    float real_ball_left;
    float real_ball_top;
    float real_ball_right;
    float real_ball_bottom;

    // BONUS/MALUS
    private Bonus_Malus bonus_malus;
    boolean bonus_malus_ready = false;
    private int spawnTime = 7000; // 10 secs
    private int despawnTime = 10000; // 7 secs

    // VARIABLES
    private volatile boolean playing = true;
    private static boolean paused;
    static int i, k;
    int j, signed=k+1;
    private long timer;
    int score;

    // LIVES
    static byte lives;
    private static Bitmap life_bitmap;
    RectF life1_rect;
    RectF life2_rect;
    RectF life3_rect;

    // CANVAS
    private Canvas canvas;
    private Paint paint;

    // DISPLAY
    private float screenX;
    private float screenY;

    //BACKGROUND STARS
    private int state;
    private Bitmap backgroundInGame;
    private ArrayList<Star> stars;
    int starNum = 100;

    // USEFUL FOR FPS AND ANIMATIONS
    private long timeThisFrame;
    private long fps;

    // GAME OVER TEXT
    static boolean gameover = false;
    Typeface typeface;

    // SONGS
    private final MediaPlayer mediaPlayer;
    int media_length;

    // WHEN BRICKS HAVE BEEN CREATED DONE FOR 1ST TIME = TRUE
    boolean first_bricks_creation = false;

    private static SoundPlayer sound;

    public GameView(Context context, float screenX, float screenY) {
        super(context);
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        gameover = false;
        lives = 3;
        sound = new SoundPlayer(context);
        mediaPlayer= MediaPlayer.create(context, R.raw.breakout_theme);
        mediaPlayer.setVolume(0.8f,0.8f);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        stars = new ArrayList<>();
        //genero 100 stelle
        for (int i=0;i<starNum;i++){
            Star s = new Star((int)screenX,(int)screenY);
            stars.add(s);
        }
        paddle = new Paddle(context, screenX, screenY);
        ball = new Ball(context, this.screenX, this.screenY, paddle);

        // BRICKS
        brick = new Brick[rows][columns];
        brick_bitmap = BitmapFactory.decodeResource(this.getResources(), levels_bricks_Bitmap[level]);
        brick_broken_bitmap = BitmapFactory.decodeResource(this.getResources(), levels_bricks_broken_Bitmap[level]);
        for(i=0; i<rows; i++){
            for(k=0; k<columns; k++){
                brick[i][k] = new Brick(context, i, k, screenX, screenY, rows, columns);
                Log.d(TAG, "brick [" + i + "]" + "[" + k + "] created");
            }
        }

        //LIVES
        life_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.breakout_life);
        float life_width = screenX/15;
        float life_height = screenY/14;
        float life_space = screenX*0.01f;
        life1_rect = new RectF(this.screenX - 3*life_width - 3*life_space,this.screenY - life_height - life_space ,screenX - 2*life_width - 3*life_space ,screenY - life_space);
        life2_rect = new RectF(this.screenX - 2*life_width - 2*life_space,this.screenY - life_height - life_space ,screenX - 1*life_width - 2*life_space ,screenY - life_space);
        life3_rect = new RectF(this.screenX - 1*life_width - 1*life_space,this.screenY - life_height - life_space ,screenX - 1*life_space ,screenY - life_space);

        typeface = ResourcesCompat.getFont(context, R.font.neon_font);
        surfaceHolder = getHolder();
        paint = new Paint();
        backgroundInGame = BitmapFactory.decodeResource(this.getResources(), R.drawable.background_ingame_meteordodge);
        fase1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.paddle_standard1);

        // Draw bricks, ball and paddle on start
        // Not start till tapping screen, because "playing = true"
        // if activity is resumed (method resume) trigger when we start a game.
        paddle.update(fps, state);

        timer = System.currentTimeMillis();
        state = 0;
        drawBricksAndRestart(screenX, screenY);
    }

    public void drawBricksAndRestart(float screenX, float screenY){
        if (surfaceHolder.getSurface().isValid()) {

            // Lock the canvas ready to draw
            canvas = surfaceHolder.lockCanvas();

            // Draw Bricks
            for(i=0; i<rows; i++){
                for(k=0; k<columns; k++){

                    canvas.drawBitmap(brick_bitmap, null, brick[i][k].getdetectCollision(), null);
                }
            }

            // Draw to the screen
            surfaceHolder.unlockCanvasAndPost(canvas);
            first_bricks_creation = true;
        }

        // Put the ball back on start
        ball.reset();
        paddle.reset(this.screenX, this.screenY);
    }

    @Override
    public void run() {
        while (playing) {
            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if (!paused) {
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame > 0) {
                fps = 1000 / timeThisFrame;
            }

        }

    }

    public void update() {
        //if(lives<1) //gameover;
        paddle.update(fps, state);
        ball.update(fps);
        for (Star s : stars) s.update();
        if(bonus_malus_ready){
            if(ball.getDetectCollision().intersect(bonus_malus.getDetectCollision())){
                switch(bonus_malus.bonus_malus_selected){
                    case 0: // 50 pts
                    case 1:
                    case 2:
                    case 3:
                        sound.playPickSound();
                        score +=50;
                        break;

                    case 4: // 100 pts
                    case 5:
                    case 6:
                        sound.playPickSound();
                        score +=100;
                        break;

                    case 7: // 250 pts
                    case 8:
                        sound.playPickSound();
                        score +=250;
                        break;

                    case 9: // 500 pts
                        sound.playPickSound();
                        score +=500;
                        break;

                    case 10: // LARGE
                    case 11:
                    case 12:
                        paddle.edit(2);
                        sound.playGrowingPaddleSound();
                        break;

                    case 13: // TIGHT
                    case 14:
                        paddle.edit(1);
                        break;

                    case 15: // SLOW
                    case 16:
                        paddle.setPaddleSpeed(0);
                        break;

                    case 17: // FAST
                    case 18:
                    case 19:
                        paddle.setPaddleSpeed(1);
                        break;
                }
                bonus_malus_ready = false;
            }
        }

        if(bonus_malus_ready && System.currentTimeMillis() - timer >= 7 *1000){
            bonus_malus_ready = false;
            timer = System.currentTimeMillis();
        }
        if( !bonus_malus_ready && System.currentTimeMillis() - timer >= spawnTime){
            bonus_malus = new Bonus_Malus(getContext(), screenX, screenY);
            bonus_malus_ready = true;
            timer = System.currentTimeMillis();
        }

        real_ball_left = ball.getDetectCollision().left;
        real_ball_top = ball.getDetectCollision().top;
        real_ball_right = ball.getDetectCollision().right;
        real_ball_bottom = ball.getDetectCollision().bottom;

        // If ball is on top middle of the screen start checking
        if(ball.getY()<screenY/2 ){
            brickUpdate();
        }

        if (bricks_deleted==rows*columns){
            changeLevel();
            setBrick_bitmaps();
        }
    }

    private void brickUpdate() {

        // Check if ball collide with bricks

        for(i=0; i<rows; i++){         // For every row

            for(k=0; k<columns; k++) {        //For every column

                if(brick[i][k].hit<2 && i!=last_brick_hit_row && k!=last_brick_hit_column ){      // If the current brick is not destroyed or last thing hit

                    if (ball.getDetectCollision().intersect(brick[i][k].getdetectCollision())) {        //If ball intersect with a brick
                        brick[i][k].hit();
                        sound.playHitSound();
                        last_brick_hit_row = i;
                        last_brick_hit_column = k;


                        Log.d("COLLISION", "-----------INTERSECT-----------");

                        if(real_ball_left >= brick[i][k].getdetectCollision().left && real_ball_right <= brick[i][k].getdetectCollision().right){		ball.reverseYSpeed();	}	// PALLA SOPRA O SOTTO
                        else if(ball.getY() >= brick[i][k].getdetectCollision().top && real_ball_bottom <= brick[i][k].getdetectCollision().bottom){	ball.reverseXSpeed(); }		// PALLA AL LATO
                        else{		// Palla che tocca sia un LATO sia la BASE SUPERIORE o INFERIORE


                            // Se la palla sta SOPRA      					Quindi tocchiamo 	AB e AC    oppure 	AB e BD
                            if(ball.getY()<=brick[i][k].getdetectCollision().top){


                                // Se la palla sta a SINISTRA 		Quindi tocchiamo AB e AC
                                if(ball.getX()<brick[i][k].getdetectCollision().left){

                                    //  CENTRO PALLA 	-	AB 	>	CENTRO PALLA - AC																	// RIMBALZA VERSO SOPRA
                                    if(Math.abs(real_ball_bottom - ball.getBallHeight()/2 - brick[i][k].getdetectCollision().top) >= Math.abs(real_ball_right - ball.getBallWidth()/2 - brick[i][k].getdetectCollision().left)){     ball.reverseYSpeed(); }
                                    else ball.reverseXSpeed();

                                }else if(ball.getXSpeed()<0){							// Se la palla sta a DESTRA      Quindi tocchiamo AB e BD

                                    //  CENTRO PALLA 	- 	AB 	>	CENTRO PALLA -	BD																	// RIMBALZA VERSO SOPRA
                                    if(Math.abs(real_ball_bottom - ball.getBallHeight()/2 - brick[i][k].getdetectCollision().top) >= Math.abs(real_ball_right - ball.getBallWidth()/2 - brick[i][k].getdetectCollision().right)){     ball.reverseYSpeed();    }
                                    else{ ball.reverseXSpeed(); }		    // RIMBALZA A DESTRA
                                }else ball.reverseYSpeed();
                            }

                            // Se la palla sta SOTTO						Quindi  tocchiamo 	CD e AC		oppure 	CD e BD
                            else{
                                Log.d("COLLISION", "-----------TOCCO DOPPIO - SOTTO-----------");
                                // Se la palla sta a SINISTRA         Quindi tocchiamo CD e AC
                                if(ball.getX()<brick[i][k].getdetectCollision().left){
                                    Log.d(TAG, "SOTTO - Ball xSpeed = " + ball.getXSpeed());

                                    //  CENTRO PALLA 	-	CD 	>	CENTRO PALLA - AC																	// RIMBALZA VERSO SOTTO
                                    if(Math.abs(real_ball_bottom - ball.getBallHeight()/2 - brick[i][k].getdetectCollision().bottom) >= Math.abs(real_ball_right - ball.getBallWidth()/2 - brick[i][k].getdetectCollision().left)){     ball.reverseYSpeed();  Log.d("COLLISION", "-----------TOCCO DOPPIO - SOTTO - SOTTO-----------DISTANZA DALLA BASE INFERIORE = " + Math.abs(ball.getDetectCollision().bottom - ball.getBallHeight()/2 - brick[i][k].getdetectCollision().bottom) + "    DISTANZA DAL LATO SINISTRO = " + Math.abs(ball.getDetectCollision().right - ball.getBallWidth()/2 - brick[i][k].getdetectCollision().left));}
                                    else ball.reverseXSpeed();		// RIMBALZA A SINISTRA

                                }else if(ball.getXSpeed()<0){							// Se la palla sta a DESTRA      Quindi tocchiamo CD e BD

                                    //  CENTRO PALLA 	- 	AB 	>	CENTRO PALLA -	BD																	// RIMBALZA VERSO SOTTO
                                    if(Math.abs(real_ball_bottom - ball.getBallHeight()/2 - brick[i][k].getdetectCollision().bottom) >= Math.abs(real_ball_right - ball.getBallWidth()/2 - brick[i][k].getdetectCollision().right)){    ball.reverseYSpeed();  Log.d("COLLISION", "-----------TOCCO DOPPIO - SOTTO - SOTTO----------- Y = "+ ball.getY() +"rect top = "+ ball.getDetectCollision().top +"DISTANZA DALLA BASE INFERIORE blu= " + Math.abs(ball.getDetectCollision().bottom - ball.getBallHeight()/2 - brick[i][k].getdetectCollision().bottom) + "   ball.right = " + ball.getDetectCollision().right + "   ball.width/2 = " + ball.getBallWidth()/2 + "   brick["+i+"]["+k+"] = " + brick[i][k].getdetectCollision().right + "     DISTANZA DAL LATO DESTRO = " + Math.abs(ball.getDetectCollision().right - ball.getBallWidth()/2 - brick[i][k].getdetectCollision().right));}
                                    else{ ball.reverseXSpeed();}			// RIMBALZA A DESTRA
                                }else ball.reverseYSpeed();
                            }
                        }
                        Log.d("BRICK", "MSG COLPITO brick["+(i+1)+"]["+(k+1)+"]");
                        score +=25;
                        signed=k;
                        if (brick[i][k].hit >= 2){
                            Log.d("BRICK", "e affondato...");
                            bricks_deleted++;
                            score +=25;
                        }
                    }
                }
                Log.d("BRICK", "brick["+i+"]["+k+"].hit" + brick[i][k].hit);
            }
        }
    }

    // Draw the newly updated scene
    public void draw() {

        // 1st draw
        if (!first_bricks_creation) drawBricksAndRestart(screenX, screenY);

        // Make sure our drawing surface is valid or we crash
        if (surfaceHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = surfaceHolder.lockCanvas();

            // BACKGROUND
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);

            // Draw the background's stars
            for (Star s: stars){
                paint.setStrokeWidth(s.getStarSize());
                canvas.drawPoint(s.getX(),s.getY(),paint);
            }

            // Draw paddle
            paddle.draw(canvas);

            // Draw bricks
            for(i=0; i<rows; i++){
                for(k=0; k<columns; k++){
                    switch (brick[i][k].hit) {
                        case 0:
                            canvas.drawBitmap(brick_bitmap, null, brick[i][k].getdetectCollision(), null);
                            break;
                        case 1:
                            canvas.drawBitmap(brick_broken_bitmap, null, brick[i][k].getdetectCollision(), null);
                            break;
                    }
                }
            }

            // Draw ball
            canvas.drawBitmap(ball.getBitmap(), null, ball.getDetectCollision(), null);

            // Draw bonus/malus
            if(bonus_malus_ready && System.currentTimeMillis() - timer <= 7 * 1000){
                if(bonus_malus.getBonus_malus_selected() == 4 || bonus_malus.getBonus_malus_selected() == 5 || bonus_malus.getBonus_malus_selected() == 6){
                    bonus_malus._100_pts_animation(canvas);
                }
                else    canvas.drawBitmap(bonus_malus.getBitmap(), null, bonus_malus.getDetectCollision(), null);
            }

            // Draw HUD
            paint.setTextSize(screenX/32);
            canvas.drawText(getResources().getString(R.string.Livello) + "  " + (level+1) , screenX/96, screenY-screenY/54 , paint);
            canvas.drawText(getResources().getString(R.string.Punteggio) + "  " +score, screenX/3.5f , screenY-screenY/54, paint);
            canvas.drawBitmap(life_bitmap, null, life1_rect , null);
            if(lives>=2) canvas.drawBitmap(life_bitmap, null, life2_rect , null);
            if(lives==3) canvas.drawBitmap(life_bitmap, null, life3_rect , null);

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTypeface(typeface);

            // Draw everything to the screen
            surfaceHolder.unlockCanvasAndPost(canvas);

            // GAME OVER
            if(gameover) {

                // GAME OVER SOUND
                mediaPlayer.stop();
                sound.playGameOverSound();

                do{
                    // Make sure our drawing surface is valid or we crash
                    if (surfaceHolder.getSurface().isValid()) {
                        // Lock the canvas ready to draw
                        canvas = surfaceHolder.lockCanvas();

                        // BACKGROUND
                        canvas.drawColor(Color.BLACK);
                        paint.setColor(Color.WHITE);

                        // Draw the background's stars
                        for (Star s : stars) {
                            paint.setStrokeWidth(s.getStarSize());
                            canvas.drawPoint(s.getX(), s.getY(), paint);
                        }
                        for (Star s : stars) s.update();

                        paint.setTextSize(150);
                        paint.setTextAlign(Paint.Align.CENTER);
                        paint.setTypeface(typeface);
                        canvas.drawText("GAME OVER", canvas.getWidth() / 2, canvas.getHeight() / 2, paint);
                        paint.setTextSize(80);
                        canvas.drawText("TAP TO CONTINUE...", canvas.getWidth() - 500, canvas.getHeight() - 50, paint);

                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }while(gameover);
            }
        }

    }

    public void setBrick_bitmaps(){
        brick_bitmap = BitmapFactory.decodeResource(this.getResources(), levels_bricks_Bitmap[level]);
        brick_broken_bitmap = BitmapFactory.decodeResource(this.getResources(), levels_bricks_broken_Bitmap[level]);
    }

    // If BreakoutGameActivity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        media_length = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }


    // If BreakoutGameActivity is started then
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
        mediaPlayer.seekTo(media_length);
        mediaPlayer.start();
    }

    public static void setPaused(boolean value){
        paused = value;
    }

    public void changeLevel(){
        if(level==9)    level = 0;
        else level++;
        bricks_deleted = 0;
        for(i=0; i<rows; i++){
            for (k=0; k<columns; k++) brick[i][k].hit =0;
        }
        ball.boost+=2;
        ball.min_xSpeed++;
        ball.max_xSpeed++;
        ball.reset();
        paddle.reset(this.screenX, this.screenY);
    }

    public static void updateLives() {
        sound.playLifeLost();
        lives--;
        if(lives<1) gameover = true;
    }

    // The SurfaceView class implements onTouchListener
    // So we can override this method and detect screen touches.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if(gameover){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN||motionEvent.getAction()==MotionEvent.BUTTON_SECONDARY){
                Intent gameOver= new Intent(context, GameOver.class);
                gameOver.putExtra("score",score);
                gameOver.putExtra("GameName","Breakout");
                gameover = false;
                context.startActivity(gameOver);
            }
        }

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:

                paused = false;
                playing = true;
                Log.d(TAG, "Paused = " + paused + "   Playing = " + playing);

                if(motionEvent.getX() > screenX / 2){
                    paddle.setMovementState(paddle.RIGHT);
                }
                else{
                    paddle.setMovementState(paddle.LEFT);
                }

                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                paddle.setMovementState(paddle.STOPPED);
                break;
        }
        return true;
    }
}
