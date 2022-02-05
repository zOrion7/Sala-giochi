package com.makeitsimple.salagiochi.SpaceShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.makeitsimple.salagiochi.R;

/**
 *  La classe Laser svolge la funzione di creazione degli oggetti immagine a forma di laser.<br>
 *  {@link #mBitmap} Contiene l'immagine del laser<br>
 *  {@link #mX}	posizione orizontale del laser<br>
 *  {@link #mY} posizione verticale del laser<br>
 *  {@link #OFFSET} Viene utilizzato come distanza tra la navicella del player e il laser stesso, un secondo utilizzo lo ha nel
 *  				essere anche come valore di riferimento per la velocità di spostamento del laser dopo la creazione<br>
 *  {@link #mCollision}	oggetto rettangolare rappresentante i bordi del bitmap<br>
 *
 */
public class Laser {

    private Bitmap mBitmap;
    private int mX;
    private int mY;
    private Rect mCollision;
    private int mScreenSizeX;
    private int mScreenSizeY;
    private final int OFFSET =10;

    /**
     * Il laser deve essere sparato dal player, quindi bisogna ottenere le coordinate x e y della navicella
     * e posizionare il laser al centro del bitmap della navicella+offset da quest'ultima.<br>
     * Viene effetuato uno scale del bitmap;<br>
     * Viene istanziato l'oggetto {@link #mCollision} per creare a bordi di collisione.<br>
     *
     * @param	context     activity dove è in esecuzione l'app e dove risiedono le risorse in uso corrente
     * @param	screenSizeX	coordinate asse ascisse della dimensione dello schermo del dispositivo in uso
     * @param	screenSizeY	coordinate asse ordinate della dimensione dello schermo del dispositivo in uso
     * @param	spaceShipX	coordinate asse ascisse del player
     * @param	spaceShipY	coordinate asse ordinate del player
     * @param	spaceShip	parametro oggetto contenente altezza e lunghezza del bitmap della navicella
     */
    public Laser(Context context, int screenSizeX, int screenSizeY, int spaceShipX, int spaceShipY, Bitmap spaceShip){
        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.space_missiles_006);
        int scaledWidth,scaledHeight;
        scaledWidth= (int) (mBitmap.getWidth() * GameView.IMAGE_SCALING_FACTOR);
        scaledHeight= (int) (mBitmap.getHeight() * GameView.IMAGE_SCALING_FACTOR);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, scaledWidth, scaledHeight, false);
        mX = spaceShipX + spaceShip.getWidth()/2 - mBitmap.getWidth()/2;
        mY = spaceShipY - mBitmap.getHeight() - OFFSET;
        mCollision = new Rect(mX, mY, mX + mBitmap.getWidth(), mY + mBitmap.getHeight());
    }
    /**
     * Vengono aggiornati i valori associato allo spostamento e i parametri di collisione del laser
     */
    public void update(){
        mY -= mBitmap.getHeight() - OFFSET;
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
     * Posiziona l'oggetto fuori dallo schermo
     */
    void destroy(){
        mY = 0 - mBitmap.getHeight();
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
