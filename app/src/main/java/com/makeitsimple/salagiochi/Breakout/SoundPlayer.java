package com.makeitsimple.salagiochi.Breakout;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.provider.MediaStore;

import com.makeitsimple.salagiochi.R;

public class SoundPlayer {
    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 3;
    private static SoundPool soundPool;
    private static int hitSound;
    private static int growingPaddleSound;
    private static int pickSound;
    private static int lifeLost;
    private static int gameOverSound;

    public SoundPlayer(Context context){

        // SoundPool is deprecated in API 21 (Lollipop)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        }else{
            //SoundPool (int maxStreams, int streamType, int srcQuality)
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }

        // CAMBIARE HITSOUND
        hitSound = soundPool.load(context, R.raw.breakout_hit_sound, 1);
        pickSound = soundPool.load(context, R.raw.breakout_pick, 1);
        growingPaddleSound = soundPool.load(context, R.raw.breakout_growing_paddle, 1);
        lifeLost = soundPool.load(context, R.raw.breakout_life_lost, 1);
        gameOverSound = soundPool.load(context, R.raw.breakout_game_over, 1);
    }

    public void playHitSound(){

        //play (int soundID,, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playGrowingPaddleSound(){

        //play (int soundID,, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(growingPaddleSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playPickSound(){

        //play (int soundID,, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(pickSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playLifeLost(){

        //play (int soundID,, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(lifeLost, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playGameOverSound(){

        //play (int soundID,, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(gameOverSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
