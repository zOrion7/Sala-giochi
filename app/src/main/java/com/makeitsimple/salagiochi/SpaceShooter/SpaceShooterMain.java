package com.makeitsimple.salagiochi.SpaceShooter;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
/**
 * Per la creazione del gioco SpaceShooter ho preso in considerzione il repository:
 * <a href="https://github.com/andevindo/space-shooter-as">SS github</a> <br>
 *
 * La classe SpaceShooterMain � la sezione iniziale del videogioco Spaceshooter
 * utile a preparare la finestra di gioco e a gestire l'accelerometro
 */

import com.makeitsimple.salagiochi.ActivityCondivise.ScreenCustomization;

public class SpaceShooterMain extends AppCompatActivity implements SensorEventListener {

    private GameView mGameView;
/**
 * Inizializza lo stato del display, istanziazione del accelerometro con annesso listner, e della schermata di gioco(gameview).
 */
    ScreenCustomization screenC = new ScreenCustomization();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        mGameView = new GameView(this, point.x, point.y);
        setContentView(mGameView);


        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert manager != null;
        Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }
/**
 * Riprende il gioco dopo che � stato messo in pausa
 */
    @Override
    protected void onResume() {
        super.onResume();
        screenC.setDecorView(getWindow().getDecorView());
        mGameView.resume();
    }
/**
 * Mette in pausa il gioco
 */
    @Override
    protected void onPause() {
        super.onPause();
        mGameView.pause();
    }
/**
 * onSensorCharged in base al feedback dell'acceleromentro,chiama steerLeft(),steerRight() o stay() della classe GameView
 * @param event descrittore oggetto dell'evento associato all'accelerometro
 */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] > 1){
            mGameView.steerLeft(event.values[0]);
        }
        else if (event.values[0] < -1){
            mGameView.steerRight(event.values[0]);
        }else{
            mGameView.stay();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
