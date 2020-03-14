package com.example.aplicacio1;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

public class MagatzemPuntuacionsSocket implements MagatzemPuntuacions {
    String ip="depinframis.no-ip.biz";
    int port=4321;

    public MagatzemPuntuacionsSocket(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }
    @Override
    public void guardarPuntuacions(int punts, String nom, long data) {
        try{
            Socket sk=new Socket(ip,port);

            BufferedReader entrada=new BufferedReader(new InputStreamReader(sk.getInputStream()));

            PrintWriter sortida=new PrintWriter(new OutputStreamWriter(sk.getOutputStream()));

            sortida.println(punts+" "+nom);

            String resposta=entrada.readLine();
        if(!resposta.equals("OK")){
            Log.e("Asteroides","Error: resposta del servidor incorrecte");
        }
        sk.close();
        }catch (IOException e){
            Log.e("Asteroides",e.toString());
        }
    }


    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Vector<String> result=new Vector<>();

        try{
            Socket sk=new Socket(ip,port);

            BufferedReader entrada=new BufferedReader(new InputStreamReader(sk.getInputStream()));

            PrintWriter sortida=new PrintWriter(new OutputStreamWriter(sk.getOutputStream()),true);

            sortida.println("PUNTS");

            int n=0;
            String resposta;
            do{
                resposta=entrada.readLine();
                if(resposta!=null){
                    result.add(resposta);
                    n++;
                }
            }while(n<quantitat && resposta!=null);
            sk.close();

        }catch (IOException e){
            Log.e("Asteroides",e.toString(),e);
        }
        return result;
    }
}
