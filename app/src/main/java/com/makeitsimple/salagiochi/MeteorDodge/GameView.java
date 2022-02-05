package com.makeitsimple.salagiochi.MeteorDodge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.core.content.res.ResourcesCompat;
import com.makeitsimple.salagiochi.ActivityCondivise.GameOver;
import com.makeitsimple.salagiochi.R;

@SuppressLint("ViewConstructor")
class GameView extends SurfaceView implements Runnable {
    protected boolean playing;
    Context context;
    private Thread gameThread = null;
    private Paint paint;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private int score;
    private int oldScore;
    private Bitmap background;
    private Bitmap backgroundInGame;
    private Bitmap[] playerLife;
    private Typeface typeface;

    private Enemy[] enemies;
    private int extraEnemy;
    private int enemyDropRate;
    private int maxEnemyDropRate;
    private int enemyIndex;

    private Player player;
    private int life;
    private boolean[] lives;
    private boolean isGameOver;

    public GameView(Context context, int screenX, int screenY, int value) {
        super(context);
        this.context= context;
        surfaceHolder= getHolder();
        paint= new Paint();
        mediaPlayer= MediaPlayer.create(context, R.raw.spaceman_theme);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        isGameOver=false;
        score= 0;
        oldScore= 0;

        player= new Player(context, screenX, screenY, value);
        life= 3;
        lives= new boolean[life];
        playerLife= new Bitmap[life];
        for(int i=0;i<life; i++){
            playerLife[i]= BitmapFactory.decodeResource(context.getResources(),R.drawable.spaceman_life);
            lives[i]= true;
        }
        life= 2;

        enemyIndex=-1;
        enemyDropRate = 100;
        maxEnemyDropRate = 600;
        extraEnemy= 2;
        int maxEnemy = 8;
        enemies= new Enemy[maxEnemy];
        for(int i= 0; i<maxEnemy; i++)
            enemies[i]= new Enemy(context, screenX, screenY);

        backgroundInGame= BitmapFactory.decodeResource(context.getResources(),R.drawable.background_ingame_meteordodge);
        background= BitmapFactory.decodeResource(context.getResources(),R.drawable.background_meteor_dodge);
        typeface = ResourcesCompat.getFont(context, R.font.neon_font);
        backgroundInGame= Bitmap.createScaledBitmap(backgroundInGame, screenX, screenY, false);
        background= Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            extraEnemy();
            control();
        }
    }

    private void update() {
        score++;
        player.update();
        for(int i= 0; i<extraEnemy; i++){
            enemies[i].update();
            if(Rect.intersects(player.getDetectCollision(),enemies[i].getDetectCollision())) {
                if(life>=0){
                    if (enemyIndex == -1) {
                        lives[life] = false;
                        life -= 1;
                        enemyIndex = i;
                        enemies[i].enemyDead(System.currentTimeMillis());
                        player.playerHit(System.currentTimeMillis());

                    } else if (enemyIndex != i) {
                        lives[life] = false;
                        life -= 1;
                        enemyIndex = i;
                        enemies[i].enemyDead(System.currentTimeMillis());
                        player.playerHit(System.currentTimeMillis());
                    }
                }else {
                    isGameOver= true;
                    playing= false;
                    mediaPlayer.stop();
                }
            }
        }
    }

    private void draw() {
        if(surfaceHolder.getSurface().isValid()){
            Canvas canvas = surfaceHolder.lockCanvas();
            int halfWidth= canvas.getWidth();
            int halfHeight= canvas.getHeight();
            halfWidth/=2;
            halfHeight/=2;
            canvas.drawBitmap(backgroundInGame,0,0,paint);
            paint.setTextSize(50);
            paint.setTypeface(typeface);
            paint.setColor(Color.rgb(142,255,188));
            canvas.drawText("Score: "+score,100,80,paint);
            player.draw(canvas);

            for(int i= 0; i<extraEnemy; i++)
                canvas.drawBitmap(enemies[i].getBitmap(), enemies[i].getX(), enemies[i].getY(), paint);

            if(lives[0]) canvas.drawBitmap(playerLife[0], halfWidth+50,25,paint);
            if(lives[1]) canvas.drawBitmap(playerLife[0], halfWidth+150,25,paint);
            if(lives[2]) canvas.drawBitmap(playerLife[0], halfWidth+250,25,paint);

            if(isGameOver){
                paint.setTextSize(150);
                paint.setColor(Color.rgb(142,255,188));
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(typeface);
                canvas.drawBitmap(background,0,0,paint);
                canvas.drawText("GAME OVER", halfWidth, halfHeight,paint);
                paint.setTextSize(80);
                canvas.drawText("TAP TO CONTINUE...", canvas.getWidth()-500, canvas.getHeight()-50,paint);
            }
            //sblocca il canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void extraEnemy(){
        if(score==oldScore+100){
            /*Aumenta i nemici ogni 100 punti finchè non viene raggiunto il numero massimo di drop */
            if(enemyDropRate < maxEnemyDropRate) enemyDropRate +=100;
            extraEnemy+=1;
            oldScore= score;
            /*Ogni 200 punti aumenta la velocità dei meteoriti*/
            if(enemyDropRate %2==0) for(int i = 0; i<extraEnemy; i++) enemies[i].increaseSpeed();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                player.setJump(true);
                break;
            case MotionEvent.ACTION_UP:
                player.setJump(false);
                break;
        }

        if(isGameOver){
            if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.BUTTON_SECONDARY){
                Intent gameOver= new Intent(context, GameOver.class);
                gameOver.putExtra("score",score);
                gameOver.putExtra("GameName","MeteorDodge");
                context.startActivity(gameOver);
            }
        }
        return true;
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void pause() {
        mediaPlayer.stop();
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {e.printStackTrace();}
    }
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}