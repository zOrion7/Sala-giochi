package com.makeitsimple.salagiochi.SpaceShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.makeitsimple.salagiochi.R;

import java.util.ArrayList;

/**
 * La classe Player svolge la funzione di controllare il comportamento
 * e lo stato del giocatore.<br>
 * {@link #mBitmap} Contiene l'immagine del player<br>
 * {@link #mCollision} oggetto rettangolare rappresentante i bordi del bitmap<br>
 * 
 * {@link #mIsSteerLeft} True quando l'accellerometro indica che il dispositivo è sbilanciato verso sinistra<br>
 * {@link #mIsSteerRight} True quando l'accellerometro indica che il dispositivo è sbilanciato verso destra<br>
 * {@link #mSteerSpeed} velocità di spostamento destra o sinistra del player<br>
 * {@link #SPEED} moltiplicatore velocità base <br>
 * {@link #mMargin} Margine dalla fine dello schermo dal basso<br>
 * {@link #mMaxX} limite massimo schermo orizzontale<br>
 * {@link #mMinX} limite minimo schermo orizzontale<br>
 * {@link #mSoundPlayer} gestore dei suoni<br>
 * {@link #mLasers} array delle istanze di classe laser create dal player
 * {@link #mShield} Oggetto della classe shield creato dal player quando acquisisce il potenziamento
 * {@link #mX} posizione orizzontale del player.<br>
 * {@link #mY} posizione verticale del player.<br>
 * 
 */
public class Player {

    private Bitmap mBitmap;

    private int mX;
    private int mY;

    private int mMaxX;
    private int mMinX;
    private int mMargin = 16;
    private boolean mIsSteerLeft, mIsSteerRight;
    private float mSteerSpeed;
    private Rect mCollision;
    private ArrayList<Laser> mLasers;
    private Shield  mShield;
    private SoundPlayer mSoundPlayer;
    private Context mContext;
    private int mScreenSizeX, mScreenSizeY;
    private final int SPEED=10;

    /**
     * Imposta la posizione iniaziale del player in basso al centro dello schermo;<br>
     * Viene effetuato uno scale del bitmap in base alle necessità di schermo;<br>
     * Viene istanziato l'oggetto {@link #mLasers} in modo da poter allocare i futuri spari dei laser<br>
     * Viene istanziato l'oggetto {@link #mCollision} per creare a bordi di collisione.<br>
     * @param	context     activity dove è in esecuzione l'app e dove risiedono le risorse in uso corrente
     * @param	screenSizeX	valore asse ascisse della dimensione dello schermo del dispositivo in uso
     * @param	screenSizeY	valore asse ordinate della dimensione dello schermo del dispositivo in uso
     * @param	soundPlayer	oggetto dedicato alla riproduzione di suoni
     */
    public Player(Context context, int screenSizeX, int screenSizeY, SoundPlayer soundPlayer) {
        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;
        mContext = context;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player_ship1_red);
        int scaledWidth,scaledHeight;
        scaledWidth= (int) (mBitmap.getWidth() * GameView.IMAGE_SCALING_FACTOR);
        scaledHeight= (int) (mBitmap.getHeight() * GameView.IMAGE_SCALING_FACTOR);

        mBitmap = Bitmap.createScaledBitmap(mBitmap, scaledWidth, scaledHeight, false);
        mMaxX = screenSizeX - mBitmap.getWidth();
        mMinX = 0;
        mX = screenSizeX/2 - mBitmap.getWidth()/2;
        mY = screenSizeY - mBitmap.getHeight() - mMargin;
        mLasers = new ArrayList<>();
        mSoundPlayer = soundPlayer;
        mCollision = new Rect(mX, mY, mX + mBitmap.getWidth(), mY + mBitmap.getHeight());
    }
/**
 * Sposta il player a destra o sinistra in base ai feedback dell'accellerometro;<br>
 * Aggiorna i bordi di collisione;<br>
 * Spara un laser ad ogni invocazione e controlla se ci sono laser da eliminare in caso hanno oltrepassato il limiti dello schermo.<br>
 */
    public void update(){
        if (mIsSteerLeft){
            mX -= SPEED * mSteerSpeed;
            if (mX<mMinX){
                mX = mMinX;
            }
        }else if (mIsSteerRight){
            mX += SPEED * mSteerSpeed;
            if (mX>mMaxX){
                mX = mMaxX;
            }
        }

        mCollision.left = mX;
        mCollision.top = mY;
        mCollision.right = mX + mBitmap.getWidth();
        mCollision.bottom = mY + mBitmap.getHeight();

        for (Laser l : mLasers) l.update();
        boolean deleting = true;
        while (deleting) {
            if (mLasers.size() != 0) {
                if (mLasers.get(0).getY() < 0) {
                    mLasers.remove(0);
                }
            }
            if (mLasers.size() == 0 || mLasers.get(0).getY() >= 0) {
                deleting = false;
            }
        }
        if(mShield!=null){
            if(mShield.getY()>0)mShield.update(mX, mY, mBitmap);
        }
    }
/**
 * @return mLasers
 */
    ArrayList<Laser> getLasers() {
        return mLasers;
    }
    /**
     * @return mShield
     */
    Shield getShield() {
        return mShield;
    }
    /**
     * Viene creato l'oggetto laser e aggiunto all'array {@link #mLasers}, riproducendone il suono dello sparo
     */
    void fire(){
        mLasers.add(new Laser(mContext, mScreenSizeX, mScreenSizeY, mX, mY, mBitmap));
        mSoundPlayer.playLaser();
    }
    /**
     * Overloading di {@link #fire()}
     * In caso il player ottenga il potenziamento dello sparo multiplo, viene utilizzato questo metodo al posto di quello standard
     * @param offset distanza orizzontale tra i laser sparati contemporaneamente
     */
    void fire(int offset){
        mLasers.add(new Laser(mContext, mScreenSizeX, mScreenSizeY, mX-offset, mY, mBitmap));
        fire();
        mLasers.add(new Laser(mContext, mScreenSizeX, mScreenSizeY, mX+offset, mY, mBitmap));
    }
    /**
     * Viene creato l'oggetto shield ancorato ai movimenti del player, riproducendone il suono della comparsa dello scudo
     */
    void shield(){
        mShield=new Shield(mContext, mScreenSizeX, mScreenSizeY, mX, mY, mBitmap);
        mSoundPlayer.playShield();
    }
    /**
     * Riproduce il suono quando il player ottiene un potenziamento
     */
    void upgrade(){
        mSoundPlayer.playUpgrade();
    }
	/**
	 * @return mCollision
	 */
    Rect getCollision() {
        return mCollision;
    }
	/**
	 * Ordina alla navicella del player di girare a destra
	 * @param speed valore di ritorno dell'accelleromentro
	 */
    void steerRight(float speed){
        mIsSteerLeft = false;
        mIsSteerRight = true;
        mSteerSpeed = Math.abs(speed);
    }
    /**
     * Ordina alla navicella del player di girare a sinistra
     * @param speed valore di ritorno dell'accelleromentro
     */
    void steerLeft(float speed){
        mIsSteerRight = false;
        mIsSteerLeft = true;
        mSteerSpeed = Math.abs(speed);
    }
    /**
     * Ordina alla navicella del player di stare ferma
     */
    void stay(){
        mIsSteerLeft = false;
        mIsSteerRight = false;
        mSteerSpeed = 0;
    }
   /**
    * @return mBitmap l'immagine associata all'oggetto
    */
    public Bitmap getBitmap() {
        return mBitmap;
    }
    /**
     * @return mX posizione orizzontale corrente
     */
    public int getX() {
        return mX;
    }
    /**
     * @return mY posizione verticale corrente
     */
    public int getY() {
        return mY;
    }

}
