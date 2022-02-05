package com.makeitsimple.salagiochi.SpaceShooter;

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

/**
 * Nella classe GameView risiede tutto il contenuto del gioco SpaceShooter
 * e di come vengono utilizzate le varie componenti associate.<br>
 */
public class GameView extends SurfaceView implements Runnable {

    private Thread mGameThread;
    private volatile boolean mIsPlaying;
    private Player mPlayer;
    private Paint mPaint;
    private Canvas mCanvas;
    private SurfaceHolder mSurfaceHolder;
    //private ArrayList<Laser> mLasers;
    private ArrayList<Enemy> mEnemies;
    private ArrayList<Life> mLife;
    private ArrayList<Star> mStars;
    private ArrayList<Upgrade> mUpgrade;
    private Shield shield;
    private int mScreenSizeX, mScreenSizeY;
    private int mCounter;
    private SoundPlayer mSoundPlayer;
    public static int SCORE = 0;
    public static int ENEMY_DESTROYED = 0;
    private volatile boolean mIsGameOver;
    private int mFireRate;
    private int mFireRateTime;
    private int mMoreAmmoTime;
    private boolean mIsStop;
    private MediaPlayer mMediaPlayer;

    public static final float IMAGE_SCALING_FACTOR=0.6f;
    private static final int LIFE_POINTS=3;
    private static final int RUNTIME_STARTS=3;
    private static final int STARTING_STARS=20;
    private static final int TIME_UNIT =20;
    private static final int OFFSET_AMMO =30;
    private static final int FIRE_RATE_UPGRADED=100;
    private static final int FIRE_RATE_STANDARD=200;
    private static final int SPAWN_TIME_STAR =250;
    private static final int SPAWN_TIME_ENEMY =1500;
    private static final int SPAWN_TIME_UPGRADE =5000;
    private static final int LIFE_TIME_UPGRADE =3000;
    private static final int TIME_COUNTER_RESET =10000;

    private static final int SCORE_TEXT=50;
    private static final int SCORE_X=75;
    private static final int SCORE_Y=75;




/**
 *  Vengono impostati i paramentri di schermo dove andrà in esecuzione il gioco e inizializzati i gestori della musica e suoni<br>
 *  Viene invocata {@link #reset()} .
 *  @param	context     activity dove è in esecuzione l'app e dove risiedono le risorse in uso corrente
 *  @param	screenSizeX	valore asse ascisse della dimensione dello schermo del dispositivo in uso
 *  @param	screenSizeY	valore asse ordinate della dimensione dello schermo del dispositivo in uso
 */
    public GameView(Context context, int screenSizeX, int screenSizeY) {
        super(context);
        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;
        mSoundPlayer = new SoundPlayer(context);
        mPaint = new Paint();
        mSurfaceHolder = getHolder();
        mMediaPlayer = MediaPlayer.create(context, R.raw.music1);
        reset();
    }

    /**
     *  Inizializza tutte le componenti, restaurandole ai valori prepartita<br>
     *  Per esempio: Score=0,imposta il numero di vite a 3 e creare un tappeto di stelle su schermo<br>
     */
    void reset() {
        SCORE = 0;
        mPlayer = new Player(getContext(), mScreenSizeX, mScreenSizeY, mSoundPlayer);
        mEnemies = new ArrayList<>();
        mStars = new ArrayList<>();
        mLife = new ArrayList<>();
        mUpgrade = new ArrayList<>();

        for (int i = 0; i < LIFE_POINTS; i++) {
            mLife.add(new Life(getContext(), mScreenSizeX, mScreenSizeY,i));

        }
        for (int i = 0; i < STARTING_STARS; i++) {
            mStars.add(new Star(getContext(), mScreenSizeX, mScreenSizeY, true));
        }
        mIsGameOver = false;
        mIsStop=false;
        mMediaPlayer.seekTo(0);
        mMediaPlayer.start();
        mFireRate =FIRE_RATE_STANDARD;
        mCounter= TIME_UNIT;
        mFireRateTime=0;
        mMoreAmmoTime=0;
    }

    /**
     *  Gestisce l'esecuzione o l'interruzione del gioco<br>
     */
    @Override
    public void run() {
        while (mIsPlaying) {
            if (!mIsGameOver&&!mIsStop) {
                update();
                draw();
                control();
            }else{
                if(mMediaPlayer.isPlaying())mMediaPlayer.pause();
            }
        }
        Log.d("GameThread", "Run stopped");
    }

    /**
     * In questo metodo vengono attivate tutte le azioni o comportamenti interattivi del gioco;<br>
     * Tramite l'operatore modulo su {@link #mCounter} si attivano questi eventi:<br>
     *     1-{@link #mFireRate} in base al suo valore, il player spara in automatico il laser normale o potenziato<br>
     *     2-{@link #SPAWN_TIME_ENEMY} Fa comparire su schermo un nemico<br>
     *     3-{@link #SPAWN_TIME_UPGRADE} Fa comparire su schermo un potenziamento<br>
     *     4-{@link #SPAWN_TIME_STAR} Vengono generate {@link #RUNTIME_STARTS} stelle<br>
     * Vengono gestile tutte le collisioni fra i nemici, i laser,il player e i potenziamenti;<br>
     * Quando un componente visivo finisce fuori dallo schermo viene distrutto.<br>
     */
    public void update() {
        mPlayer.update();
        if (mCounter % mFireRate == 0) {
            if(mMoreAmmoTime==0){
                mPlayer.fire();
            }else {
                mPlayer.fire(OFFSET_AMMO);
            }
        }
        for (Enemy e : mEnemies) {
            e.update();
            if (Rect.intersects(e.getCollision(), mPlayer.getCollision())) {
                e.destroy();
                mLife.remove(mLife.size() - 1);
                if(mLife.size()==0){
                        mIsGameOver = true;
                }
            }

            for (Laser l : mPlayer.getLasers()) {
                if (Rect.intersects(e.getCollision(), l.getCollision())) {
                    e.hit();
                    l.destroy();
                }
            }

            shield = mPlayer.getShield();
            if(shield !=null){
                if (Rect.intersects(e.getCollision(), shield.getCollision())) {
                    e.hit();
                    e.destroy();
                    shield.destroy();
                }
            }

        }
        boolean deleting = true;
        while (deleting) {
            if (mEnemies.size() != 0) {
                if (mEnemies.get(0).getY() > mScreenSizeY) {
                    mEnemies.remove(0);
                }
            }

            if (mEnemies.size() == 0 || mEnemies.get(0).getY() <= mScreenSizeY) {
                deleting = false;
            }
        }
        if (mCounter % SPAWN_TIME_ENEMY == 0) {
            mEnemies.add(new Enemy(getContext(), mScreenSizeX, mScreenSizeY, mSoundPlayer));
        }

        for (Star s : mStars) {
            s.update();
        }
        deleting = true;
        while (deleting) {
            if (mStars.size() != 0) {
                if (mStars.get(0).getY() > mScreenSizeY) {
                    mStars.remove(0);
                }
            }

            if (mStars.size() == 0 || mStars.get(0).getY() <= mScreenSizeY) {
                deleting = false;
            }
        }
        for (Upgrade u : mUpgrade) {
            u.update();
            if (Rect.intersects(u.getCollision(), mPlayer.getCollision())) {
                u.destroy();
                switch (u.getCurrentUpgrade()) {
                    case R.drawable.upgrade_scudo:{
                        Log.d("Upgrade", "Scudo");
                        mPlayer.shield();
                        break;
                    }
                    case R.drawable.upgrade_velocita:{
                        Log.d("Upgrade", "Velocita");
                        mFireRate =FIRE_RATE_UPGRADED;
                        mFireRateTime= TIME_UNIT;
                        mPlayer.upgrade();
                        break;
                    }
                    case R.drawable.upgrade_missile:{
                        Log.d("Upgrade", "Missile");
                        mMoreAmmoTime= TIME_UNIT;
                        mPlayer.upgrade();
                        break;
                    }
                    default:
                        break;
                }
            }

        }
        deleting = true;
        while (deleting) {
            if (mUpgrade.size() != 0) {
                if (mUpgrade.get(0).getY() > mScreenSizeY) {
                    mUpgrade.remove(0);
                }
            }

            if (mUpgrade.size() == 0 || mUpgrade.get(0).getY() <= mScreenSizeY) {
                deleting = false;
            }
        }

//--------------------------------------------------------------------------------------------------
        if (mCounter % SPAWN_TIME_STAR == 0) {
            Random random = new Random();
            int mNewStars=random.nextInt(RUNTIME_STARTS) + 1;
            for (int i = 0; i < mNewStars; i++) {
                mStars.add(new Star(getContext(), mScreenSizeX, mScreenSizeY, false));
            }

        }
        if (mCounter % SPAWN_TIME_UPGRADE == 0) {
            mUpgrade.add(new Upgrade(getContext(), mScreenSizeX, mScreenSizeY));
        }


    }

    /**
     *  Inserisce e sposta su schermo tutte le componenti visive ad ogni refresh dello schermo<br>
     */
    public void draw() {
        if (mSurfaceHolder.getSurface().isValid()) {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.BLACK);
            for (Star s : mStars) {
                mCanvas.drawBitmap(s.getBitmap(), s.getX(), s.getY(), mPaint);
            }
            for (Laser l : mPlayer.getLasers()) {
                mCanvas.drawBitmap(l.getBitmap(), l.getX(), l.getY(), mPaint);
            }
            for (Enemy e : mEnemies) {
                mCanvas.drawBitmap(e.getBitmap(), e.getX(), e.getY(), mPaint);
            }
            for (Upgrade u : mUpgrade) {
                mCanvas.drawBitmap(u.getBitmap(), u.getX(), u.getY(), mPaint);
            }
            if(shield !=null) mCanvas.drawBitmap(shield.getBitmap(), shield.getX(), shield.getY(), mPaint);
            for (Life l : mLife) {
                mCanvas.drawBitmap(l.getBitmap(), l.getX(), l.getY(), mPaint);
            }
            drawScore();
            mCanvas.drawBitmap(mPlayer.getBitmap(), mPlayer.getX(), mPlayer.getY(), mPaint);
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    /**
     *  Incrementa di {@link #TIME_UNIT} il contatore in argomento del metodo<br>
     * @param time contatore da incrementare
     * @return time ritorna il contantore incrementato
     */
    int addTime(int time){
        if(time>0){
            time+= TIME_UNIT;
        }
        return time;
    }

    /**
     * Stampa su schermo in alto a sinstra il punteggio
     */
    void drawScore() {
        Paint score = new Paint();
        score.setTextSize(SCORE_TEXT);
        score.setColor(Color.WHITE);
        mCanvas.drawText("Score : " + SCORE, SCORE_X, SCORE_Y, score);
    }

    /**
     *Chiama il metodo {@link Player#steerLeft(float speed)}
     *
     */
    public void steerLeft(float speed) {
        mPlayer.steerLeft(speed);
    }

    /**
     *Chiama il metodo {@link Player#steerRight(float speed)}
     *
     */
    public void steerRight(float speed) {
        mPlayer.steerRight(speed);
    }

    /**
     *  Chiama il metodo {@link Player#stay()}
     */
    public void stay() {
        mPlayer.stay();
    }

    /**
     * Controlla e scandisce le varie fasi del gioco a runtime.<br>
     * Tramite l'uso di {@link #mCounter} e {@link #TIME_UNIT} viene scandito il frame rate di
     * aggiornamento da un immagine all'altra e ad ogni scaglione di mCounter si innescano degli eventi(comparsa nemici,potenziamenti...)<br>
     * Gestisce il tempo di esecuzione di ogni potenziamento e quando terminarlo<br>
     * Verifica che la musica di sottofondo sia sempre in play<br>
     * Manda in gameover il gioco se {@link #mIsGameOver}=true <br>
     * @throws InterruptedException
     */
    public void control() {
        try {
            if (mCounter == TIME_COUNTER_RESET) {
                mCounter = 0;
            }

            Thread.sleep(TIME_UNIT);
            mCounter += TIME_UNIT;
            if(mFireRateTime>=LIFE_TIME_UPGRADE){
                mFireRateTime=0;
                mFireRate =FIRE_RATE_STANDARD;
            }
            if(mMoreAmmoTime>=LIFE_TIME_UPGRADE)mMoreAmmoTime=0;
            mFireRateTime= addTime(mFireRateTime);
            mMoreAmmoTime= addTime(mMoreAmmoTime);

            //loop case
            if(!mMediaPlayer.isPlaying()){
                mMediaPlayer.start();
            }

            if (mIsGameOver) {
                gameover();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mette in pausa il gioco, insieme a tutte le risorse come la musica o suoni
     */
    public void pause() {
        mIsPlaying = false;
        try {
            mGameThread.join();
            mSoundPlayer.pause();
            mMediaPlayer.pause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Riprende il gioco dopo essere stato bloccato, insieme a tutte le risorse che erano stato fermate o cancellate
     */
    public void resume() {
        mIsPlaying = true;
        mSoundPlayer.resume();
        mMediaPlayer.start();
        mGameThread = new Thread(this);
        mGameThread.start();
    }

    /**
     * Chiude lo stream audio e trasferisce all'activity "gameover" lo score totalizzato e il nome del gioco
     */
    public void gameover() {
        mMediaPlayer.pause();
        mSoundPlayer.destroy();
        Intent gameOver= new Intent(getContext(), GameOver.class);
        gameOver.putExtra("score",SCORE);
        gameOver.putExtra("GameName","SpaceShooter");
        getContext().startActivity(gameOver);
    }

    /**
     * Mette in pausa o riprende il gioco qualora il player tocchi lo schermo
     * @param event indica il tipo di pressione su schermo da parte del giocatore
     * @return viene invocato il metodo origile onTouchEvent per controllare eventuali RuntimeException
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsStop=!mIsStop;
                break;
        }
        return super.onTouchEvent(event);
    }
}
