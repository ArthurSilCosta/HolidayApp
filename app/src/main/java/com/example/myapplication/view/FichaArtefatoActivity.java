package com.example.myapplication.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.ArtefatoService;
import com.example.myapplication.api.FichaArtefatoDTO;
import com.example.myapplication.model.FichaArtefato;

import java.util.UUID;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FichaArtefatoActivity extends AppCompatActivity {

    private String idProjeto;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_artefato);

        idProjeto = getIntent().getStringExtra("idProjeto");
        realm = Realm.getDefaultInstance();

        EditText inputDescricao = findViewById(R.id.input_descricao);
        EditText inputTipo = findViewById(R.id.input_tipo);
        EditText inputMaterial = findViewById(R.id.input_material);
        EditText inputDimensoes = findViewById(R.id.input_dimensoes);
        EditText inputContexto = findViewById(R.id.input_contexto);
        EditText inputData = findViewById(R.id.input_data_descoberta);
        inputData.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        // Formato: yyyy-MM-dd
                        String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        inputData.setText(dataFormatada);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });


        Button btnSalvar = findViewById(R.id.btn_salvar_artefato);
        Button btnVoltar = findViewById(R.id.btn_voltar_categoria);
        btnVoltar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> {
            String descricao = inputDescricao.getText().toString().trim();
            String tipo = inputTipo.getText().toString().trim();
            String material = inputMaterial.getText().toString().trim();
            String dimensoes = inputDimensoes.getText().toString().trim();
            String contexto = inputContexto.getText().toString().trim();
            String dataDescoberta = inputData.getText().toString().trim();
            String id = UUID.randomUUID().toString();  // Gera ID uma única vez
            String tipoTexto = tipo;
            String descricaoTexto = descricao;

            // Descrição (obrigatória)
            if (descricao.isEmpty()) {
                inputDescricao.setError(getString(R.string.error_artifact_description_required));
                return;
            }
            if (descricao.length() > 300) {
                inputDescricao.setError(getString(R.string.error_artifact_description_max));
                return;
            }

            // Tipo (obrigatório)
            if (tipo.isEmpty()) {
                inputTipo.setError(getString(R.string.error_Type_description_required));
                return;
            }
            if (tipo.length() > 100) {
                inputTipo.setError(getString(R.string.error_type_description_max));
                return;
            }

            // Material (opcional)
            if (material.length() > 100) {
                inputMaterial.setError(getString(R.string.error_material_max));
                return;
            }

            // Dimensões (opcional)
            if (dimensoes.length() > 50) {
                inputDimensoes.setError(getString(R.string.error_dimension_max));
                return;
            }

            // Contexto (opcional)
            if (contexto.length() > 300) {
                inputContexto.setError(getString(R.string.error_context_max));
                return;
            }



            realm.executeTransactionAsync(bgRealm -> {
                FichaArtefato ficha = bgRealm.createObject(FichaArtefato.class, id); // Usa o mesmo ID
                ficha.setIdProjeto(idProjeto);
                ficha.setDescricaoArtefato(descricao);
                ficha.setTipoArtefato(tipo);
                ficha.setMaterial(material);
                ficha.setDimensoes(dimensoes);
                ficha.setContexto(contexto);
                ficha.setDataDescoberta(dataDescoberta);
            }, () -> {
                Log.d("REALM", "✅ Ficha de artefato salva com sucesso.");
                Toast.makeText(this, "Ficha salva!", Toast.LENGTH_SHORT).show();

                String dataAtual = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

                FichaArtefatoDTO dto = new FichaArtefatoDTO(
                        id,
                        idProjeto,
                        tipoTexto,
                        descricaoTexto,
                        dataAtual
                );


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/") // use o IP local real se for celular
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ArtefatoService api = retrofit.create(ArtefatoService.class);

                api.enviarArtefato(dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("API", "Artefato enviado com sucesso!");
                        } else {
                            Log.e("API", "Erro ao enviar artefato: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("API", "Falha na conexão: " + t.getMessage());
                    }
                });

                finish();
            }, error -> {
                Log.e("REALM", "❌ Erro ao salvar ficha de artefato", error);
                Toast.makeText(this, "Erro ao salvar", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
