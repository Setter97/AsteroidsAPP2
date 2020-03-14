package com.example.aplicacio1;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Puntuacions extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AdaptadorPuntuacions adaptador;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puntuacions);
        recyclerView=findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptador=new AdaptadorPuntuacions(this,MainActivity.magatzem.llistaPuntuacions(10));
        recyclerView.setAdapter(adaptador);
        adaptador.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                int pos=recyclerView.getChildAdapterPosition(v);
                String s=MainActivity.magatzem.llistaPuntuacions(10).get(pos);
                Toast.makeText(Puntuacions.this,"Seleccio: "+pos+" - "+s,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
