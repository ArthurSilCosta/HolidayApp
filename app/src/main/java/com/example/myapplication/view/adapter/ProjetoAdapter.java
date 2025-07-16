package com.example.myapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Projeto;

import java.util.List;

public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.ViewHolder> {

    public interface OnProjetoClickListener {
        void onClick(Projeto projeto);
    }

    private final List<Projeto> lista;
    private final OnProjetoClickListener listener;

    public ProjetoAdapter(List<Projeto> lista, OnProjetoClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProjetoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_projeto, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjetoAdapter.ViewHolder holder, int position) {
        Projeto projeto = lista.get(position);
        holder.txtNome.setText(projeto.getNome());
        holder.itemView.setOnClickListener(v -> listener.onClick(projeto));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txt_nome_projeto);
        }
    }
}
