package com.example.aplicacio1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Vector;

public class AdaptadorPuntuacions extends RecyclerView.Adapter<AdaptadorPuntuacions.ViewHolder> {
    private LayoutInflater inflador;
    private Vector<String> llista;
    private int cont=0;
    private Context context;
    protected View.OnClickListener onClickListener;

    public AdaptadorPuntuacions(Context context, Vector<String> llista) {
        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.llista = llista;
        this.context=context;
    }
    public void setOnClickListener(View.OnClickListener onClickListener){
        this.onClickListener=onClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titol,subtitol;
        public ImageView icono;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titol=(TextView)itemView.findViewById(R.id.titol);
            subtitol=itemView.findViewById(R.id.subtitol);
            icono=itemView.findViewById(R.id.icono);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=inflador.inflate(R.layout.element_puntuacio,parent,false);
        //Toast.makeText(context,"Cont"+(++cont),Toast.LENGTH_SHORT).show();
        v.setOnClickListener(onClickListener);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titol.setText(llista.get(position));
        switch (Math.round((float)Math.random()*3)){
        case 0:
            holder.icono.setImageResource(R.drawable.asteroide1);
            break;
        case 1:
            holder.icono.setImageResource(R.drawable.asteroide2);
            break;
        default:
            holder.icono.setImageResource(R.drawable.asteroide3);
            break;
}
    }

    @Override
    public int getItemCount() {
        return llista.size();
    }


}
