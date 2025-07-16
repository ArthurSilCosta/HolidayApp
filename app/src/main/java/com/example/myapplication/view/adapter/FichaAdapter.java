package com.example.myapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Ficha;

import java.util.List;

public class FichaAdapter extends RecyclerView.Adapter<FichaAdapter.FichaViewHolder> {

    private List<Ficha> listaFichas;

    public FichaAdapter(List<Ficha> listaFichas) {
        this.listaFichas = listaFichas;
    }

    @NonNull
    @Override
    public FichaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ficha, parent, false);
        return new FichaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FichaViewHolder holder, int position) {
        Ficha ficha = listaFichas.get(position);
        holder.txtTitulo.setText(ficha.getSitio() != null ? ficha.getSitio() : "(Sem nome)");
    }

    @Override
    public int getItemCount() {
        return listaFichas.size();
    }

    public static class FichaViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo;

        public FichaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_ficha);
        }
    }
}
