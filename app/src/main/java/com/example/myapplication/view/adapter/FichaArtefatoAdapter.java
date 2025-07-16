// FichaArtefatoAdapter.java
package com.example.myapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.FichaArtefato;

import java.util.List;

public class FichaArtefatoAdapter extends RecyclerView.Adapter<FichaArtefatoAdapter.ViewHolder> {

    private final List<FichaArtefato> lista;
    private final OnDeleteListener listener;

    public interface OnDeleteListener {
        void onDelete(FichaArtefato ficha);
    }

    public FichaArtefatoAdapter(List<FichaArtefato> lista, OnDeleteListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ficha_artefato, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FichaArtefato ficha = lista.get(position);
        holder.txtTipo.setText("Tipo: " + ficha.getTipoArtefato());
        holder.txtDescricao.setText("Descrição: " + ficha.getDescricaoArtefato());

        holder.btnExcluir.setOnClickListener(v -> listener.onDelete(ficha));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTipo, txtDescricao;
        Button btnExcluir;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTipo = itemView.findViewById(R.id.txt_tipo_artefato);
            txtDescricao = itemView.findViewById(R.id.txt_descricao_artefato);
            btnExcluir = itemView.findViewById(R.id.btn_excluir_artefato);
        }
    }
}
