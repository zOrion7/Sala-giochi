package com.makeitsimple.salagiochi.Helpers;

import android.content.Context;
import android.util.Log;
import android.widget.TableLayout;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ServerHelper  {


    public static void SendScore(Context context, final String nickname, final String nomeGioco,final String score){
        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        String url = "http://sms1920salagiochi.altervista.org/inserimento.php"; // url del server


        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //i valori nei log sono le print nel file .php del server
                Log.d("VOLLEY",response);
            }
        }, new Response.ErrorListener() { //listener per la gestione degli errori
            @Override
            public void onErrorResponse(VolleyError error) {
                //errore dal server
                Log.d("VOLLEY",error.toString());
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String > dati = new HashMap<>();
                dati.put("nickname", nickname);
                dati.put("score", score);
                dati.put("gioco", nomeGioco);
                return dati;
            }
        };


        MyRequestQueue.add(MyStringRequest);
    }


    public static void UpdateGlobalScoreboard(final Context context){

        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
        String url = "http://sms1920salagiochi.altervista.org/classificaGlobale.php"; // url del server


        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                DBOpenHelper db = new DBOpenHelper(context);
                String[] celle = response.split("\\-");

                for(int i=0;i<celle.length;i+=6) {
                    db.updateLocalDB(celle[i], celle[i+1], celle[i+2], celle[i+3], celle[i+4], celle[i+5]);
                    Log.d("GLOBALE"+i, celle[i]+" "+ celle[i+1]+" "+ celle[i+2]+" "+ celle[i+3]+ " "+celle[i+4]+" "+ celle[i+5]);
                }

            }
        }, new Response.ErrorListener() { //listener per la gestione degli errori
            @Override
            public void onErrorResponse(VolleyError error) {
                //errore dal server
                Log.d("VOLLEY",error.toString());
            }
        });


        MyRequestQueue.add(MyStringRequest);
    }

}
