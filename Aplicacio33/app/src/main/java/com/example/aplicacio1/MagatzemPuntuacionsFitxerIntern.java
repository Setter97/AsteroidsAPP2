package com.example.aplicacio1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class MagatzemPuntuacionsFitxerIntern implements MagatzemPuntuacions {

    private static String FITXER="puntuacions.txt";
    private Context context;

    public MagatzemPuntuacionsFitxerIntern(Context context){
        this.context=context;
    }
    @Override
    public void guardarPuntuacions(int punts, String nom, long data) {
        try{
            FileOutputStream f=context.openFileOutput(FITXER,Context.MODE_APPEND);
            String text=punts+" "+nom+"\n";
            f.write(text.getBytes());
            f.close();
        }catch (Exception e){
            Log.e("Asteroides",e.getMessage(),e);
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result=new Vector<>();
        try{
            FileInputStream f=context.openFileInput(FITXER);
            BufferedReader entrada=new BufferedReader(new InputStreamReader(f));

            int n=0;
            String linia="";

            do{
                linia=entrada.readLine();
                if(linia!=null){
                    result.add(linia);
                    n++;
                }

            }while (n<quantitat && linia!=null);
            f.close();

        }catch (Exception e){
            Log.e("Asteroides",e.getMessage(),e);
        }

        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] a=o1.split(" ");
                String[] b=o2.split(" ");
                //Toast.makeText(context,b[0],Toast.LENGTH_SHORT).show();
                int uno=Integer.parseInt(a[0]);
                int dos=Integer.parseInt(b[0]);

                return Integer.compare(uno,dos);
            }
        });

        return result;
    }
}
