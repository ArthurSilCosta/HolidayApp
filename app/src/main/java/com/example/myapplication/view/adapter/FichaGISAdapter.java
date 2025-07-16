package com.example.myapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.FichaGIS;

import java.util.List;

public class FichaGISAdapter extends RecyclerView.Adapter<FichaGISAdapter.ViewHolder> {

    private final List<FichaGIS> lista;
    private final OnDeleteListener listener;

    public interface OnDeleteListener {
        void onDelete(FichaGIS ficha);
    }

    public FichaGISAdapter(List<FichaGIS> lista, OnDeleteListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FichaGISAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ficha_gis, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FichaGISAdapter.ViewHolder holder, int position) {
        FichaGIS ficha = lista.get(position);
        holder.txtTipo.setText("Tipo: " + ficha.getTipoDado());
        holder.txtCoord.setText("Coord.: " + ficha.getCoordenadas());

        holder.btnExcluir.setOnClickListener(v -> listener.onDelete(ficha));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTipo, txtCoord;
        Button btnExcluir;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTipo = itemView.findViewById(R.id.txt_tipo_gis);
            txtCoord = itemView.findViewById(R.id.txt_coordenadas_gis);
            btnExcluir = itemView.findViewById(R.id.btn_excluir_gis);
        }
    }
}
