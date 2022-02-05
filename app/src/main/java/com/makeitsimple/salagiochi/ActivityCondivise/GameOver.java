package com.makeitsimple.salagiochi.ActivityCondivise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.makeitsimple.salagiochi.ActivityCondivise.Scoreboard;
import com.makeitsimple.salagiochi.Breakout.BreakoutGameActivity;
import com.makeitsimple.salagiochi.FlappyPlanet.FlappyPlanet;
import com.makeitsimple.salagiochi.Helpers.ConnectionHelper;
import com.makeitsimple.salagiochi.Helpers.DBOpenHelper;
import com.makeitsimple.salagiochi.Helpers.ServerHelper;
import com.makeitsimple.salagiochi.MainActivity;
import com.makeitsimple.salagiochi.MeteorDodge.MeteorDodgeMain;
import com.makeitsimple.salagiochi.R;
import com.makeitsimple.salagiochi.SpaceShooter.SpaceShooterMain;

import java.util.Timer;
import java.util.TimerTask;

public class GameOver extends AppCompatActivity {

    ScreenCustomization screenC= new ScreenCustomization();

    private TextView textPunteggio;
    private Integer punteggio;
    private String myGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        textPunteggio = findViewById(R.id.textViewPunteggio);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            punteggio = bundle.getInt("score");
            myGameName=bundle.getString("GameName");
        }

        textPunteggio.append(punteggio.toString());


        if(ConnectionHelper.ControlloConnessione(this)) {
            ServerHelper.SendScore(this, MainActivity.NICKNAME,  myGameName,punteggio.toString());
        }
        else{
            Toast.makeText(this, R.string.Internet, Toast.LENGTH_LONG).show();


            // NON C'è INTERNET, AVVIO UN THREAD PER INVIARE IL PUNTEGGIO OGNI 3 SECONDI

            final Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    Log.d("INTERNET","Provo a inviare i dati");
                    if(ConnectionHelper.ControlloConnessione(getApplicationContext())) {
                        ServerHelper.SendScore(getApplicationContext(), MainActivity.NICKNAME,  myGameName,punteggio.toString());
                        timer.cancel();
                    }
                }
            }, 0, 3000);
        }

        DBOpenHelper db = new DBOpenHelper(this);
        db.sendDataDB(MainActivity.NICKNAME,myGameName,punteggio);

    }


    public void onRestart(View v){
        Intent restart = null;
        switch (myGameName){
            case "FlappyPlanet":
                restart= new Intent(this, FlappyPlanet.class);
                break;
            case "MeteorDodge":
                restart= new Intent(this, MeteorDodgeMain.class);
                break;
            case "SpaceShooter":
                restart= new Intent(this, SpaceShooterMain.class);
                break;
            case "Breakout":
                restart= new Intent(this, BreakoutGameActivity.class);
                break;
        }
        startActivity(restart);
    }

    public void onScoreboard(View v){
        Intent scoreboard = new Intent(this, Scoreboard.class);
        scoreboard.putExtra("GameName",myGameName);
        startActivity(scoreboard);

    }
    public void onMenu(View v){
        Intent menu = new Intent(this, MainActivity.class);
        startActivity(menu);
        finish();
    }
    @Override
    public void onBackPressed() {
        Intent menu = new Intent(this, MainActivity.class);
        startActivity(menu);
        finish();
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
