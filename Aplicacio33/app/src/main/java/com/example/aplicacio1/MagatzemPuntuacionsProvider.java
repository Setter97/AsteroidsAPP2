package com.example.aplicacio1;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.util.Vector;

public class MagatzemPuntuacionsProvider implements MagatzemPuntuacions {
    private Activity activity;
    public MagatzemPuntuacionsProvider(Activity activity){
        this.activity=activity;
    }
    @Override
    public void guardarPuntuacions(int punts, String nom, long data) {
        Uri uri=Uri.parse("content://com.example.eje11_3/puntuacions");
        ContentValues valors=new ContentValues();

        valors.put("nom",nom);
        valors.put("punts",punts);
        valors.put("data",data);

        try{
            activity.getContentResolver().insert(uri,valors);

        }catch (Exception e){
            Toast.makeText(activity,"Verificar que esta instalada",Toast.LENGTH_SHORT).show();
            Log.e("Asteroides",e.toString(),e);
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result=new Vector<>();
        Uri uri=Uri.parse("content://com.example.eje11_3/puntuacions");
        Cursor cursor=activity.getContentResolver().query(uri,null,null,null,"data DESC");

        if(cursor!=null){
            while(cursor.moveToNext()){
                String nom=cursor.getString(cursor.getColumnIndex("nom"));
                int punts=cursor.getInt(cursor.getColumnIndex("punts"));
                result.add(punts+" "+nom);
            }
            cursor.close();
        }
        return result;
    }
}
