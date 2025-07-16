package com.example.myapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.model.FichaAnotacao;
import java.util.List;

public class FichaAnotacaoAdapter extends RecyclerView.Adapter<FichaAnotacaoAdapter.ViewHolder> {

    private final List<FichaAnotacao> lista;
    private final OnDeleteListener listener;

    public interface OnDeleteListener {
        void onDelete(FichaAnotacao ficha);
    }

    public FichaAnotacaoAdapter(List<FichaAnotacao> lista, OnDeleteListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ficha_anotacao, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FichaAnotacao ficha = lista.get(position);
        holder.txtTitulo.setText("Título: " + ficha.getTitulo());
        holder.txtData.setText("Data: " + ficha.getDataCriacao());
        holder.btnExcluir.setOnClickListener(v -> listener.onDelete(ficha));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtData;
        Button btnExcluir;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_anotacao);
            txtData = itemView.findViewById(R.id.txt_data_anotacao);
            btnExcluir = itemView.findViewById(R.id.btn_excluir_anotacao);
        }
    }
}
