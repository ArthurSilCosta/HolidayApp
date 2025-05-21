package com.example.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Ficha;
import com.example.myapplication.adapter.FichaAdapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListaFichasActivity extends AppCompatActivity {

    private RecyclerView recyclerFichas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_fichas);

        recyclerFichas = findViewById(R.id.recyclerFichas);
        recyclerFichas.setLayoutManager(new LinearLayoutManager(this));

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Ficha> resultado = realm.where(Ficha.class).findAll();

        FichaAdapter adapter = new FichaAdapter(realm.copyFromRealm(resultado));
        recyclerFichas.setAdapter(adapter);

        Button btnVoltar = findViewById(R.id.btn_voltar_menu);
        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(ListaFichasActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // opcional
        });
        EditText editSearch = findViewById(R.id.edit_search);
        Button btnFiltroData = findViewById(R.id.btn_filtro_data);
        Button btnFiltroSitio = findViewById(R.id.btn_filtro_sitio);
        Button btnFiltroPesquisador = findViewById(R.id.btn_filtro_pesquisador);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarFichas(s.toString(), "todos");
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        btnFiltroData.setOnClickListener(v -> filtrarFichas(editSearch.getText().toString(), "dataTexto"));
        btnFiltroSitio.setOnClickListener(v -> filtrarFichas(editSearch.getText().toString(), "sitio"));
        btnFiltroPesquisador.setOnClickListener(v -> filtrarFichas(editSearch.getText().toString(), "pesquisador"));
    }
    private void filtrarFichas(String termo, String campo) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Ficha> resultados;

        if (campo.equals("todos")) {
            resultados = realm.where(Ficha.class)
                    .contains("sitio", termo, Case.INSENSITIVE)
                    .or()
                    .contains("pesquisador", termo, Case.INSENSITIVE)
                    .or()
                    .contains("dataTexto", termo, Case.INSENSITIVE)
                    .findAll();
        } else {
            resultados = realm.where(Ficha.class)
                    .contains(campo, termo, Case.INSENSITIVE)
                    .findAll();
        }

        recyclerFichas.setAdapter(new FichaAdapter(realm.copyFromRealm(resultados)));
    }


}
