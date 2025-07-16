package com.example.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class ProjetoInternoActivity extends AppCompatActivity {

    private String nomeProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projeto_interno);

        // Recebe o nome do projeto via Intent
        nomeProjeto = getIntent().getStringExtra("nome_projeto");

        TextView txtNomeProjeto = findViewById(R.id.txt_nome_projeto);
        txtNomeProjeto.setText("Projeto: " + nomeProjeto);

        Button btnSitios = findViewById(R.id.btn_sitios);
        Button btnArtefatos = findViewById(R.id.btn_artefatos);
        Button btnGIS = findViewById(R.id.btn_gis);
        Button btnAnotacoes = findViewById(R.id.btn_anotacoes);
        Button btnCroquis = findViewById(R.id.btn_croquis);

        // Para cada botão, abrir uma nova tela (a ser criada)
        btnSitios.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaFichasActivity.class);
            intent.putExtra("categoria", "sitios");
            intent.putExtra("nome_projeto", nomeProjeto);
            startActivity(intent);
        });

        btnArtefatos.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaFichasActivity.class);
            intent.putExtra("categoria", "artefatos");
            intent.putExtra("nome_projeto", nomeProjeto);
            startActivity(intent);
        });

        btnGIS.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaFichasActivity.class);
            intent.putExtra("categoria", "gis");
            intent.putExtra("nome_projeto", nomeProjeto);
            startActivity(intent);
        });

        btnAnotacoes.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaFichasActivity.class);
            intent.putExtra("categoria", "anotacoes");
            intent.putExtra("nome_projeto", nomeProjeto);
            startActivity(intent);
        });

        btnCroquis.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaFichasActivity.class);
            intent.putExtra("categoria", "croquis");
            intent.putExtra("nome_projeto", nomeProjeto);
            startActivity(intent);
        });
    }
}
