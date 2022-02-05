package com.makeitsimple.salagiochi.SpaceShooter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.makeitsimple.salagiochi.R;

import java.util.Random;

/**
 *  La classe Star svolge la funzione di creazione degli oggetti immagine a forma di stelle.<br>
 *  {@link #mBitmap} Contiene l'immagine della stella<br>
 *  {@link #mX}	posizione orizontale della stella<br>
 *  {@link #mY} posizione verticale della stella<br>
 *  {@link #mStars}	array intero riservato a contenere i tag dei drawable delle varie stelle<br>
 *  {@link #mSpeed} Esprime il valore di velocità di spostamento su schermo della stessa<br>
 *
 */
public class Star {

    private Bitmap mBitmap;
    private int mX;
    private int mY;
    private int mMaxX;
    private int mSpeed;
    private int mScreenSizeX;
    private int mScreenSizeY;
    private final int STAR_SPEED=7;
    private int[] mStars = new int[]{R.drawable.star_1, R.drawable.star_2, R.drawable.star_3};
    /**
     * Tramite le posizioni di riferimento {@link #mScreenSizeX} e {@link #mScreenSizeY},
     * il costruttore posiziona la nuova stella sulla schermata di gioco;<br>
     * Viene effetuato uno scale del bitmap;<br>
     * L'array {@link #mStars} viene utilizzato nella scelta casuale del bitmat da associare alla nuova stella.<br>
     * Contiene un nodo if per gestire le invocazioni nelle fasi di start e runtime
     *
     * @param	context     activity dove è in esecuzione l'app e dove risiedono le risorse in uso corrente
     * @param	screenSizeX	valore asse ascisse della dimensione dello schermo del dispositivo in uso
     * @param	screenSizeY	valore asse ordinate della dimensione dello schermo del dispositivo in uso
     * @param	randomY		Consente al construttore di capire dove posizione le stelle(false>mY=0|true>mY=numero casuale)
     */
    protected Star(Context context, int screenSizeX, int screenSizeY, boolean randomY){
        mScreenSizeX = screenSizeX;
        mScreenSizeY = screenSizeY;

        Random random = new Random();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), mStars[random.nextInt(3)]);

        float mStarScaleFactor = (float)(random.nextInt(mStars.length) + 1)/mStars.length;
        int scaledWidth,scaledHeight;
        scaledWidth= (int)(mBitmap.getWidth() * mStarScaleFactor);
        scaledHeight= (int)(mBitmap.getHeight() * mStarScaleFactor);

        mBitmap = Bitmap.createScaledBitmap(mBitmap, scaledWidth, scaledHeight, false);


        mMaxX = screenSizeX - mBitmap.getWidth();

        mSpeed = random.nextInt(STAR_SPEED) + 1;

        mX = random.nextInt(mMaxX);

        if (randomY){
            mY = random.nextInt(mScreenSizeY);
        }else{
            mY = 0 - mBitmap.getHeight();
        }

    }
    /**
     * Aggiorna la posizione verticale della stella chiamante
     */
    public void update(){
        mY += mSpeed;
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
