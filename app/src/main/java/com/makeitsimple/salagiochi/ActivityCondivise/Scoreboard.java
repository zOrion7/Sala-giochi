package com.makeitsimple.salagiochi.ActivityCondivise;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.makeitsimple.salagiochi.Helpers.DBOpenHelper;
import com.makeitsimple.salagiochi.Helpers.ServerHelper;
import com.makeitsimple.salagiochi.Helpers.TableHelper;
import com.makeitsimple.salagiochi.R;


public class Scoreboard extends AppCompatActivity {

    ScreenCustomization screenC= new ScreenCustomization();

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_scoreboard);

        TextView gameName = findViewById(R.id.idTextGameName);
        TableLayout table = findViewById(R.id.idTableSingleFlappyPlanet);
        DBOpenHelper db = new DBOpenHelper(this);
        Cursor cursor=null;

        //header activity
        gameName.setTextColor(Color.WHITE);

        //per vedere i punteggi degli altri giocatori e per il calcolo del GLOBAL.
        ServerHelper.UpdateGlobalScoreboard(this);

        Bundle bundle = getIntent().getExtras();
        String myGameName=null;
        if (bundle != null) {
            myGameName = bundle.getString("GameName");
            gameName.setText(myGameName.toUpperCase());

            cursor = db.getGameScoreboard(myGameName);
        }
        else{
            /*Blocchiamo la visualizzazione in Landscape per favorire una miglior comprensione della classifica */
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
             gameName.setText(R.string.ClassificaGlobale);
             cursor = db.getGlobalScoreboard();
        }

        TableHelper.CreateTable(this,cursor,table);


    }

    @Override
    protected void onResume() {
        super.onResume();
        screenC.setDecorView(getWindow().getDecorView());
    }
}
