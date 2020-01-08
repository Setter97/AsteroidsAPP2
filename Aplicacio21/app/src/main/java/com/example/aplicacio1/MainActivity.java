package com.example.aplicacio1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private Button bAcercaDe;
    private Button bExit;
    private Button bConf;
    private Button bPlay;
    MediaPlayer mp;
    public static MagatzemPuntuacions magatzem=new MagatzemPuntuacionsArray();
    int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAcercaDe=findViewById(R.id.btnacerca);
        /*bAcercaDe.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                llancarAcercaDe(null);
            }
        });
         */
        bAcercaDe.setOnClickListener(this);

        bExit=findViewById(R.id.btnsalir);
        bExit.setOnClickListener(this);
        bExit.setOnLongClickListener(this);
       // bExit.setBackgroundResource(R.drawable.degradat);

        /*bExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
         */

        /*bExit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               llancarPuntuacions(null);
                return true;
            }
        });
         */

        bConf=findViewById(R.id.btnconf);
        bConf.setOnClickListener(this);
        bConf.setOnLongClickListener(this);
        /*
        bConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llancarPreferencies(null);
            }
        });

         */

       /* bConf.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mostrarPreferencies();
                return true;
            }
        });

        */

       bPlay=findViewById(R.id.btnjugar);
       bPlay.setOnClickListener(this);
       /*
       bPlay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               llancarJoc(null);
           }
       });

        */
       Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT).show();
       mp=MediaPlayer.create(this,R.raw.audio);
       if(pos>0){
            mp.seekTo(pos);
            pos=0;
       }else{
           mp.start();
       }
    }

    public void llancarPreferencies(View view){
        Intent i=new Intent(this,Preferencies.class);
        startActivity(i);
    }
    public void mostrarPreferencies() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String conf = "Musica: " + pref.getBoolean(getString(R.string.pa1_key), false)
                +"\nTipus grafics: "+pref.getString(getString(R.string.pa2_key),"?")
                +"\nNumero de fragments: "+pref.getString(getString(R.string.pa3_key),"?")
                +"\nActivar multijugador: "+pref.getBoolean(getString(R.string.pa4_key),true)
                +"\nMaxim de jugadors: "+pref.getString(getString(R.string.pa5_key),"?")
                +"\nTipus connexio: "+pref.getString(getString(R.string.pa6_key),"?")
                ;

        Toast.makeText(this, conf, Toast.LENGTH_SHORT).show();
    }

    public void llancarAcercaDe(View view){
        //Toast.makeText(this,"ha entrat",Toast.LENGTH_SHORT).show();
        //mp.pause();
        Intent i=new Intent(this,AcercaDe.class);
        startActivity(i);
    }
    public void llancarPuntuacions(View view){
        Intent i=new Intent(this,Puntuacions.class);
        startActivity(i);
    }

    public void llancarJoc(View view){
        Intent i=new Intent(this,Joc.class);
        startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater infl=getMenuInflater();
        infl.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id==R.id.config){
            llancarPreferencies(null);
            return true;
        }
        if(id==R.id.acercaDe){
            llancarAcercaDe(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnacerca:llancarAcercaDe(null);break;
            case R.id.btnconf:llancarPreferencies(null);break;
            case R.id.btnjugar:llancarJoc(null);break;
            case R.id.btnsalir:finish();break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        
        if(v.getId()==R.id.btnsalir){
            llancarPuntuacions(null);
            return true;
        }else if(v.getId()==R.id.btnconf){
            mostrarPreferencies();
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected void onStart(){
        //mp.start();
        super.onStart();
        Toast.makeText(this,"onStart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume(){
        mp.start();
        super.onResume();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause(){
        mp.pause();
        super.onPause();
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop(){
        pos=mp.getCurrentPosition();
        super.onStop();
        Toast.makeText(this,"onStop",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart(){
        mp.start();
        super.onRestart();
        Toast.makeText(this,"onRestart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){

        mp.stop();
        super.onDestroy();
        Toast.makeText(this,"onDestroy",Toast.LENGTH_SHORT).show();
    }
}
