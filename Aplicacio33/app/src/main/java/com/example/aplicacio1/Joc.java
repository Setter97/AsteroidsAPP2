package com.example.aplicacio1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Joc extends Activity {
    private VistaJoc vistaJoc;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joc);
        vistaJoc=(VistaJoc)findViewById(R.id.VistaJoc);
        vistaJoc.setVistaVictoria(findViewById(R.id.Victoria));
        vistaJoc.setVistaDerrota(findViewById(R.id.Derrota));
        vistaJoc.setPare(this);

    }


    @Override
    protected void onPause(){
        super.onPause();
        vistaJoc.getFil().pausar();
    }

    @Override
    protected void onResume(){
        super.onResume();
        vistaJoc.getFil().reanudar();
    }
    @Override
    protected void onDestroy(){
        super.onPause();
        vistaJoc.getFil().aturar();
        super.onDestroy();
    }
}
