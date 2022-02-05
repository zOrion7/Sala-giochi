package com.makeitsimple.salagiochi.Breakout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import static org.sufficientlysecure.htmltextview.HtmlTextView.TAG;

public class Animation {
    private Bitmap[] frames;
    private int frameIndex;
    private boolean isPlaying = false;
    private float frameTime;
    private long lastFrame;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void play() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }
    public void stop() {
        isPlaying = false;
    }

    public Animation(Bitmap[] frames, float animTime) {
        this.frames = frames;
        frameIndex = 0;
        frameTime = animTime/frames.length;
        lastFrame = System.currentTimeMillis();
    }

    public void draw(Canvas canvas, RectF destination) {
        if(!isPlaying)
            return;
        canvas.drawBitmap(frames[frameIndex], null, destination, new Paint());
    }
    public void update() {
        if(!isPlaying)
            return;

        if(System.currentTimeMillis() - lastFrame > frameTime*1000) {
            frameIndex++;
            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
            lastFrame = System.currentTimeMillis();
        }
    }
}
