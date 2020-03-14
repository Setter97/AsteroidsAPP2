package com.example.aplicacio1;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

public class MagatzemPuntuacionsPreferencies implements MagatzemPuntuacions {
    private static String PREFERENCIES="puntuacions";
    private Context context;

    public MagatzemPuntuacionsPreferencies(Context context){
        this.context=context;
    }
    @Override
    public void guardarPuntuacions(int punts, String nom, long data) {
        SharedPreferences preferences=context.getSharedPreferences(PREFERENCIES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("puntuacio",punts+" "+nom);
        editor.apply();
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String>result=new Vector<String>();
        SharedPreferences preferences=context.getSharedPreferences(PREFERENCIES,Context.MODE_PRIVATE);
        String s=preferences.getString("puntuacio","");
        if(s!=""){
            result.add(s);
        }
        return result;
    }


}
