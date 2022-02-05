package com.makeitsimple.salagiochi.SpaceShooter;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.makeitsimple.salagiochi.R;

/**
 * La classe SoundPlayer contiene tutti i suoni o audio brevi da riprodurre.<br>
 *{@link #mSoundThread} <br>
 *{@link #mIsPlaying},
 *{@link #mIsCrashPlaying},
 *{@link #mIsExplodePlaying},
 *{@link #mIsLaserPlaying},
 *{@link #mIsShieldPlaying},
 *{@link #mIsUpgradePlaying} son flag utilizzate per controllare lo stato dell'audio associato(true=playing||false=stopped)<br>
 *{@link #mLaserId},
 *{@link #mCrashId},
 *{@link #mExplodeId},
 *{@link #mShieldId},
 *{@link #mUpgradeId} contengono gli id delle risorse audie utlizzate.<br>
 *{@link #mVolume} altezza volume riprodotto dalle casse <br>
 *{@link #mSoundPool} riproduce o ferma gli audio <br>
 *
 */
public class SoundPlayer implements Runnable {

    private Thread mSoundThread;
    private volatile boolean mIsPlaying;
    private SoundPool mSoundPool;
    private int mExplodeId, mLaserId, mCrashId, mShieldId,mUpgradeId;
    private boolean mIsLaserPlaying, mIsExplodePlaying, mIsCrashPlaying, mIsShieldPlaying, mIsUpgradePlaying;
    private float mVolume=0.5f;

    /**
     * Esegue un check della versione android del dispositivo,
     * imposta le proprietà del soundplayer e carica in memoria tutti i suoni da riprodurre.
     *
     * @param context   activity dove è in esecuzione l'app e dove risiedono le risorse in uso corrente
     */
    public SoundPlayer(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }
        mExplodeId = mSoundPool.load(context, R.raw.rock_explode_1, 1);
        mLaserId = mSoundPool.load(context, R.raw.laser_1, 1);
        mCrashId = mSoundPool.load(context, R.raw.rock_explode_2, 1);
        mShieldId = mSoundPool.load(context, R.raw.shield1, 1);
        mUpgradeId = mSoundPool.load(context, R.raw.upgrade, 1);
    }
/**
 * In runtime run() controlla lo stato dei vari suoni e li riproduce
 */
    @Override
    public void run() {
        while (mIsPlaying){
            if (mIsLaserPlaying){
                mSoundPool.play(mLaserId, mVolume, mVolume, 1, 0, 1f);
                mIsLaserPlaying = false;
            }

            if (mIsUpgradePlaying){
                mSoundPool.play(mUpgradeId, mVolume, mVolume, 1, 0, 1f);
                mIsUpgradePlaying = false;
            }

            if (mIsExplodePlaying){
                mSoundPool.play(mExplodeId, mVolume, mVolume, 1, 0, 1f);
                mIsExplodePlaying = false;
            }

            if (mIsCrashPlaying){
                mSoundPool.play(mCrashId, mVolume, mVolume, 1, 0, 1f);
                mIsCrashPlaying = false;
            }
            if (mIsShieldPlaying){
                mSoundPool.play(mShieldId, mVolume, mVolume, 1, 0, 1f);
                mIsShieldPlaying = false;
            }
        }
    }

    void playCrash(){
        mIsCrashPlaying = true;
    }

    void playLaser(){
        mIsLaserPlaying = true;
    }

    void playExplode(){
        mIsExplodePlaying = true;
    }

    void playShield(){
        mIsShieldPlaying = true;
    }

    void playUpgrade(){
        mIsUpgradePlaying = true;
    }
/**
 *  Resume riprende a suonare dopo essere stato messo in pausa
 */
    void resume(){
        mIsPlaying = true;
        mSoundThread = new Thread(this);
        mSoundThread.start();
    }
/**
 * Pause ferma la riproduzione dei suoni
 */
    void pause() throws InterruptedException {
        Log.d("GameThread", "Sound");
        mIsPlaying = false;
        mSoundThread.join();
    }
    /**
     * Libera la memoria del soundpool allocata da tutti i suoni
     */
    void destroy(){
        mSoundPool.unload(mCrashId);
        mSoundPool.unload(mExplodeId);
        mSoundPool.unload(mLaserId);
        mSoundPool.unload(mShieldId);
        mSoundPool.unload(mUpgradeId);
        mSoundPool.release();
    }
}
