package com.example.aplicacio1;

import java.util.Vector;

public class MagatzemPuntuacionsArray implements MagatzemPuntuacions {
    private Vector<String> puntuacions;

    public MagatzemPuntuacionsArray(){
        puntuacions=new Vector<String>();
        puntuacions.add("123000 Asdadadsd");
        puntuacions.add("873624 Pepep");
        puntuacions.add("123000 popopo");
        puntuacions.add("981233 ldfka");
        puntuacions.add("472973 aslkdql");
        puntuacions.add("294281 mselk");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
        puntuacions.add("123200 lqwj");
    }
    @Override
    public void guardarPuntuacions(int punts, String nom, long data) {
        puntuacions.add(0,punts+" "+nom);
    }

    @Override
    public Vector<String> llistaPuntuacions(int quantitat) {
        return puntuacions;
    }
}
