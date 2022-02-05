package com.makeitsimple.salagiochi.FlappyPlanet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.makeitsimple.salagiochi.ActivityCondivise.ScreenCustomization;


public class FlappyPlanet extends AppCompatActivity {


    private GameView gameView;
    ScreenCustomization screenC = new ScreenCustomization();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //dimensione dello schermo
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);


        gameView = new GameView(this, size.x, size.y);
        setContentView(gameView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.Pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        screenC.setDecorView(getWindow().getDecorView());
        gameView.Resume();
    }
}
