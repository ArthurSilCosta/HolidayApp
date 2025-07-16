package com.example.myapplication.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Ficha;
import io.realm.Realm;
import android.widget.Button;
import android.widget.Toast;


public class VisualizarFichaActivity extends AppCompatActivity {

    private TextView txtSitio, txtLocalizacao, txtQuadra, txtProfundidade, txtPesquisador, txtData;
    private TextView txtCorSolo, txtTextura, txtEstruturas, txtMaterial, txtRestos, txtObservacoes;
    private TextView txtLatitude, txtLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_ficha);

        String fichaId = getIntent().getStringExtra("fichaId");

        Realm realm = Realm.getDefaultInstance();
        Ficha ficha = realm.where(Ficha.class).equalTo("id", fichaId).findFirst();

        txtSitio = findViewById(R.id.txt_sitio);
        txtLocalizacao = findViewById(R.id.txt_localizacao);
        txtQuadra = findViewById(R.id.txt_quadra);
        txtProfundidade = findViewById(R.id.txt_profundidade);
        txtPesquisador = findViewById(R.id.txt_pesquisador);
        txtData = findViewById(R.id.txt_data);
        txtCorSolo = findViewById(R.id.txt_cor_solo);
        txtTextura = findViewById(R.id.txt_textura);
        txtEstruturas = findViewById(R.id.txt_estruturas);
        txtMaterial = findViewById(R.id.txt_material);
        txtRestos = findViewById(R.id.txt_restos);
        txtObservacoes = findViewById(R.id.txt_observacoes);
        txtLatitude = findViewById(R.id.txt_latitude);
        txtLongitude = findViewById(R.id.txt_longitude);


        if (ficha != null) {
            txtSitio.setText("Sítio: " + ficha.getSitio());
            txtLocalizacao.setText("Localização: " + ficha.getLocalizacao());
            txtQuadra.setText("Quadra: " + ficha.getQuadra());
            txtProfundidade.setText("Profundidade: " + ficha.getProfundidade());
            txtPesquisador.setText("Pesquisador: " + ficha.getPesquisador());
            txtData.setText("Data: " + ficha.getDataTexto());
            txtCorSolo.setText("Cor do solo: " + ficha.getCorSolo());
            txtTextura.setText("Textura: " + ficha.getTextura());
            txtEstruturas.setText("Estruturas: " + ficha.getEstruturas());
            txtMaterial.setText("Material arqueológico: " + ficha.getMaterialArqueologico());
            txtRestos.setText("Restos orgânicos: " + ficha.getRestosOrganicos());
            txtObservacoes.setText("Observações: " + ficha.getObservacoes());
            ImageView iconSemGPS = findViewById(R.id.icon_sem_gps);

            if (ficha.getLatitude() == -999.999 && ficha.getLongitude() == -999.999) {
                txtLatitude.setText("Latitude: não disponível");
                txtLongitude.setText("Longitude: não disponível");
                iconSemGPS.setVisibility(View.VISIBLE);
            } else {
                txtLatitude.setText("Latitude: " + ficha.getLatitude());
                txtLongitude.setText("Longitude: " + ficha.getLongitude());
                iconSemGPS.setVisibility(View.GONE);
            }

            TextView txtDataFoto = findViewById(R.id.txt_data_foto_croqui);
            txtDataFoto.setText("Data da foto: " + ficha.getDataFotoCroqui());

        }



        Button btnEditarFicha = findViewById(R.id.btn_editar_ficha);
        btnEditarFicha.setOnClickListener(v -> {
            Intent intent = new Intent(VisualizarFichaActivity.this, NovaFichaActivity.class);
            intent.putExtra("modoEdicao", true);
            intent.putExtra("fichaId", ficha.getId());
            startActivity(intent);
        });

        Button btnVoltarLista = findViewById(R.id.btn_voltar_lista);
        btnVoltarLista.setOnClickListener(v -> {
            Intent intent = new Intent(VisualizarFichaActivity.this, ListaFichasActivity.class);
            startActivity(intent);
            finish();
        });

        Button btnExcluir = findViewById(R.id.btn_excluir_ficha);
        btnExcluir.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar exclusão")
                    .setMessage("Tem certeza que deseja excluir esta ficha?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        Realm realm1 = Realm.getDefaultInstance();
                        realm.executeTransactionAsync(r -> {
                            Ficha fichaToDelete = r.where(Ficha.class).equalTo("id", fichaId).findFirst();
                            if (fichaToDelete != null) {
                                fichaToDelete.deleteFromRealm();
                            }
                        }, () -> {
                            runOnUiThread(() -> {
                                Toast.makeText(VisualizarFichaActivity.this, "Ficha excluída com sucesso", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VisualizarFichaActivity.this, ListaFichasActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        }, error -> {
                            runOnUiThread(() ->
                                    Toast.makeText(VisualizarFichaActivity.this, "Erro ao excluir: " + error.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                        });
                        Toast.makeText(this, "Ficha excluída com sucesso", Toast.LENGTH_SHORT).show();
                        // Voltar automaticamente para lista
                        Intent intent = new Intent(this, ListaFichasActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });



    }
}
