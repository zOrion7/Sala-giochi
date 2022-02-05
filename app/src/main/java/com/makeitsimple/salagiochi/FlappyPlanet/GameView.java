package com.makeitsimple.salagiochi.FlappyPlanet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.makeitsimple.salagiochi.ActivityCondivise.GameOver;
import com.makeitsimple.salagiochi.R;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements  Runnable {

    boolean playing;
    private Thread gameThread = null;

    private Player player;
    private MediaPlayer mediaPlayer;

    int punteggio;

    //drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private ArrayList<Star> stars;
    private Enemy[] enemies;
    private Friend[] friends;

    private int friendsCount = 1;
    private int enemyCount = 3;
    private boolean isGameOver;


    public GameView(Context context, int screenX, int screenY) {
        super(context);

        surfaceHolder = getHolder();
        player = new Player(context, screenX,screenY);
        paint = new Paint();

        mediaPlayer= MediaPlayer.create(context, R.raw.flappyplanet_background);
        mediaPlayer.start();

        stars = new ArrayList<>();
        int starNums = 100;

        isGameOver =false;
        punteggio=0;

        //genero 100 stelle
        for (int i=0;i<starNums;i++){
            Star s = new Star(screenX,screenY);
            stars.add(s);
        }

        //genero 3 pianeti
        enemies = new Enemy[enemyCount];
        for(int i=0; i<enemyCount; i++){
            Random rand = new Random();
            int n = rand.nextInt(2*screenX);
            enemies[i] = new Enemy(context, screenX + n, screenY);
        }


        friends = new Friend[friendsCount];
        for (int i=0;i<friendsCount;i++){
            friends[i] = new Friend(context,screenX,screenY);
        }
    }


    @Override
    public void run() {

        while (playing) {

            //aggiorna i frame
            Update();

            //disegna il frame
            Draw();

            //velocità di aggiornamento frame
            Control();

        }
    }


    private void Update() {


        if(!isGameOver) {
            player.update();
            punteggio++;

            //Se il giocatore sale, aumenta la velocità delle stelle.
            for (Star s : stars) {
                if (!player.isBoosting())
                    s.update(3);

                else {
                    s.update(11);
                }
            }


            //aggiorno i nemici
            for (int i = 0; i < enemyCount; i++) {

                //collisione col giocatore
                if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {
                    isGameOver = true;

                    //nascondo il nemico a sinistra. Ripartirà successivamente da destra
                    enemies[i].setX(-450);
                }

                //se il giocatore sale, aumenta la velocità dei pianeti.
                if (!player.isBoosting())
                    enemies[i].update(3);

                else {
                    enemies[i].update(10);
                }
            }


            //aggiorno gli amici
            for (int i = 0; i < friendsCount; i++) {

                //collisione col giocatore
                if (Rect.intersects(player.getDetectCollision(), friends[i].getDetectCollision())) {

                    //nascondo il pianeta a sinistra. ripartirà successivamente da destra.
                    friends[i].setX(-450);
                    punteggio+=10;
                }

                //se il giocatore sale, aumenta la velocità dei pianeti.
                if (!player.isBoosting())
                    friends[i].update(3);

                else {
                    friends[i].update(10);
                }
            }

        }
    }

    private void Draw(){
        //controllo se surface è valido
        if(surfaceHolder.getSurface().isValid()){

            canvas = surfaceHolder.lockCanvas();

            //sfondo
            canvas.drawColor(Color.BLACK);

            //colore per disegnare le stelle
            paint.setColor(Color.WHITE);

            //disegno tutte le stelle
            for (Star s: stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(),s.getY(),paint);
            }

            //disegno i pianeti
            for (int i = 0; i < enemyCount; i++) {
                canvas.drawBitmap(
                        enemies[i].getBitmap(),
                        enemies[i].getX(),
                        enemies[i].getY(),
                        paint
                );
            }


            //disegno i pianeti
            for (int i = 0; i < friendsCount; i++) {
                canvas.drawBitmap(
                        friends[i].getBitmap(),
                        friends[i].getX(),
                        friends[i].getY(),
                        paint
                );
            }

            //disegno il giocatore
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint );

            //punteggio
            paint.setStrokeWidth(3);
            paint.setColor(Color.WHITE);
            paint.setTextSize(40);
            canvas.drawText("PUNTEGGIO: "+punteggio,50,50, paint);


            //disegno l'hitbox del giocatore
           /* paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(player.getDetectCollision(),paint);
            canvas.drawRect(enemies[0].getDetectCollision(),paint);
            */


            //Gameover
            if(isGameOver){

                //nascondo il giocatore
                player.setX(-600);

                //nascondo i pianeti
                for (int i = 0; i < enemyCount; i++) {
                  enemies[i].setX(-600);
                }
                for (int i = 0; i < friendsCount; i++) {
                    friends[i].setX(-600);
                }


                paint.setStrokeWidth(4);
                paint.setTextSize(150);
                paint.setColor(Color.rgb(142,255,188));
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("GAME OVER",canvas.getWidth()/2,canvas.getHeight()/2,paint);
                paint.setTextSize(80);
                canvas.drawText("TAP TO CONTINUE...",canvas.getWidth()-500,canvas.getHeight()-50,paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void Control(){

        try{
            Thread.sleep(17);

        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


    //gioco in pausa
    public void Pause(){

        playing = false;
        mediaPlayer.pause();
        try{
            //stopping thread
            gameThread.join();

        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    //gioco riprende
    public void Resume(){

        mediaPlayer.start();
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    //controlli touch
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK){


            case MotionEvent.ACTION_DOWN:
                //quando l'utente tocca lo schermo

                if(!isGameOver) {
                    player.setBoosting();
                }
                else{
                    Intent gameOver = new Intent(getContext(), GameOver.class);
                    gameOver.putExtra("score",punteggio);
                    gameOver.putExtra("GameName","FlappyPlanet");
                    getContext().startActivity(gameOver);
                }
                break;

            case MotionEvent.ACTION_UP:
                //quando l'utente rilascia il tocco
                player.stopBoosting();

                break;
        }
        return true;
    }
}
