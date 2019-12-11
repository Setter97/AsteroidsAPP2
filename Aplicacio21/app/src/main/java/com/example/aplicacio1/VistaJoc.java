package com.example.aplicacio1;

import android.content.Context;
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
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

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
    public VistaJoc(Context context, AttributeSet attrs){
        super(context,attrs);

        setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        Drawable drawableNau,drawableAsteroide,drawableMissil;
        //drawableAsteroide=context.getResources().getDrawable(R.drawable.asteroide1);

        asteroides=new Vector<>();
        missils=new Vector<>();
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getContext());
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

            ShapeDrawable dAsteroide=new ShapeDrawable(new PathShape(pathAsteroide,1,1));
            dAsteroide.getPaint().setColor(Color.WHITE);
            dAsteroide.getPaint().setStyle(Paint.Style.STROKE);
            dAsteroide.setIntrinsicHeight(100);
            dAsteroide.setIntrinsicWidth(100);
            drawableAsteroide=dAsteroide;
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
            drawableAsteroide=context.getResources().getDrawable(R.drawable.asteroide1);
            drawableNau=getResources().getDrawable(R.drawable.nau);
            drawableMissil=getResources().getDrawable(R.drawable.missil1);
        }

        for(int i=0;i<numAsteroides;i++){
            Grafic asteroide=new Grafic(this,drawableAsteroide);
            asteroide.setIncY(Math.random()*4-2);
            asteroide.setIncX(Math.random()*4-2);
            asteroide.setAngle((int)(Math.random()*360));
            asteroide.setRotacio((int)(Math.random()*8-4));
            asteroides.add(asteroide);
        }

        for(int i=0;i<2000;i++){
            missils.add(new Grafic(this,drawableMissil));
        }



        nau=new Grafic(this,drawableNau);

        SensorManager mSensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> llistaSensor=mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if(!llistaSensor.isEmpty()){
            Sensor orientation=llistaSensor.get(0);
            mSensorManager.registerListener(this,orientation,SensorManager.SENSOR_DELAY_GAME);
        }


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
            while (true) {
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
        asteroides.remove(i);
        //missilActiu=false;

    }

    private void activaMissil(int i){

        missils.get(i).setCenX(nau.getCenX());
        missils.get(i).setCenY(nau.getCenY());
        missils.get(i).setAngle(nau.getAngle());
        missils.get(i).setIncX(Math.cos(Math.toRadians( missils.get(i).getAngle()))*PAS_VELOCITAT_MISSIL);
        missils.get(i).setIncY(Math.sin(Math.toRadians( missils.get(i).getAngle()))*PAS_VELOCITAT_MISSIL);
        tempsMissil=(int)Math.min(this.getWidth()/Math.abs( missils.get(i).getIncX()),
                this.getHeight()/Math.abs( missils.get(i).getIncY()))-2;
        missilActiu=true;
    }
}
