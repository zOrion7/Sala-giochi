package com.makeitsimple.salagiochi.MeteorDodge;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import androidx.appcompat.app.AppCompatActivity;
import com.makeitsimple.salagiochi.ActivityCondivise.ScreenCustomization;

public class MeteorDodgeGameActivity extends AppCompatActivity {
    GameView gameView;
    private int value;
    ScreenCustomization screenC= new ScreenCustomization();
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Display display= getWindowManager().getDefaultDisplay();
        //Otteniamo la risoluzione dello schermo come un oggetto point
        Point size= new Point();
        display.getRealSize(size);
        Bundle bundle= getIntent().getExtras();
        if (bundle != null)
            value= bundle.getInt("value");
        gameView = new GameView(this, size.x, size.y, value);
        setContentView(gameView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        screenC.setDecorView(getWindow().getDecorView());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}