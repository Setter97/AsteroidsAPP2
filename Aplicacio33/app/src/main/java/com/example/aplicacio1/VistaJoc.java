package com.example.aplicacio1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class VistaJoc extends View implements SensorEventListener {
    private Vector<Grafic>asteroides;
    private int numAsteroides=5;
    private int numFragments=3;
    private Grafic nau;
    private int girNau;
    private double acceleracioNau;
    private static final int MAX_VELOCITAT_NAU=20;

    private static final int PAS_GIR_GRAU=5;

    private static final float PAS_ACCELERACIO_NAU=0.5f;
    private VistaJoc vistaJoc;
    private ThreadJoc fil=new ThreadJoc();
    private static int PERIODE_PROCES=50;
    private long darrerProces=0;

    private float mX=0, mY=0;
    private boolean dispar=false;

    private boolean hihaValorInicial=false;
    private float valorInicial=0;

    public ThreadJoc getFil() {
        return fil;
    }

    private Vector<Grafic>missils;
    private Vector<Integer>tempsMissils;

    private static int PAS_VELOCITAT_MISSIL=12;
    private boolean missilActiu=false;
    private int tempsMissil;
    private  int numMissil=0;
    private SoundPool soundPool;
    int idDispar,idExplosio;
    private Drawable drawableAsteroide[]=new Drawable[numFragments];
    private int puntuacio=0;


    private static final int ESTAT_JUGANT=0;
    public static final int ESTAT_VICTORIA=1;
    public static final int ESTAT_DERROTA=2;

    private int estat=ESTAT_JUGANT;
    private View vistaDerrota;
    private View vistaVictoria;


    private Activity pare;

    public VistaJoc(Context context, AttributeSet attrs){
        super(context,attrs);


        soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        idDispar=soundPool.load(context,R.raw.dispar,0);
        idExplosio=soundPool.load(context,R.raw.explosio,0);
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        Drawable drawableNau,drawableMissil;
        //drawableAsteroide=context.getResources().getDrawable(R.drawable.asteroide1);

        asteroides=new Vector<>();
        missils=new Vector<>();
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getContext());
        numFragments=Integer.parseInt(pref.getString("fragmentos","3"));

        if(pref.getString("graficos","1").equals("0")){
            Path pathAsteroide=new Path();
            pathAsteroide.moveTo((float)0.3,(float)0.0);
            pathAsteroide.lineTo((float)0.6,(float)0.0);
            pathAsteroide.lineTo((float)0.6,(float)0.3);
            pathAsteroide.lineTo((float)0.8,(float)0.2);
            pathAsteroide.lineTo((float)1.0,(float)0.4);
            pathAsteroide.lineTo((float)0.8,(float)0.6);
            pathAsteroide.lineTo((float)0.9,(float)0.9);
            pathAsteroide.lineTo((float)0.8,(float)1.0);
            pathAsteroide.lineTo((float)0.4,(float)1.0);
            pathAsteroide.lineTo((float)0.0,(float)0.6);
            pathAsteroide.lineTo((float)0.0,(float)0.2);
            pathAsteroide.lineTo((float)0.3,(float)0.0);


            //Asteroides----------------------------------------------------------------------------
            for(int i=0;i<numFragments;i++){
                ShapeDrawable dAsteroide=new ShapeDrawable(new PathShape(pathAsteroide,1,1));
                dAsteroide.getPaint().setColor(Color.WHITE);
                dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
                dAsteroide.setIntrinsicHeight(100);
                dAsteroide.setIntrinsicWidth(100);
                drawableAsteroide[i]=dAsteroide;

            }
            setBackgroundColor(Color.BLACK);

            //Nau-----------------------------------------------------------------------------------
            Path pathNau=new Path();
            pathNau.moveTo((float)0.0,(float)0.0);

            pathNau.lineTo((float)0.0,(float)0.0);
            pathNau.lineTo((float)1.0,(float)0.5);
            pathNau.lineTo((float)0.0,(float)1.0);
            pathNau.lineTo((float)0.0,(float)0.0);

            ShapeDrawable dNau=new ShapeDrawable(new PathShape(pathNau,1,1));
            dNau.getPaint().setColor(Color.WHITE);
            dNau.getPaint().setStyle(Paint.Style.STROKE);
            dNau.setIntrinsicWidth(75);
            dNau.setIntrinsicHeight(75);
            drawableNau=dNau;
            setBackgroundColor(Color.BLACK);

            ShapeDrawable dMissil=new ShapeDrawable(new RectShape());
            dMissil.getPaint().setColor(Color.WHITE);
            dMissil.getPaint().setStyle(Paint.Style.STROKE);
            dMissil.setIntrinsicHeight(3);
            dMissil.setIntrinsicWidth(15);
            drawableMissil=dMissil;

        }else{
            drawableAsteroide[0]=context.getResources().getDrawable(R.drawable.asteroide1);
            drawableAsteroide[1]=context.getResources().getDrawable(R.drawable.asteroide2);
            drawableAsteroide[2]=context.getResources().getDrawable(R.drawable.asteroide3);
            drawableNau=getResources().getDrawable(R.drawable.nau);
            drawableMissil=getResources().getDrawable(R.drawable.missil1);
        }

        for(int i=0;i<numAsteroides;i++){
            Grafic asteroide=new Grafic(this,drawableAsteroide[0]);
            asteroide.setIncY(Math.random()*4-2);
            asteroide.setIncX(Math.random()*4-2);
            asteroide.setAngle((int)(Math.random()*360));
            asteroide.setRotacio((int)(Math.random()*8-4));
            asteroides.add(asteroide);
        }

        for(int i=0;i<2000;i++){
            missils.add(new Grafic(this,drawableMissil));
//            tempsMissils.add(tempsMissil);
        }



        nau=new Grafic(this,drawableNau);

        SensorManager mSensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> llistaSensor=mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if(!llistaSensor.isEmpty()){
            Sensor orientation=llistaSensor.get(0);
            mSensorManager.registerListener(this,orientation,SensorManager.SENSOR_DELAY_GAME);
        }


    }

    public void setVistaDerrota(View vistaDerrota) {
        this.vistaDerrota = vistaDerrota;
    }

    public void setVistaVictoria(View vistaVictoria) {
        this.vistaVictoria = vistaVictoria;
    }

    @Override
    protected void onSizeChanged(int ample,int alt,int ample_ant,int alt_ant){
        super.onSizeChanged(ample,alt,ample_ant,alt_ant);

        nau.setCenX(1100);
        nau.setCenY(550);

        for(Grafic asteroide:asteroides){
            do{
                asteroide.setCenX((int)(Math.random()*ample));
                asteroide.setCenY((int)(Math.random()*alt));
            }while(asteroide.distancia(nau)<(ample+alt)/5);
        }

        darrerProces=System.currentTimeMillis();
        fil.start();

    }

    @Override
    synchronized protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(Grafic asteroide:asteroides){
            asteroide.dibuixaGrafic(canvas);
        }
        for(Grafic missil:missils) {
            missil.dibuixaGrafic(canvas);
        }
        nau.dibuixaGrafic(canvas);

        if(estat==ESTAT_VICTORIA){
            vistaVictoria.setVisibility(VISIBLE);
        }
        if(estat==ESTAT_DERROTA){
            vistaDerrota.setVisibility(VISIBLE);
        }

    }

    synchronized protected void actualitzaFisica(){
        long ara= System.currentTimeMillis();
        if(darrerProces+PERIODE_PROCES>ara){
            return;
        }

        double retard=(ara-darrerProces)/PERIODE_PROCES;
        darrerProces=ara;
        nau.setAngle((int)(nau.getAngle()+girNau*retard));

        double nIncX=nau.getIncX()+acceleracioNau*Math.cos(Math.toRadians(nau.getAngle()))*retard;
        double nIncY=nau.getIncY()+acceleracioNau*Math.sin(Math.toRadians(nau.getAngle()))*retard;

        if(Math.hypot(nIncX,nIncY)<=MAX_VELOCITAT_NAU){
            nau.setIncX(nIncX);
            nau.setIncY(nIncY);
        }
        nau.incrementaPos(retard);

        for(int i=0;i<asteroides.size();i++){
            asteroides.get(i).incrementaPos(retard);
        }

        if(missilActiu){
            double copiaRetart=retard;
            for(Grafic missil:missils){
                missil.incrementaPos(retard);
                
            }

            tempsMissil-=copiaRetart;

            if(tempsMissil<0){
                //missilActiu=false;
                missils.remove(missils.firstElement());
                //numMissil--;
            }else{

                for(int i=0;i<asteroides.size();i++){
                    int j=0;
                    for(Grafic missil:missils){
                        if(missil.verificaColisio(asteroides.get(i))){
                            destrueixAsteroide(i);
                            missils.remove(j);
                            //numMissil--;
                            j++;
                            break;
                        }j++;

                    }

                }
            }
        }

        for(Grafic asteroide:asteroides){
            if(asteroide.verificaColisio(nau)){
                estat=ESTAT_DERROTA;
                sortir();
            }
        }

    }

    public boolean onTouchEvent(MotionEvent mevent){
        super.onTouchEvent(mevent);

        float x=mevent.getX();
        float y=mevent.getY();

        switch (mevent.getAction()){
            case MotionEvent.ACTION_DOWN:
                dispar=true;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx=Math.abs(x-mX);
                float dy=Math.abs(y-mY);

                if(dy<6 && dx>6){
                    girNau=Math.round((x-mX)/2);
                    dispar=false;
                }else if(dx<6 && dy>6){
                    acceleracioNau=Math.round(mY-y)/25;
                    dispar=false;
                }
                break;

            case MotionEvent.ACTION_UP:
                girNau=0;
                acceleracioNau=0;
                if(dispar){
                    activaMissil(numMissil);
                    numMissil++;
                }
                break;
        }
        mX=x;
        mY=y;

        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float valor=event.values[1];
        if(!hihaValorInicial){
            valorInicial=valor;
            hihaValorInicial=true;
        }
        girNau=(int)(valorInicial-valor)/3;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class ThreadJoc extends Thread {
        private boolean pausa, corrent;



        public synchronized void pausar() {
            pausa = true;
        }

        public synchronized void reanudar() {
            pausa = false;
            notify();
        }

        public synchronized void  aturar() {
            corrent = false;
            if (pausa) reanudar();
        }

        @Override
        public void run() {
            corrent=true;
            while (corrent) {
                actualitzaFisica();
                synchronized (this) {
                    while (pausa) {
                        try {
                            wait();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
    }

    private void destrueixAsteroide(int i){
        int tam;
        puntuacio+=1000;

        if(asteroides.get(i).getDrawable()!=drawableAsteroide[2]){
            if(asteroides.get(i).getDrawable()==drawableAsteroide[1]){
                tam=2;
            }else{
                tam=1;
            }

            for(int n=0;n<numFragments;n++){
                Grafic asteroide=new Grafic(this,drawableAsteroide[tam]);
                asteroide.setCenX(asteroides.get(i).getCenX());
                asteroide.setCenY(asteroides.get(i).getCenY());
                asteroide.setIncX(Math.random()*7-2-tam);
                asteroide.setIncY(Math.random()*7-2-tam);
                asteroide.setAngle((int)(Math.random()*360));
                asteroide.setRotacio((int)(Math.random()*8-4));
                asteroides.add(asteroide);
            }
        }
        soundPool.play(idExplosio,1,1,0,0,1);
        asteroides.remove(i);
        //missilActiu=false;


        if(asteroides.isEmpty()){
            estat=ESTAT_VICTORIA;
            sortir();
        }

    }

    private void activaMissil(int i){
        soundPool.play(idDispar,1,1,1,0,1);
        missils.get(i).setCenX(nau.getCenX());
        missils.get(i).setCenY(nau.getCenY());
        missils.get(i).setAngle(nau.getAngle());
        missils.get(i).setIncX(Math.cos(Math.toRadians( missils.get(i).getAngle()))*PAS_VELOCITAT_MISSIL);
        missils.get(i).setIncY(Math.sin(Math.toRadians( missils.get(i).getAngle()))*PAS_VELOCITAT_MISSIL);
        tempsMissil=(int)Math.min(this.getWidth()/Math.abs( missils.get(i).getIncX()),
                this.getHeight()/Math.abs( missils.get(i).getIncY()))-2;
        missilActiu=true;
    }


    public void setPare(Activity pare){
        this.pare=pare;
    }

    private void sortir(){
        Bundle bundle=new Bundle();
        bundle.putInt("puntuacio",puntuacio);
        Intent intent=new Intent();
        intent.putExtras(bundle);
        pare.setResult(Activity.RESULT_OK,intent);
        pare.finish();
    }


}
