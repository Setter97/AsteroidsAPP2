package com.example.aplicacio1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class MagatzemPuntuacionsSQLite extends SQLiteOpenHelper implements MagatzemPuntuacions{

    public MagatzemPuntuacionsSQLite(Context context){
        super(context,"puntuacions",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table vpuntuacions( " +
                "_id INTEGER primary key autoincrement, " +
                "punts integer, " +
                "nom text," +
                "data long);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //hi ha de tancar el cursor.
    }

    @Override
    public void guardarPuntuacions(int punts, String nom, long data) {

        SQLiteDatabase db=getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("punts", punts);
        values.put("nom", nom);
        values.put("data", data);
        db.insert("vpuntuacions",null,values);
        db.close();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result=new Vector<>();

        SQLiteDatabase db=getReadableDatabase();

        Cursor cursor=db.rawQuery("select punts, nom from vpuntuacions order by punts DESC LIMIT "+quantitat,null);
        while (cursor.moveToNext()){
            result.add(cursor.getInt(0)+" "+cursor.getString(1));
        }
        cursor.close();
        db.close();
        return result;
    }
}
