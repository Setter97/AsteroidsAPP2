package com.example.aplicacio1;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

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

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MagatzemPuntuacionsXML_SAX implements MagatzemPuntuacions {

    // nombre del fichero donde se guarda los datos
    // data/data/com.example.aplicacio1/files
    private static String FITXER = "puntuacions.xml";
    private Context context;
    //para guardar la informacion leida del fichero
    private LlistaPuntuacions llista;
    // indica si la  variable lista ya ha sido leida desde el fichero
    private boolean carregaLlista;

    public MagatzemPuntuacionsXML_SAX(Context context){
        this.context = context;
        llista = new LlistaPuntuacions();
        carregaLlista = false;
    }

    @Override
    public void guardarPuntuacions(int punts, String nom, long data) {
        try{
            //comprueba si la variable lista tiene datos
            if(!carregaLlista){
                llista.llegirXML(context.openFileInput(FITXER));
            }
        }catch (FileNotFoundException e){
            // Si es la primera vez el archivo no existira y lanza la excepcion
            // esta excepcion, pero no pasa nada, no realiza ninguna accion
            Log.e("Asteroides",e.getMessage(),e);
        }catch (Exception e){
            Log.e("Asteroides",e.getMessage(),e);
        }
        // añadir la nueva puntuacion a la lista
        llista.nou(punts,nom,data);
        try{
            // escribir de nuevo toda la informacion de la lista al fichero
            llista.escriureXML(context.openFileOutput(FITXER, Context.MODE_PRIVATE));
        }catch (Exception e){
            Log.e("Asteroides",e.getMessage(),e);
        }
    }



    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        try{
            // Comprovar si la variable lista tiene  los datos
            if (!carregaLlista){
                // Lee datos del fichero XML
                llista.llegirXML(context.openFileInput(FITXER));
            }
        } catch (Exception e){
            Log.e("Asteroides",e.getMessage(),e);
        }
        return llista.aVectorString();
    }

    private class LlistaPuntuacions {
        //Una clase interna
        private class Puntuacio{
            int punts;
            String nom;
            long data;
        }

        class ManejadorXML extends DefaultHandler {
            private StringBuilder cadena;
            private Puntuacio puntuacio;

            @Override
            //Inicializa variables
            public void startDocument() throws SAXException {
                llistaPuntuacions = new ArrayList<>();
                cadena = new StringBuilder();
            }

            @Override
            public void startElement(String uri, String nomLocal, String nomQualif, Attributes atr) throws SAXException{
                cadena.setLength(0);
                if (nomLocal.equals("puntuacio")){
                    puntuacio = new Puntuacio();
                    puntuacio.data = Long.parseLong(atr.getValue("data"));
                }
            }

            @Override
            public void characters(char[] ch, int inici, int lon) throws SAXException{
                cadena.append(ch,inici,lon);
            }

            @Override
            public void endElement(String uri, String nomlocal, String nomQualif) throws SAXException{
                if (nomlocal.equals("punts")){
                    puntuacio.punts = Integer.parseInt(cadena.toString());
                }else if (nomlocal.equals("nom")){
                    puntuacio.nom = cadena.toString();
                }else if (nomlocal.equals("puntuacio")){
                    llistaPuntuacions.add(puntuacio);
                }
            }
        }

        // variables que realmente contiene los datos XML
        private List<Puntuacio> llistaPuntuacions;

        public LlistaPuntuacions(){
            llistaPuntuacions = new ArrayList<>();
        }

        // afegeix una nova puntuacion a la llista
        public void nou(int punts, String nom, long data){
            Puntuacio puntuacio = new Puntuacio();
            puntuacio.punts = punts;
            puntuacio.nom = nom;
            puntuacio.data = data;
            llistaPuntuacions.add(puntuacio);
        }

        // extraer la informacion que interessa de la lista y contruir un vector de strings con la informacion
        public Vector<String> aVectorString(){
            Vector<String> result = new Vector<>();
            for(Puntuacio puntuacio: llistaPuntuacions){
                result.add(puntuacio.nom+" "+puntuacio.punts);
            }
            return result;
        }

        public void llegirXML (InputStream entrada) throws  Exception{
            SAXParserFactory fabrica = SAXParserFactory.newInstance();
            SAXParser parser = fabrica.newSAXParser();
            XMLReader lector = parser.getXMLReader();
            ManejadorXML manejadorXML = new ManejadorXML();
            lector.setContentHandler(manejadorXML);
            lector.parse(new InputSource(entrada));
            carregaLlista = true;
        }

        public void escriureXML(OutputStream sortida){
            XmlSerializer serializer = Xml.newSerializer();
            try{
                serializer.setOutput(sortida, "UTF-8");
                serializer.startDocument("UTF-8",true);
                serializer.startTag("","llista_puntuacions");
                for (Puntuacio puntuacio:llistaPuntuacions){
                    serializer.startTag("","puntuacio");
                    serializer.attribute("","data",String.valueOf(puntuacio.data));
                    serializer.startTag("","nom");
                    serializer.text(puntuacio.nom);
                    serializer.endTag("","nom");
                    serializer.startTag("","punts");
                    serializer.text(String.valueOf(puntuacio.punts));
                    serializer.endTag("","punts");
                    serializer.endTag("","puntuacio");
                }
                serializer.endTag("","llista_puntuacions");
                serializer.endDocument();
            }catch (Exception e){
                Log.e("Asteroid",e.getMessage(),e);
            }
        }

    }
}