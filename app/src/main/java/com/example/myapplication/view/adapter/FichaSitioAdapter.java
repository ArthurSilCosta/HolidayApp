package com.example.myapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.FichaSitio;

import java.util.List;

public class FichaSitioAdapter extends RecyclerView.Adapter<FichaSitioAdapter.ViewHolder> {

    private final List<FichaSitio> lista;
    private final OnDeleteListener listener;

    public interface OnDeleteListener {
        void onDelete(FichaSitio ficha);
    }

    public FichaSitioAdapter(List<FichaSitio> lista, OnDeleteListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ficha_sitio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FichaSitio ficha = lista.get(position);
        holder.txtNome.setText("Sítio: " + ficha.getSitio());
        holder.txtLocalizacao.setText("Localização: " + ficha.getLocalizacao());

        holder.btnExcluir.setOnClickListener(v -> listener.onDelete(ficha));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome, txtLocalizacao;
        Button btnExcluir;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txt_nome_sitio);
            txtLocalizacao = itemView.findViewById(R.id.txt_localizacao_sitio);
            btnExcluir = itemView.findViewById(R.id.btn_excluir_sitio);
        }
    }
}
