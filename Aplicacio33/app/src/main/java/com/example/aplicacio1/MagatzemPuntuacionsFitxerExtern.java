package com.example.aplicacio1;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class MagatzemPuntuacionsFitxerExtern implements MagatzemPuntuacions {



    private Context context;
    private static String FITXER ;

    public MagatzemPuntuacionsFitxerExtern(Context context) {
        this.context = context;
    }

    @Override
    public void guardarPuntuacions(int punts, String nom, long data) {
        FITXER=context.getFilesDir().toString()+"/puntuacions.txt";
        try {
            //Toast.makeText(context,FITXER,Toast.LENGTH_LONG).show();
            FileOutputStream f = new FileOutputStream(FITXER,true);
            String text = punts + " " + nom + "\n";
            f.write(text.getBytes());
            f.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result = new Vector<>();
        //FITXER=context.getExternalFilesDir()
        Toast.makeText(context,FITXER,Toast.LENGTH_LONG).show();
        try {
            FileInputStream f =new FileInputStream(FITXER);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(f));

            int n = 0;
            String linia;

            do {
                linia = entrada.readLine();
                if (linia != null) {
                    result.add(linia);
                    n++;
                }

            } while (n < quantitat && linia != null);
            f.close();

        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }

        Collections.sort(result, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] a = o1.split(" ");
                String[] b = o2.split(" ");
                //Toast.makeText(context,b[0],Toast.LENGTH_SHORT).show();
                int uno = Integer.parseInt(a[0]);
                int dos = Integer.parseInt(b[0]);

                return Integer.compare(uno, dos);
            }
        });

        return result;
    }


}
