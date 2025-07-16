package com.example.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.api.ProjetoDTO;
import com.example.myapplication.api.ProjetoService;
import com.example.myapplication.model.Projeto;
import com.example.myapplication.view.adapter.ProjetoAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuProjetosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProjetoAdapter adapter;
    private final List<Projeto> listaProjetos = new ArrayList<>();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_projetos);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        recyclerView = findViewById(R.id.recycler_projetos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProjetoAdapter(listaProjetos, projeto -> abrirDetalhesProjeto(projeto.getId()));
        recyclerView.setAdapter(adapter);

        carregarProjetos();

        Button btnCriar = findViewById(R.id.btn_criar_projeto);
        btnCriar.setOnClickListener(v -> mostrarDialogCriarProjeto());
    }

    private void carregarProjetos() {
        RealmResults<Projeto> resultados = realm.where(Projeto.class).findAll();
        listaProjetos.clear();
        listaProjetos.addAll(realm.copyFromRealm(resultados));
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogCriarProjeto() {
        EditText input = new EditText(this);
        input.setHint("Nome do Projeto");
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)}); // limite de 50 caracteres

        new AlertDialog.Builder(this)
                .setTitle("Novo Projeto")
                .setView(input)
                .setPositiveButton("Criar", (dialog, which) -> {
                    String nome = input.getText().toString().trim();

                    if (nome.isEmpty()) {
                        Toast.makeText(this, "Digite um nome válido para o projeto", Toast.LENGTH_SHORT).show();
                        input.setError("Nome obrigatório");
                        return;
                    }

                    Projeto existente = realm.where(Projeto.class)
                            .equalTo("nome", nome)
                            .findFirst();

                    if (existente != null) {
                        Toast.makeText(this, "Já existe um projeto com esse nome", Toast.LENGTH_SHORT).show();
                        input.setError("Projeto duplicado");
                        return;
                    }

                    salvarProjeto(nome);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }


    private void salvarProjeto(String nomeProjeto) {
        String idProjeto = UUID.randomUUID().toString(); // Gera antes
        Date dataCriacao = new Date(); // Data para Realm e Mongo

        realm.executeTransactionAsync(realm1 -> {
            Projeto projeto = realm1.createObject(Projeto.class, idProjeto);
            projeto.setNome(nomeProjeto);
            projeto.setDataCriacao(dataCriacao);
        }, () -> {
            Log.d("REALM", "✅ Projeto salvo com sucesso!");

            // Agora envia para o MongoDB
            String dataFormatada = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(dataCriacao);

            ProjetoDTO dto = new ProjetoDTO(idProjeto, nomeProjeto, dataFormatada);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ProjetoService api = retrofit.create(ProjetoService.class);

            api.enviarProjeto(dto).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("API", "Projeto enviado com sucesso!");
                    } else {
                        Log.e("API", "Erro ao enviar projeto: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("API", "Falha ao enviar projeto: " + t.getMessage());
                }
            });

            carregarProjetos(); // Atualiza lista após salvar
        }, error -> {
            Log.e("REALM", "❌ Erro ao salvar projeto: ", error);
            Toast.makeText(this, "Erro ao criar projeto.", Toast.LENGTH_SHORT).show();
        });
    }





    private void abrirDetalhesProjeto(String idProjeto) {
        Intent intent = new Intent(this, DetalhesProjetoActivity.class);
        intent.putExtra("idProjeto", idProjeto);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
