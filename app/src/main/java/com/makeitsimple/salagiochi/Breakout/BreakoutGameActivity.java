package com.makeitsimple.salagiochi.Breakout;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.makeitsimple.salagiochi.ActivityCondivise.ScreenCustomization;

public class BreakoutGameActivity extends AppCompatActivity {
    GameView gameView;

    ScreenCustomization screenC = new ScreenCustomization();
    private float screenX;
    private float screenY;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object
        Point size = new Point();
        display.getRealSize(size);

        // The size of the screen in pixels. They are inverted because of LANDSCAPE
        screenX = size.y;
        screenY = size.x;

        // Initialize gameView and set it as the view
        gameView = new GameView(this, screenY, screenX);
        setContentView(gameView);

    }

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    @Override
    protected void onResume() {
        super.onResume();
        screenC.setDecorView(getWindow().getDecorView());
        gameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }
}