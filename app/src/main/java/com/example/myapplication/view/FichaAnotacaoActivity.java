package com.example.myapplication.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.AnotacaoService;
import com.example.myapplication.api.FichaAnotacaoDTO;
import com.example.myapplication.model.FichaAnotacao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FichaAnotacaoActivity extends AppCompatActivity {

    private Realm realm;
    private String idProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_anotacao);

        idProjeto = getIntent().getStringExtra("idProjeto");
        realm = Realm.getDefaultInstance();

        EditText titulo = findViewById(R.id.input_titulo_anotacao);
        EditText conteudo = findViewById(R.id.input_conteudo_anotacao);
        EditText data = findViewById(R.id.input_data_criacao);
        data.setFocusable(false);
        data.setClickable(true);
        data.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        data.setText(dataFormatada);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });


        Button btnSalvar = findViewById(R.id.btn_salvar_anotacao);
        Button btnVoltar = findViewById(R.id.btn_voltar_categoria);
        btnVoltar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> {
            String tituloTexto = titulo.getText().toString().trim();
            String conteudoTexto = conteudo.getText().toString().trim();
            String id = UUID.randomUUID().toString();
            String dataTexto = data.getText().toString().trim();
            // Validação do título
            if (tituloTexto.isEmpty()) {
                titulo.setError("Título é obrigatório");
                return;
            }
            if (tituloTexto.length() > 100) {
                titulo.setError("Máximo de 100 caracteres");
                return;
            }

            // Validação do conteúdo (opcional, mas com limite)
            if (conteudoTexto.length() > 500) {
                conteudo.setError("Máximo de 500 caracteres");
                return;
            }


            realm.executeTransactionAsync(bgRealm -> {
                        FichaAnotacao ficha = bgRealm.createObject(FichaAnotacao.class, id);
                        ficha.setIdProjeto(idProjeto);
                ficha.setTitulo(tituloTexto);
                ficha.setConteudo(conteudoTexto);
                ficha.setDataCriacao(dataTexto);
            }

            , () -> {
                Toast.makeText(this, "Anotação salva com sucesso!", Toast.LENGTH_SHORT).show();
                        // Envio para o MongoDB Atlas
                         String dataAtual = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

                        FichaAnotacaoDTO dto = new FichaAnotacaoDTO(
                                id,
                                idProjeto,
                                tituloTexto,
                                conteudoTexto,
                                dataTexto
                        );

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://10.0.2.2:3000/") // ou IP real se for dispositivo físico
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        AnotacaoService api = retrofit.create(AnotacaoService.class);

                        api.enviarAnotacao(dto).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Log.d("API", "Anotação enviada com sucesso!");
                                } else {
                                    Log.e("API", "Erro ao enviar anotação: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("API", "Falha na conexão: " + t.getMessage());
                            }
                        });


                        finish();
            }, error -> {
                Toast.makeText(this, "Erro ao salvar anotação.", Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
