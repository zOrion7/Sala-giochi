package com.makeitsimple.salagiochi.MeteorDodge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.makeitsimple.salagiochi.ActivityCondivise.Scoreboard;
import com.makeitsimple.salagiochi.R;
import com.makeitsimple.salagiochi.ActivityCondivise.ScreenCustomization;

public class MeteorDodgeMain extends Activity implements View.OnClickListener {

    private Button buttonPlay;
    private Button buttonScore;
    private int value;
    ScreenCustomization screenC= new ScreenCustomization();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_meteor_dodge);
        buttonPlay= findViewById(R.id.buttonRiprova);
        buttonScore= findViewById(R.id.buttonHomepage);
        SeekBar mySeekBar = findViewById(R.id.seekBarSensore);
        //Listener
        buttonScore.setOnClickListener(this);
        buttonPlay.setOnClickListener(this);
        //Seekbar
        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    @Override
    public void onClick(View v) {
        if(v==buttonPlay){
            Intent startGame= new Intent(this, MeteorDodgeGameActivity.class);
            //Passo il valore della seekbar all'ultimo rilevamento
            startGame.putExtra("value",value);
            startActivity(startGame);
        }
        if(v==buttonScore){
            Intent scoreboard = new Intent(this, Scoreboard.class);
            scoreboard.putExtra("GameName","MeteorDodge");
            startActivity(scoreboard);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        screenC.setDecorView(getWindow().getDecorView());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
