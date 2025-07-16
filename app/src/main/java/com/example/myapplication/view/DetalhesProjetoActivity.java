package com.example.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.Projeto;

import io.realm.Realm;

public class DetalhesProjetoActivity extends AppCompatActivity {

    private String idProjeto;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_projeto);

        idProjeto = getIntent().getStringExtra("idProjeto");


        Realm realm = Realm.getDefaultInstance();
        Projeto projeto = realm.where(Projeto.class).equalTo("id", idProjeto).findFirst();

        TextView txtTituloProjeto = findViewById(R.id.txt_titulo_projeto);
        if (projeto != null) {
            txtTituloProjeto.setText("Projeto: " + projeto.getNome());
        } else {
            txtTituloProjeto.setText("Projeto: " + idProjeto); // fallback
        }


        Button btnSitios = findViewById(R.id.btn_sitios);
        Button btnArtefatos = findViewById(R.id.btn_artefatos);
        Button btnGIS = findViewById(R.id.btn_gis);
        Button btnAnotacoes = findViewById(R.id.btn_anotacoes);
        Button btnFotos = findViewById(R.id.btn_fotos);

        btnSitios.setOnClickListener(v -> abrirCategoria("sitios"));
        btnArtefatos.setOnClickListener(v -> abrirCategoria("artefatos"));
        btnGIS.setOnClickListener(v -> abrirCategoria("gis"));
        btnAnotacoes.setOnClickListener(v -> abrirCategoria("anotacoes"));
        btnFotos.setOnClickListener(v -> abrirCategoria("fotos"));
        Button btnMenuPrincipal = findViewById(R.id.btn_menu_principal);

        btnMenuPrincipal.setOnClickListener(v -> {
            Intent intent = new Intent(DetalhesProjetoActivity.this, MenuProjetosActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
        });
        realm.close();


    }

    private void abrirCategoria(String categoria) {
        Intent intent = new Intent(this, ListaFichasCategoriaActivity.class);
        intent.putExtra("idProjeto", idProjeto);
        intent.putExtra("categoria", categoria);
        startActivity(intent);

    }






}
