package com.example.aplicacio1;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.google.gson.Gson;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Vector;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MagatzemPuntuacionsGson implements MagatzemPuntuacions {
    private String string;
    private Gson gson = new Gson();
    private Type type = new TypeToken<Clase>(){}.getType();

    public MagatzemPuntuacionsGson(){
        //guardarPuntuacion(4500,"Mi nombre",System.currentTimeMillis());
        //guardarPuntuacion(3100,"Otro nombre", System.currentTimeMillis());
    }

    @Override
    public void guardarPuntuacions(int punts, String nom, long data) {
        Clase obj;
        if (string == null){
            obj = new Clase();
        }else{
            obj = gson.fromJson(string,type);
        }

        obj.puntuacios.add(new Puntuacio(punts,nom,data));
        string = gson.toJson(obj, type);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        Clase obj;
        if (string == null){
            obj = new Clase();
        }else{
            obj = gson.fromJson(string, type);
        }
        Vector<String> salida = new Vector<>();
        for (Puntuacio puntuacio: obj.puntuacios){
            salida.add(puntuacio.getPunts()+" "+puntuacio.getNom());
        }
        return salida;
    }

    public class Clase{
        private ArrayList<Puntuacio> puntuacios = new ArrayList<>();
        private boolean guardad;
    }
}