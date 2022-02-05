package com.makeitsimple.salagiochi.MeteorDodge;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Animation {
    private Bitmap[] frames;
    private int frameIndex;
    private boolean isPlaying = false;
    private float frameTime;
    private long lastFrame;

    boolean isPlaying() {
        return isPlaying;
    }
    void stop() {
        isPlaying = false;
    }

    void play() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

    Animation(Bitmap[] frames, float animTime) {
        this.frames = frames;
        frameIndex = 0;
        /*frametime consiste nel tempo di riproduzione di un singolo bitmap ottenuto dal tempo totale fratto il numero di bitmap*/
        frameTime = animTime/frames.length;
        lastFrame = System.currentTimeMillis();
    }

    public void draw(Canvas canvas, Rect destination) {
        if(!isPlaying)
            return;

        canvas.drawBitmap(frames[frameIndex], null, destination, new Paint());
    }

    public void update() {
        if(!isPlaying)
            return;
        /*Verifica se al tempo attuale l'ultimo frame è stato interamente riprodotto*/
        if(System.currentTimeMillis() - lastFrame > frameTime*1000) {
            frameIndex++;
            /*Operatore ternario che verifica se l’index ha raggiunto l’ultimo elemento dell’array di bitmap,
              se vero azzera l’index, altrimenti il valore resta invariato. */
            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
            lastFrame = System.currentTimeMillis();
        }
    }
}
