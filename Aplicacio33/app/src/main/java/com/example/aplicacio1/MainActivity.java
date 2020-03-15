package com.example.aplicacio1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private Button bAcercaDe;
    private Button bExit;
    private Button bConf;
    private Button bPlay;


    public static MagatzemPuntuacions magatzem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Inicialitzacio del objecte magatzem;------------------------------------------------------

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);

        boolean musica=pref.getBoolean("musica",false);
        if(musica){
            startService(new Intent(MainActivity.this,ServeiMusica.class));
        }else{
            stopService(new Intent(MainActivity.this,ServeiMusica.class));
        }
        switch (pref.getString("tipus_mag","0")){
            case "0":magatzem=new MagatzemPuntuacionsPreferencies(this);break;
            case "1":magatzem=new MagatzemPuntuacionsFitxerIntern(this);break;
            case "2":magatzem=new MagatzemPuntuacionsFitxerExtern(this);break;
            case "3":magatzem=new MagatzemPuntuacionsSQLite(this);break;
            case "4": magatzem=new MagatzemPuntuacionsProvider(this);break;
            case "5": magatzem=new MagatzemPuntuacionsSocket();break;
            case "6": magatzem=new MagatzemPuntuacionsXML_SAX(this);break;
            case "7": magatzem=new MagatzemPuntuacionsGson();break;
            case "8": magatzem=new MagatzemPuntuaciosJson();break;
            default:magatzem=new MagatzemPuntuacionsPreferencies(this);break;
        }

        bAcercaDe=findViewById(R.id.btnacerca);
        bAcercaDe.setOnClickListener(this);

        bExit=findViewById(R.id.btnsalir);
        bExit.setOnClickListener(this);
        bExit.setOnLongClickListener(this);

        bConf=findViewById(R.id.btnconf);
        bConf.setOnClickListener(this);
        bConf.setOnLongClickListener(this);

        bPlay=findViewById(R.id.btnjugar);
        bPlay.setOnClickListener(this);

        //Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT).show();

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
        startActivityForResult(i,1234);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==1234 && resultCode==RESULT_OK && data!=null){
            final int puntuacio=data.getExtras().getInt("puntuacio");


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Introdueix el teu nom");

            final EditText editText=new EditText(MainActivity.this);
            editText.setText("");
            builder.setView(editText);
            builder.setPositiveButton("Confirma", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String nom=editText.getText().toString();
                    //Toast.makeText(MainActivity.this,nom,Toast.LENGTH_LONG).show();
                    magatzem.guardarPuntuacions(puntuacio,nom,System.currentTimeMillis());
                    llancarPuntuacions(null);
                }
            });

            builder.setNegativeButton("Denega", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(MainActivity.this,"adeu",Toast.LENGTH_LONG).show();
                    String nom="Jugador 1";
                    magatzem.guardarPuntuacions(puntuacio,nom,System.currentTimeMillis());
                    llancarPuntuacions(null);
                }
            });

            builder.show();

        }
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
        //Toast.makeText(this,"onStart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume(){
        //mp.start();
        super.onResume();
        //Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause(){
        //mp.pause();
        super.onPause();
        //Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop(){
        //pos=mp.getCurrentPosition();
        super.onStop();
        //Toast.makeText(this,"onStop",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart(){
        //mp.start();
        super.onRestart();
        //Toast.makeText(this,"onRestart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        stopService(new Intent(MainActivity.this,ServeiMusica.class));
        //mp.stop();
        super.onDestroy();
        //Toast.makeText(this,"onDestroy",Toast.LENGTH_SHORT).show();
    }
}
