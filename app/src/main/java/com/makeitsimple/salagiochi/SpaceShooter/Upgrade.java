package com.makeitsimple.salagiochi.SpaceShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.makeitsimple.salagiochi.R;

import java.util.Random;

/**
 * La classe Upgrade riguarda tutti i potenziamenti che il player può
 * acquisire durante la partita.<br>
 * {@link #mBitmap}	Contiene l'immagine del potenziamento.<br>
 * {@link #mCollision}	oggetto rettangolare rappresentante i bordi del bitmap<br>
 * {@link #mSpeed}	Esprime il valore di velocità di spostamento su schermo del potenziamento<br>
 * {@link #mUpgradeSelected}	tag drawable del potenziamento. scelto dal costruttore<br>
 * {@link #mUpgrade}	array intero riservato a contenere i tag dei drawable dei vari potenziamenti<br>
 * {@link #mX} posizione orizzontale del potenziamento.<br>
 * {@link #mY} posizione verticale del potenziamento.<br>
 */
public class Upgrade {

    private Bitmap mBitmap;
    private int mX;
    private int mY;
    private int mSpeed;
    private Rect mCollision;


    private int mUpgradeSelected;
    private int mScreenSizeX;
    private int mScreenSizeY;
    private final int UPGRADE_SPEED=4;
    private int[] mUpgrade = new int[]{R.drawable.upgrade_scudo, R.drawable.upgrade_missile, R.drawable.upgrade_velocita};

    /**
     * Seleziona casualmente tag del potenziamento e la posizone orizzontale mentre lo posiziona verticale è costante all'origine(mY=0)<br>
     * Viene effetuato uno scale del bitmap in base alle necessità di schermo.<br>
     * Viene istanziato l'oggetto {@link #mCollision} per creare a bordi di collisione.<br>
     * @param	context     activity dove è in esecuzione l'app e dove risiedono le risorse in uso corrente
     * @param	screenSizeX	valore asse ascisse della dimensione dello schermo del dispositivo in uso
     * @param	screenSizeY	valore asse ordinate della dimensione dello schermo del dispositivo in uso
     */
    public Upgrade(Context context, int screenSizeX, int screenSizeY){
        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;
        Random random = new Random();
        mUpgradeSelected=mUpgrade[random.nextInt(mUpgrade.length)];
        mBitmap = BitmapFactory.decodeResource(context.getResources(), mUpgradeSelected);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() , mBitmap.getHeight() , false);



        mSpeed = random.nextInt(UPGRADE_SPEED) + 1;

        mX = (random.nextInt(mScreenSizeX- mBitmap.getWidth())+ 1);
        mY = 0 - mBitmap.getHeight();
        mCollision = new Rect(mX, mY, mX + mBitmap.getWidth(), mY + mBitmap.getHeight());


    }
    /**
     * Vengono aggiornati i valori associato allo spostamento e i parametri di collisione del potenziamento
     */
    public void update(){
        mY += mSpeed*UPGRADE_SPEED;
        mCollision.left = mX;
        mCollision.top = mY;
        mCollision.right = mX + mBitmap.getWidth();
        mCollision.bottom = mY + mBitmap.getHeight();
    }
    /**
     * @return mCollision istanza dei bordi del rettangolo associato al bitmap
     */
    Rect getCollision() {
        return mCollision;
    }
    /**
     * Posiziona l'oggetto fuori dallo schermo
     */
    void destroy() {
        mY = mScreenSizeY + 1;
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
     *  @return mY posizione verticale corrente
     */
    public int getY() {
        return mY;
    }
    /**
     * @return mUpgradeSelected
     */
    int getCurrentUpgrade() {
        return mUpgradeSelected;
    }
}
