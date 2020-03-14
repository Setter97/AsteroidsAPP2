package com.example.aplicacio1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ServeiMusica extends Service {

    MediaPlayer reproductor;
    private static final int ID_NOTIFICACIO_CREAR=1;

    @Override
    public void onCreate(){
        super.onCreate();
        //Toast.makeText(this,"Servei creat",Toast.LENGTH_SHORT).show();
        reproductor=MediaPlayer.create(this,R.raw.audio);
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int idArranc){
        super.onStartCommand(intent,flags,idArranc);

        Notification.Builder notificacio=new Notification.Builder(this)
                .setContentTitle("Creant Servei de Musica")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setWhen(System.currentTimeMillis()+1000*60*60)
                .setContentInfo("mes info")
                .setTicker("Text en barra d'estat");

        NotificationManager nm;
        nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(ID_NOTIFICACIO_CREAR,notificacio.build());

        PendingIntent intencioPendent=PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
        notificacio.setContentIntent(intencioPendent);

        //Toast.makeText(this,"Servei arrancat "+idArranc,Toast.LENGTH_SHORT).show();
        reproductor.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        NotificationManager nm;
        nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(ID_NOTIFICACIO_CREAR);
        //Toast.makeText(this,"Servei aturat",Toast.LENGTH_SHORT).show();
        reproductor.stop();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
