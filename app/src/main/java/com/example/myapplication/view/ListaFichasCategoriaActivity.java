package com.example.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.*;
import com.example.myapplication.model.*;
import com.example.myapplication.view.adapter.FichaAnotacaoAdapter;
import com.example.myapplication.view.adapter.FichaArtefatoAdapter;
import com.example.myapplication.view.adapter.FichaCroquiAdapter;
import com.example.myapplication.view.adapter.FichaGISAdapter;
import com.example.myapplication.view.adapter.FichaImagemAdapter;
import com.example.myapplication.view.adapter.FichaSitioAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListaFichasCategoriaActivity extends AppCompatActivity {

    private Realm realm;
    private String idProjeto;
    private String categoria;

    private RecyclerView recyclerView;
    private RecyclerView recyclerFotos;
    private RecyclerView recyclerCroquis;

    private FichaArtefatoAdapter artefatoAdapter;
    private FichaSitioAdapter sitioAdapter;
    private FichaGISAdapter adapterGIS;
    private FichaAnotacaoAdapter anotacaoAdapter;
    private FichaImagemAdapter imagemAdapter;
    private FichaCroquiAdapter croquiAdapter;

    private List<FichaArtefato> listaArtefatos = new ArrayList<>();
    private List<FichaSitio> listaSitios = new ArrayList<>();
    private List<FichaAnotacao> listaAnotacoes = new ArrayList<>();
    private List<FichaImagem> listaImagens = new ArrayList<>();
    private List<FichaCroqui> listaCroquis = new ArrayList<>();

    private LinearLayout abasLayout;
    private Button btnAbaFotos, btnAbaCroquis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_fichas_categoria);

        recyclerFotos = findViewById(R.id.recycler_fotos);
        recyclerCroquis = findViewById(R.id.recycler_croquis);
        recyclerView = findViewById(R.id.recycler_fichas);
        btnAbaFotos = findViewById(R.id.btn_aba_fotos);
        btnAbaCroquis = findViewById(R.id.btn_aba_croquis);
        abasLayout = findViewById(R.id.layout_abas_fotos);

        LinearLayout layoutBotoesFoto = findViewById(R.id.layout_botoes_foto);
        Button btnFoto = findViewById(R.id.btn_criar_foto);
        Button btnCroqui = findViewById(R.id.btn_criar_croqui);
        Button btnMenuPrincipal = findViewById(R.id.btn_menu_principal);
        Button btnVoltarProjeto = findViewById(R.id.btn_voltar_projeto);
        Button btnCriarFicha = findViewById(R.id.btn_criar_ficha_categoria);
        TextView txtCategoria = findViewById(R.id.txt_categoria);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        idProjeto = getIntent().getStringExtra("idProjeto");
        categoria = getIntent().getStringExtra("categoria");
        txtCategoria.setText(getString(R.string.category) +"" + categoria);
        Log.d("DEBUG", "Categoria recebida: " + categoria);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if ("artefatos".equalsIgnoreCase(categoria)) {
            RealmResults<FichaArtefato> resultados = realm.where(FichaArtefato.class).equalTo("idProjeto", idProjeto).findAll();
            listaArtefatos.addAll(realm.copyFromRealm(resultados));
            artefatoAdapter = new FichaArtefatoAdapter(listaArtefatos, artefato -> {
                realm.executeTransactionAsync(bgRealm -> {
                    FichaArtefato encontrado = bgRealm.where(FichaArtefato.class).equalTo("id", artefato.getId()).findFirst();
                    if (encontrado != null) encontrado.deleteFromRealm();
                }, () -> {
                    listaArtefatos.remove(artefato);
                    artefatoAdapter.notifyDataSetChanged();
                });
            });
            recyclerView.setAdapter(artefatoAdapter);

        } else if ("sitios".equalsIgnoreCase(categoria)) {
            RealmResults<FichaSitio> resultados = realm.where(FichaSitio.class).equalTo("idProjeto", idProjeto).findAll();
            listaSitios.addAll(realm.copyFromRealm(resultados));
            sitioAdapter = new FichaSitioAdapter(listaSitios, sitio -> {
                realm.executeTransactionAsync(bgRealm -> {
                    FichaSitio encontrado = bgRealm.where(FichaSitio.class).equalTo("id", sitio.getId()).findFirst();
                    if (encontrado != null) encontrado.deleteFromRealm();
                }, () -> {
                    listaSitios.remove(sitio);
                    sitioAdapter.notifyDataSetChanged();
                });
            });
            recyclerView.setAdapter(sitioAdapter);

        } else if ("gis".equalsIgnoreCase(categoria)) {
            List<FichaGIS> listaGIS = realm.copyFromRealm(realm.where(FichaGIS.class).equalTo("idProjeto", idProjeto).findAll());
            adapterGIS = new FichaGISAdapter(listaGIS, ficha -> {
                realm.executeTransactionAsync(bgRealm -> {
                    FichaGIS encontrada = bgRealm.where(FichaGIS.class).equalTo("id", ficha.getId()).findFirst();
                    if (encontrada != null) encontrada.deleteFromRealm();
                }, this::recreate);
            });
            recyclerView.setAdapter(adapterGIS);

        } else if ("anotacoes".equalsIgnoreCase(categoria)) {
            RealmResults<FichaAnotacao> resultados = realm.where(FichaAnotacao.class).equalTo("idProjeto", idProjeto).findAll();
            listaAnotacoes.addAll(realm.copyFromRealm(resultados));
            anotacaoAdapter = new FichaAnotacaoAdapter(listaAnotacoes, anotacao -> {
                realm.executeTransactionAsync(bgRealm -> {
                    FichaAnotacao encontrada = bgRealm.where(FichaAnotacao.class).equalTo("id", anotacao.getId()).findFirst();
                    if (encontrada != null) encontrada.deleteFromRealm();
                }, () -> {
                    listaAnotacoes.remove(anotacao);
                    anotacaoAdapter.notifyDataSetChanged();
                });
            });
            recyclerView.setAdapter(anotacaoAdapter);

        } else if ("fotos".equalsIgnoreCase(categoria)) {
            Log.d("DEBUG", "Entrou na categoria FOTOS");

            // TESTE: Ver quantos croquis existem no banco no total
            RealmResults<FichaCroqui> todosCroquis = realm.where(FichaCroqui.class).findAll();
            Log.d("DEBUG", "Croquis totais no banco: " + todosCroquis.size());

            // Agora sim: croquis filtrados por projeto
            RealmResults<FichaCroqui> croquis = realm.where(FichaCroqui.class)
                    .equalTo("idProjeto", idProjeto)
                    .findAll();


            listaCroquis.addAll(realm.copyFromRealm(croquis));
            croquiAdapter = new FichaCroquiAdapter(listaCroquis, ficha -> {
                realm.executeTransactionAsync(r -> {
                    FichaCroqui encontrada = r.where(FichaCroqui.class).equalTo("id", ficha.getId()).findFirst();
                    if (encontrada != null) encontrada.deleteFromRealm();
                }, () -> {
                    listaCroquis.remove(ficha);
                    croquiAdapter.notifyDataSetChanged();
                });
            });
            recyclerCroquis.setLayoutManager(new LinearLayoutManager(this));
            recyclerCroquis.setAdapter(croquiAdapter);
            recyclerCroquis.setVisibility(View.GONE);

            RealmResults<FichaImagem> imagens = realm.where(FichaImagem.class).equalTo("idProjeto", idProjeto).findAll();
            listaImagens.addAll(realm.copyFromRealm(imagens));
            imagemAdapter = new FichaImagemAdapter(listaImagens, ficha -> {
                realm.executeTransactionAsync(r -> {
                    FichaImagem encontrada = r.where(FichaImagem.class).equalTo("id", ficha.getId()).findFirst();
                    if (encontrada != null) encontrada.deleteFromRealm();
                }, () -> {
                    listaImagens.remove(ficha);
                    imagemAdapter.notifyDataSetChanged();
                });
            });
            recyclerFotos.setLayoutManager(new LinearLayoutManager(this));
            recyclerFotos.setAdapter(imagemAdapter);
            recyclerFotos.setVisibility(View.VISIBLE);

            recyclerView.setVisibility(View.GONE);
            abasLayout.setVisibility(View.VISIBLE);

            btnAbaFotos.setOnClickListener(view -> {
                Log.d("DEBUG", "Clique em aba FOTOS");
                recyclerFotos.setVisibility(View.VISIBLE);
                recyclerCroquis.setVisibility(View.GONE);
            });

            btnAbaCroquis.setOnClickListener(view -> {
                Log.d("DEBUG", "Clique em aba CROQUIS");
                recyclerFotos.setVisibility(View.GONE);
                recyclerCroquis.setVisibility(View.VISIBLE);
            });

            btnCriarFicha.setVisibility(View.GONE);
            layoutBotoesFoto.setVisibility(View.VISIBLE);

            btnFoto.setOnClickListener(v -> {
                Intent intent = new Intent(this, FichaFotoCroquiActivity.class);
                intent.putExtra("idProjeto", idProjeto);
                startActivity(intent);
            });

            btnCroqui.setOnClickListener(v -> {
                Intent intent = new Intent(this, DesenharCroquiActivity.class);
                intent.putExtra("idProjeto", idProjeto);
                startActivity(intent);
            });
        }

        btnMenuPrincipal.setOnClickListener(v -> {
            Intent intent = new Intent(this, MenuProjetosActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnVoltarProjeto.setOnClickListener(v -> finish());

        if (!"fotos".equalsIgnoreCase(categoria) && !"croquis".equalsIgnoreCase(categoria)) {
            btnCriarFicha.setVisibility(View.VISIBLE); // garante que apareça
            btnCriarFicha.setOnClickListener(v -> {
                if ("sitios".equalsIgnoreCase(categoria)) {
                    Intent intent = new Intent(this, FichaSitioActivity.class);
                    intent.putExtra("idProjeto", idProjeto);
                    startActivity(intent);
                } else if ("artefatos".equalsIgnoreCase(categoria)) {
                    Intent intent = new Intent(this, FichaArtefatoActivity.class);
                    intent.putExtra("idProjeto", idProjeto);
                    startActivity(intent);
                } else if ("gis".equalsIgnoreCase(categoria)) {
                    Intent intent = new Intent(this, FichaGISActivity.class);
                    intent.putExtra("idProjeto", idProjeto);
                    startActivity(intent);
                } else if ("anotacoes".equalsIgnoreCase(categoria)) {
                    Intent intent = new Intent(this, FichaAnotacaoActivity.class);
                    intent.putExtra("idProjeto", idProjeto);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getString(R.string.toast_category_not_implemented), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
