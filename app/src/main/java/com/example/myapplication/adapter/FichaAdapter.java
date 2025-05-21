package com.example.myapplication.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Ficha;
import com.example.myapplication.view.VisualizarFichaActivity;

import java.util.List;

import io.realm.Realm;

public class FichaAdapter extends RecyclerView.Adapter<FichaAdapter.FichaViewHolder> {

    private final List<Ficha> fichas;

    public FichaAdapter(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    @Override
    public FichaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ficha, parent, false);
        return new FichaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FichaViewHolder holder, int position) {
        Ficha ficha = fichas.get(position);
        holder.txtSitio.setText("Sítio: " + ficha.getSitio());
        holder.txtPesquisador.setText("Pesquisador: " + ficha.getPesquisador());
        holder.txtData.setText("Data: " + ficha.getDataTexto());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VisualizarFichaActivity.class);
            intent.putExtra("fichaId", ficha.getId()); // Certifique-se de que o campo "id" existe
            v.getContext().startActivity(intent);
        });
        holder.btnExcluir.setOnClickListener(view -> {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(r -> {
                Ficha fichaToDelete = r.where(Ficha.class).equalTo("id", ficha.getId()).findFirst();
                if (fichaToDelete != null) {
                    fichaToDelete.deleteFromRealm();
                }
            }, () -> {
                // Atualizar a lista local após exclusão
                fichas.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, fichas.size());
                Toast.makeText(view.getContext(), "Ficha excluída com sucesso", Toast.LENGTH_SHORT).show();
            }, error -> {
                Toast.makeText(view.getContext(), "Erro ao excluir: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });

    }

    @Override
    public int getItemCount() {
        return fichas.size();
    }

    static class FichaViewHolder extends RecyclerView.ViewHolder {
        TextView txtSitio, txtPesquisador, txtData;
        View btnExcluir;

        public FichaViewHolder(View itemView) {
            super(itemView);
            txtSitio = itemView.findViewById(R.id.txt_sitio);
            txtPesquisador = itemView.findViewById(R.id.txt_pesquisador);
            txtData = itemView.findViewById(R.id.txt_data);
            btnExcluir = itemView.findViewById(R.id.btn_excluir);
        }
    }
}
