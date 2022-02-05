package com.makeitsimple.salagiochi.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="my_sms1920salagiochi.db";
    public static int VERSION= 1;
    public final static String TABLE_NAME="Classifica";
    public final static String _ID= "IdScore";
    public final static String COLUMN_NICKNAME="Nickname";
    public final static String COLUMN_GIOCO1="FlappyPlanet";
    public final static String COLUMN_GIOCO2="MeteorDodge";
    public final static String COLUMN_GIOCO3="SpaceShooter";
    public final static String COLUMN_GIOCO4="Breakout";
    public final static String COLUMN_GLOBALE="Globale";

    //Stringa per la crazione della tabella
    private static final String SCORE_TABLE= "CREATE TABLE "+TABLE_NAME +" ("
            +_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
            +COLUMN_NICKNAME+" TEXT NOT NULL UNIQUE,"
            +COLUMN_GIOCO1+" INTEGER,"+COLUMN_GIOCO2+" INTEGER,"+COLUMN_GIOCO3+" INTEGER,"
            +COLUMN_GIOCO4+" INTEGER,"+COLUMN_GLOBALE+" INTEGER)";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void sendDataDB(String nick, String game, int score){
        SQLiteDatabase db= this.getWritableDatabase();
        Log.d("DB","Stato iniziale Nickname : "+nick+" tipo di gioco : "+game+" score : "+score);
        Cursor cursor= getScore(nick,game);
        if( cursor.getCount()!=0){
            Log.d("DB","La query ottenuta ha record? DIMENSIONE :"+cursor.getCount());
            while(cursor.moveToNext()){
                if(Integer.parseInt(cursor.getString(0))<score){
                    Log.d("DB", "HIGHSCORE SUPERATO");
                    updateScore(score,game,nick);
                }else  Log.d("DB", "HIGHSCORE NON SUPERATO MAX SCORE : "+cursor.getString(0));
            }
        }else{
            Log.d("DB", "CREAZIONE NUOVO RECORD");
            ContentValues values= new ContentValues();
            values.put(COLUMN_NICKNAME, nick);
            values.put(game,score);
            final long insert= db.insert(TABLE_NAME,null, values);
            Log.d("DB", "Risultato caricamento record su DB ( 1 )<-ok (-1)<-fail RISULTATO : "+insert);
        }

    }

    public Cursor getScore(String Nickname, String Gioco){
        SQLiteDatabase db= this.getWritableDatabase();

        //Ritorna lo score conseguito da un certo utente in un determinato gioc
        return db.rawQuery("SELECT "+Gioco+" FROM "+TABLE_NAME+" WHERE "+COLUMN_NICKNAME+" = '"+Nickname+"'",null);
    }



    public void updateScore(int score,String Gioco, String Nickname){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Gioco, score);

        db.update(TABLE_NAME,values,COLUMN_NICKNAME+"='"+Nickname+"'", null);
        db.close();
    }

    public Cursor getGameScoreboard(String Gioco){
        SQLiteDatabase db= this.getWritableDatabase();
        //Ritorna tutti i nickname e score conseguiti in un determinato gioco
        return db.rawQuery("SELECT "+COLUMN_NICKNAME+","+Gioco+ " FROM "+TABLE_NAME+" ORDER BY "+Gioco+" DESC ",null);
    }

    public Cursor getGlobalScoreboard(){
        SQLiteDatabase db= this.getWritableDatabase();
        //Ritorna tutti i nickname e score conseguiti in un determinato gioco
        return db.rawQuery("SELECT "+COLUMN_NICKNAME+","+COLUMN_GIOCO1+","+COLUMN_GIOCO2+","+COLUMN_GIOCO3+","+COLUMN_GIOCO4+","+COLUMN_GLOBALE+" FROM "+TABLE_NAME+" ORDER BY "+COLUMN_GLOBALE+" DESC ",null);
    }


    public Cursor getNickname(String Nickname){
        SQLiteDatabase db= this.getWritableDatabase();
        return db.rawQuery("SELECT *"+" FROM "+TABLE_NAME+" WHERE "+COLUMN_NICKNAME+"='"+Nickname+"'",null);
    }

    public void updateLocalDB( String Nickname, String Gioco1, String Gioco2, String Gioco3, String Gioco4, String Globale ){

        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor = getNickname(Nickname);

        ContentValues values = new ContentValues();
        values.put(COLUMN_NICKNAME,Nickname);
        values.put(COLUMN_GIOCO1,Gioco1);
        values.put(COLUMN_GIOCO2,Gioco2);
        values.put(COLUMN_GIOCO3,Gioco3);
        values.put(COLUMN_GIOCO4,Gioco4);
        values.put(COLUMN_GLOBALE,Globale);

        //se esiste update
        if( cursor.getCount()!=0) {

            Log.d("DB", "ESISTE");
            db.update(TABLE_NAME,values,COLUMN_NICKNAME+"='"+Nickname+"'", null);
            db.close();
        }
        else{
            Log.d("DB", "CREAZIONE NUOVO RECORD");
            final long insert= db.insert(TABLE_NAME,null, values);
            Log.d("DB", "Risultato caricamento record su DB ( 1 )<-ok (-1)<-fail RISULTATO : "+insert);
        }

    }
}
