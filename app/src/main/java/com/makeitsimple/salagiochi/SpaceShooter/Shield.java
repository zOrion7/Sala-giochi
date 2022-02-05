package com.makeitsimple.salagiochi.SpaceShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.makeitsimple.salagiochi.R;

/**
 *  La classe Shield gestisce la barriera del giocatore e viene invocata quando il player ottiene il potenziamento della barriera.<br>
 *  {@link #mBitmap} Contiene l'immagine della barriera<br>
 *  {@link #mX}	posizione orizontale della barriera<br>
 *  {@link #mY} posizione verticale della barriera<br>
 *  {@link #OFFSET} distanza tra lo scudo e il giocatore<br>
 *  {@link #mCollision} oggetto rettangolare rappresentante i bordi del bitmap<br>
 *
 */
public class Shield {

    private Bitmap mBitmap;
    private int mX;
    private int mY;
    private Rect mCollision;
    private int mScreenSizeX;
    private int mScreenSizeY;

    private final int OFFSET =10;

    /**
     * Lo schield una volta ottenuto deve essere posizionato davanti al player<br>
     * Viene effetuato uno scale del bitmap;<br>
     * Viene istanziato l'oggetto {@link #mCollision} per creare a bordi di collisione.<br>
     *
     * @param	context     activity dove è in esecuzione l'app e dove risiedono le risorse in uso corrente
     * @param	screenSizeX	valore asse ascisse della dimensione dello schermo del dispositivo in uso
     * @param	screenSizeY	valore asse ordinate della dimensione dello schermo del dispositivo in uso
     * @param	spaceShipX	coordinate asse ascisse del player
     * @param	spaceShipY	coordinate asse ordinate del player
     * @param	spaceShip	parametro oggetto contenente altezza e lunghezza del bitmap della navicella
     */
    public Shield(Context context, int screenSizeX, int screenSizeY, int spaceShipX, int spaceShipY, Bitmap spaceShip){
        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;


        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shield_up);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() , mBitmap.getHeight() , false);

        mX = spaceShipX + spaceShip.getWidth()/2 - mBitmap.getWidth()/2;
        mY = spaceShipY - mBitmap.getHeight() - OFFSET;


        mCollision = new Rect(mX, mY, mX + mBitmap.getWidth(), mY + mBitmap.getHeight());
    }
/**
 * Il metodo update aggiorna la posizione e i bordi di collisione della barriera ad ogni spostamento del giocatore
 */
    public void update(int spaceShipX, int spaceShipY, Bitmap spaceShip){

            mX = spaceShipX + spaceShip.getWidth()/2 - mBitmap.getWidth()/2;
            mY = spaceShipY - mBitmap.getHeight() - OFFSET;

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
 * Il metodo destroy sposta la posizione e i bordi di collisione fuori dai limiti dello schermo
 */
    void destroy(){
        mY = 0 - mBitmap.getHeight();
        mCollision.left = mY;
        mCollision.top = mY;
        mCollision.right = mY;
        mCollision.bottom = mY;
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
