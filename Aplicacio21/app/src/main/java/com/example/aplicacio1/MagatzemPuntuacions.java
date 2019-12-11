package com.example.aplicacio1;

import java.util.Vector;

public interface MagatzemPuntuacions {
    public void guardarPuntuacions(int punts,String nom,long data);
    public Vector<String>llistaPuntuacions(int quantitat);
}
