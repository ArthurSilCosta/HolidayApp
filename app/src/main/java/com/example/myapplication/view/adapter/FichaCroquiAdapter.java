
package com.example.myapplication.view.adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.FichaCroqui;

import java.util.List;

public class FichaCroquiAdapter extends RecyclerView.Adapter<FichaCroquiAdapter.ViewHolder> {

    private final List<FichaCroqui> lista;
    private final OnDeleteListener listener;

    public interface OnDeleteListener {
        void onDelete(FichaCroqui ficha);
    }

    public FichaCroquiAdapter(List<FichaCroqui> lista, OnDeleteListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FichaCroquiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ficha_croqui, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FichaCroquiAdapter.ViewHolder holder, int position) {
        FichaCroqui ficha = lista.get(position);

        holder.txtTitulo.setText("Título: " + ficha.getTitulo());
        holder.txtAnotacao.setText("Anotação: " + ficha.getAnotacao());

        // Carregar imagem do caminho salvo
        if (ficha.getCaminhoImagem() != null) {
            holder.imgCroqui.setImageBitmap(BitmapFactory.decodeFile(ficha.getCaminhoImagem()));
        }

        holder.btnExcluir.setOnClickListener(v -> listener.onDelete(ficha));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtAnotacao;
        ImageView imgCroqui;
        Button btnExcluir;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_croqui);
            txtAnotacao = itemView.findViewById(R.id.txt_anotacao_croqui);
            imgCroqui = itemView.findViewById(R.id.img_croqui);
            btnExcluir = itemView.findViewById(R.id.btn_excluir_croqui);
        }
    }
}
