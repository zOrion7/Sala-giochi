package com.makeitsimple.salagiochi;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.makeitsimple.salagiochi.ActivityCondivise.Scoreboard;
import com.makeitsimple.salagiochi.ActivityCondivise.ScreenCustomization;
import com.makeitsimple.salagiochi.Breakout.BreakoutGameActivity;
import com.makeitsimple.salagiochi.FlappyPlanet.FlappyPlanet;
import com.makeitsimple.salagiochi.Helpers.DBOpenHelper;
import com.makeitsimple.salagiochi.Helpers.ServerHelper;
import com.makeitsimple.salagiochi.MeteorDodge.MeteorDodgeMain;
import com.makeitsimple.salagiochi.SpaceShooter.SpaceShooterMain;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ScreenCustomization screenC = new ScreenCustomization();
    private Toast backToast;
    private long backPressedTime;
    private EditText nicknameET;
    private Button Game1Button;
    private Button Game2Button;
    private Button Game3Button;
    private Button Game4Button;
    private Button ScoreboardButton;
    public static String NICKNAME = "";
    boolean isOnCreate;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nicknameET = findViewById(R.id.idNickEditText);
        Game1Button = findViewById(R.id.idButtonGioco1);
        Game2Button = findViewById(R.id.idButtonGioco2);
        Game3Button = findViewById(R.id.idButtonGioco3);
        Game4Button = findViewById(R.id.idButtonGioco4);
        ScoreboardButton = findViewById(R.id.idButtonClassifica);

        pref = getApplicationContext().getSharedPreferences("NICKNAME", 0);
        NICKNAME = pref.getString("Nickname", null);
        isOnCreate = true;
        CheckNickname();
       // this.deleteDatabase("my_sms1920salagiochi.db");


       ServerHelper.UpdateGlobalScoreboard(this);
        Game1Button.setOnClickListener(this);
        Game2Button.setOnClickListener(this);
        Game3Button.setOnClickListener(this);
        Game4Button.setOnClickListener(this);
        ScoreboardButton.setOnClickListener(this);


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

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            finish();
            moveTaskToBack(true);
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), R.string.messaggio_toast, Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    public void ActivityGioco1(View view) {

        //activity gioco1

        Intent intent = new Intent(this, FlappyPlanet.class);
        startActivity(intent);

    }

    public void ActivityGioco2(View view) {
        Intent intent = new Intent(this, MeteorDodgeMain.class);
        startActivity(intent);
    }

    public void ActivityGioco3(View view) {
        Intent intent = new Intent(this, SpaceShooterMain.class);
        startActivity(intent);

    }

    public void ActivityGioco4(View view) {
        Intent intent = new Intent(this, BreakoutGameActivity.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        Intent intent = null;
        isOnCreate = false;
        if (CheckNickname()) {
            switch (v.getId()) {
                case R.id.idButtonGioco1:
                    intent = new Intent(this, FlappyPlanet.class);
                    break;
                case R.id.idButtonGioco2:
                    intent = new Intent(this, MeteorDodgeMain.class);
                    break;
                case R.id.idButtonGioco3:
                    intent = new Intent(this, SpaceShooterMain.class);
                    break;
                case R.id.idButtonGioco4:
                    intent = new Intent(this, BreakoutGameActivity.class);
                    break;
                case R.id.idButtonClassifica:
                    intent = new Intent(this, Scoreboard.class);
                    break;
            }
            startActivity(intent);
        }
    }


    void NicknameErrorAnimation() {
        nicknameET.setHintTextColor(getResources().getColor(R.color.colorAccent));
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        nicknameET.startAnimation(shake);
    }

    boolean CheckNickname() {
        if (TextUtils.isEmpty(NICKNAME)) {
            if (!isOnCreate) {
                if (nicknameET.getText().toString().isEmpty()) {
                    nicknameET.setHint(getString(R.string.NicknameEditText));
                    NicknameErrorAnimation();
                    return false;
                } else {
                    String nickname = nicknameET.getText().toString();
                    DBOpenHelper db = new DBOpenHelper(this);
                    Cursor dbNickname = db.getNickname(nickname);
                    if (dbNickname.getCount() > 0) {//Se il nick è presente nel db
                        nicknameET.getText().clear();
                        nicknameET.setHint(getString(R.string.NicknameError));
                        NicknameErrorAnimation();
                        return false;
                    } else {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Nickname", nickname);
                        editor.commit();
                        nicknameET.setInputType(0);
                        nicknameET.setBackground(null);
                        NICKNAME = nickname;
                        return true;
                    }
                }
            }
        } else {
            nicknameET.setText(NICKNAME);
            nicknameET.setBackground(null);
            nicknameET.setInputType(0);
            return true;
        }
        return false;
    }
}
