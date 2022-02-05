package com.makeitsimple.salagiochi.SpaceShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.makeitsimple.salagiochi.R;

import java.util.Random;

import static com.makeitsimple.salagiochi.SpaceShooter.GameView.ENEMY_DESTROYED;
import static com.makeitsimple.salagiochi.SpaceShooter.GameView.SCORE;

/**
 * La classe Enemy svolge la funzione di controllare il comportamento
 * delle navicelle nemiche.<br>
 * {@link #mBitmap} Contiene l'immagine del nemico<br>
 * {@link #mCollision} oggetto rettangolare rappresentante i bordi del bitmap<br>
 * {@link #mEnemies} array intero riservato a contenere i tag dei drawable dei vari nemici<br>
 * {@link #mHP} Hit point, denota quanti colpi può subire il nemico<br>
 * {@link #mIsTurnRight} quando spostare il nemico orizzontalmente( true= dx | false= sx )<br>
 * {@link #mMaxX} limite massimo schermo orizzontale<br>
 * {@link #mMaxY} limite massimo schermo verticale<br>
 * {@link #mSoundPlayer} gestore dei suoni<br>
 * {@link #mSpeed} velocità base del nemico<br>
 * {@link #mSpeedMultiplier}  moltiplicatore velocità del nemico<br>
 * {@link #mX} posizione orizzontale del nemico.<br>
 * {@link #mY} posizione verticale del nemico.<br>
 * {@link #ENEMY_POINT} valore ricompensa annientamento del nemico, di default=50<br>
 */
public class Enemy {

    private Bitmap mBitmap;
    private int mX;
    private int mY;
    private Rect mCollision;
    private int mScreenSizeX;
    private int mScreenSizeY;
    private int[] mEnemies;
    private int mMaxX;
    private int mMaxY;
    private int mHP;
    private int mSpeedMultiplier;
    private boolean mIsTurnRight;
    private SoundPlayer mSoundPlayer;
    private int mSpeed;
    private static final int ENEMY_POINT=50;

    /**
     * Seleziona casualmente il tag in {@link #mEnemies} ,{@link #mX} e {@link #mSpeedMultiplier} mentre {@link #mY} è costante all'origine(mY=0)<br>
     * Gli {@link #mHP} vengono impostati a 5, {@link #mSpeed} a 6<br>
     * Viene effetuato uno scale del bitmap in base alle necessità di schermo<br>
     * Viene istanziato l'oggetto {@link #mCollision} per creare a bordi di collisione.<br>
     * @param	context     activity dove è in esecuzione l'app e dove risiedono le risorse in uso corrente
     * @param	screenSizeX	valore asse ascisse della dimensione dello schermo del dispositivo in uso
     * @param	screenSizeY	valore asse ordinate della dimensione dello schermo del dispositivo in uso
     * @param	soundPlayer	oggetto dedicato alla riproduzione di suoni
     */
    public  Enemy(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer) {
        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;
        mSoundPlayer = soundPlayer;

        mHP = 5;
        mSpeed =6;

        mEnemies = new int[]{R.drawable.enemy_black1, R.drawable.enemy_red1, R.drawable.enemy_green4};
        Random random = new Random();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), mEnemies[random.nextInt(3)]);

        int scaledWidth,scaledHeight;
        scaledWidth = (int) (mBitmap.getWidth() * GameView.IMAGE_SCALING_FACTOR);
        scaledHeight = (int) (mBitmap.getHeight() * GameView.IMAGE_SCALING_FACTOR);

        mBitmap = Bitmap.createScaledBitmap(mBitmap, scaledWidth, scaledHeight, false);

        mSpeedMultiplier = random.nextInt(mEnemies.length) + 1;

        mMaxX = screenSizeX - mBitmap.getWidth();
        mMaxY = screenSizeY - mBitmap.getHeight();

        mX = random.nextInt(mMaxX);
        mY = 0 - mBitmap.getHeight();

        mIsTurnRight = mX < mMaxX;

        mCollision = new Rect(mX, mY, mX + mBitmap.getWidth(), mY + mBitmap.getHeight());

    }
    /**
     * Aggiorna la posizione, i parametri di collisione e dove virare se si raggiunge il limite destro o sinitro dello schermo
     */
    public void update(){
        mY += mSpeed * mSpeedMultiplier;

        if (mX<=0){
            mIsTurnRight = true;
        }else if (mX>=mScreenSizeX-mBitmap.getWidth()){
            mIsTurnRight = false;
        }

        if (mIsTurnRight){
            mX += mSpeed * mSpeedMultiplier;
        }else{
            mX -= mSpeed * mSpeedMultiplier;
        }

        mCollision.left = mX;
        mCollision.top = mY;
        mCollision.right = mX + mBitmap.getWidth();
        mCollision.bottom = mY + mBitmap.getHeight();
    }
    /**
     * @return mCollision
     */
    Rect getCollision() {
        return mCollision;
    }
    /**
     *  Decrementa il valore di {@link #mHP} quando subisce un colpo, in caso di colpo di grazia, incrementa lo SCORE e invoca {@link #destroy()}
     */
    void hit(){
        if (--mHP ==0){
            SCORE += ENEMY_POINT;
            ENEMY_DESTROYED++;
            destroy();
        }else{
            mSoundPlayer.playExplode();
        }
    }
    /**
     * Posiziona l'oggetto fuori dallo schermo
     */
    void destroy(){
        mY = mScreenSizeY + 1;
        mSoundPlayer.playCrash();
    }
    /**
     *  @return {@link #mBitmap} l'immagine associata all'oggetto
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }
    /**
     * @return {@link #mX} posizione orizzontale corrente
     */
    public int getX() {
        return mX;
    }
    /**
     * @return {@link #mY} posizione verticale corrente
     */
    public int getY() {
        return mY;
    }
}
