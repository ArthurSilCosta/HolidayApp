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
import com.example.myapplication.model.FichaImagem;

import java.io.File;
import java.util.List;

public class FichaImagemAdapter extends RecyclerView.Adapter<FichaImagemAdapter.ViewHolder> {

    private final List<FichaImagem> lista;
    private final OnDeleteListener listener;

    public interface OnDeleteListener {
        void onDelete(FichaImagem ficha);
    }

    public FichaImagemAdapter(List<FichaImagem> lista, OnDeleteListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ficha_imagem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FichaImagem ficha = lista.get(position);
        holder.txtLegenda.setText("Legenda: " + ficha.getLegenda());

        File imgFile = new File(ficha.getCaminhoImagem());
        if (imgFile.exists()) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        }

        holder.btnExcluir.setOnClickListener(v -> listener.onDelete(ficha));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtLegenda;
        ImageView imageView;
        Button btnExcluir;

        public ViewHolder(View itemView) {
            super(itemView);
            txtLegenda = itemView.findViewById(R.id.txt_legenda_foto);
            imageView = itemView.findViewById(R.id.img_foto);
            btnExcluir = itemView.findViewById(R.id.btn_excluir_foto);
        }
    }
}
