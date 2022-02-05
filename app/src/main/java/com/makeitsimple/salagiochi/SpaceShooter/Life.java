package com.makeitsimple.salagiochi.SpaceShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.makeitsimple.salagiochi.R;

/**
 *  La classe Life gestisce i punti vita del giocatore,vengono creati all'inizio della partita
 *  ma non possono essere recuperati in caso di perdità.<br>
 *  {@link #mBitmap} Contiene l'immagine della vita<br>
 *  {@link #mX}	posizione orizontale della vita<br>
 *  {@link #mY} posizione verticale della vita<br>
 *  {@link #OFFSET} utilizzato per orientare nella parte destra dello schermo i punti vita<br>
 *
 */
public class Life {

    private Bitmap mBitmap;
    private int mX;
    private int mY;

    private int mScreenSizeX;
    private int mScreenSizeY;

    private final int OFFSET=100;
    /**
     * Tramite le posizioni di riferimento {@link #mScreenSizeX} e {@link #mScreenSizeY},
     * il costruttore posiziona il punto vita sulla schermata di gioco, in alto a destra;<br>
     * Viene effetuato uno scale del bitmap;<br>
     *
     * @param	context     activity dove è in esecuzione l'app e dove risiedono le risorse in uso corrente
     * @param	screenSizeX	valore asse ascisse della dimensione dello schermo del dispositivo in uso
     * @param	screenSizeY	valore asse ordinate della dimensione dello schermo del dispositivo in uso
     * @param	distance	I punti vita essendo disposti uno affianco all'altro occupano uno spazio in larghezza
     * 						quindi per aggiungerne di uno nuovo bisogna tener conto di questo spazio
     */
    public Life(Context context, int screenSizeX, int screenSizeY, int distance){
        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() , mBitmap.getHeight() , false);

        mX =screenSizeX-OFFSET-distance*mBitmap.getWidth();
        mY =0;

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
