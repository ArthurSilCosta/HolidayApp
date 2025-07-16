package com.example.myapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.FichaBase;

import java.util.List;

public class FichaBaseAdapter extends RecyclerView.Adapter<FichaBaseAdapter.ViewHolder> {

    private List<FichaBase> lista;

    public FichaBaseAdapter(List<FichaBase> lista) {
        this.lista = lista;
    }

    @Override
    public FichaBaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ficha_simples, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FichaBase ficha = lista.get(position);
        holder.txtTitulo.setText(ficha.getTitulo());
        holder.txtDescricao.setText(ficha.getDescricao());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtDescricao;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txt_titulo_ficha);
            txtDescricao = itemView.findViewById(R.id.txt_descricao_ficha);
        }
    }
}
