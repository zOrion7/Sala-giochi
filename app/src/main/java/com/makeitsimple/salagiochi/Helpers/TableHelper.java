package com.makeitsimple.salagiochi.Helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.makeitsimple.salagiochi.R;

public class TableHelper {

    //questo metodo serve per creare dinamicamente la classifica in base ai risultati delle query, funziona sia per la classifica globale che singola.
    public static void CreateTable(Context context, Cursor cursor, TableLayout table){


        Typeface typeface = ResourcesCompat.getFont(context, R.font.neon_font);
        TableRow tableRow;
        TextView[] textView = new TextView[cursor.getColumnCount()];


        //HEADER DELLA TABELLA @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        tableRow = new TableRow(context);

        for(int i=0;i<cursor.getColumnCount();i++){

            textView[i] = new TextView(context);
            textView[i].setTypeface(typeface);
            textView[i].setPadding(100, 15, 50, 15);
            textView[i].setText(cursor.getColumnName(i).toUpperCase());
            textView[i].setTextSize(11);

           // textView[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER );
            textView[i].setTextColor(Color.WHITE);
            tableRow.addView(textView[i]);
        }

        table.addView(tableRow);

        //CORPO DELLA TABELLA @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        int k=0;

        //ciclo per le righe
        while( cursor.moveToNext()) {
            tableRow = new TableRow(context);

            //ciclo per le colonne
            for(int i=0;i<cursor.getColumnCount();i++){

                textView[i] = new TextView(context);
                textView[i].setText(cursor.getString(i));
                textView[i].setTypeface(typeface);
                textView[i].setPadding(100, 15, 50, 15);
                textView[i].setTextSize(18);
                if(k==0) textView[i].setTextColor(Color.CYAN);
                else textView[i].setTextColor(Color.WHITE);

                tableRow.addView(textView[i]);

            }
            tableRow.setBackgroundResource(R.drawable.rettangolo_neon);
            table.addView(tableRow);

            k++;
        }
    }
}
