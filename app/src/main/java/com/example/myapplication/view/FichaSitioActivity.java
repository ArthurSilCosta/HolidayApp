package com.example.myapplication.view;

import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import com.example.myapplication.R;
import com.example.myapplication.api.FichaSitioDTO;
import com.example.myapplication.api.SitioService;
import com.example.myapplication.model.FichaSitio;

import java.util.UUID;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FichaSitioActivity extends AppCompatActivity {

    private String idProjeto;
    private Realm realm;

    // Campos de entrada
    private EditText campoSitio, campoLocalizacao, campoPesquisador, campoDataTexto;
    private EditText campoDescricaoGeral, campoTextura, campoCorSolo;
    private EditText inputNome;
    private FusedLocationProviderClient fusedLocationClient;

    private void obterLocalizacao() {
        Log.d("DEBUG_LOC", "obterLocalizacao() chamado");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                Log.d("LOCALIZACAO", "Localização obtida: " + location.getLatitude() + ", " + location.getLongitude());

                String latLong = location.getLatitude() + ", " + location.getLongitude();

                if (campoLocalizacao != null) {
                    campoLocalizacao.setText(latLong);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d("PERMISSAO", "Verificando permissão de localização...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_sitio);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSAO", "Permissão de localização ainda NÃO concedida. Solicitando...");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        } else {
            Log.d("PERMISSAO", "Permissão de localização já concedida. Chamando obterLocalizacao()");
            obterLocalizacao();
        }


        idProjeto = getIntent().getStringExtra("idProjeto");
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        // Referências dos campos
        campoSitio = findViewById(R.id.input_nome_sitio);
        campoLocalizacao = findViewById(R.id.input_localizacao);
        campoPesquisador = findViewById(R.id.input_pesquisador);
        campoDataTexto = findViewById(R.id.input_data_registro);
        campoDataTexto.setFocusable(false);
        campoDataTexto.setClickable(true);
        inputNome = findViewById(R.id.input_nome_sitio);

        campoDataTexto.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        campoDataTexto.setText(dataFormatada);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });


        campoDescricaoGeral = findViewById(R.id.input_descricao_geral);
        campoTextura = findViewById(R.id.input_tipo_solo);
        campoCorSolo = findViewById(R.id.input_coloracao_solo);
        Button btnSalvar = findViewById(R.id.btn_salvar_sitio);
        Button btnVoltar = findViewById(R.id.btn_voltar_categoria);
        btnVoltar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> {
            String sitio = campoSitio.getText().toString().trim();
            String localizacao = campoLocalizacao.getText().toString().trim();
            String pesquisador = campoPesquisador.getText().toString().trim();
            String dataTexto = campoDataTexto.getText().toString().trim();
            String estruturas = campoDescricaoGeral.getText().toString().trim();
            String textura = campoTextura.getText().toString().trim();
            String corSolo = campoCorSolo.getText().toString().trim();
            String nomeTexto = campoSitio.getText().toString().trim();


// Limites de caracteres
            if (nomeTexto.length() > 100) {
                inputNome.setError(getString(R.string.error_max_name));
                return;
            }
            if (localizacao.isEmpty()) {
                campoLocalizacao.setError(getString(R.string.error_location));

            }
            if (estruturas.length() > 300) {
                campoDescricaoGeral.setError(getString(R.string.max_structure_lenght));
                return;
            }
            // Campos obrigatórios
            if (nomeTexto.isEmpty()) {
                inputNome.setError(getString(R.string.error_site_name_required));
                return;
            }




            realm.executeTransactionAsync(bgRealm -> {
                FichaSitio ficha = bgRealm.createObject(FichaSitio.class, UUID.randomUUID().toString());
                ficha.setIdProjeto(idProjeto);
                ficha.setSitio(sitio);
                ficha.setLocalizacao(localizacao);
                ficha.setPesquisador(pesquisador);
                ficha.setDataTexto(dataTexto);
                ficha.setEstruturas(estruturas);
                ficha.setTextura(textura);
                ficha.setCorSolo(corSolo);
            }, () -> {
                Log.d("REALM", "✅ Ficha de Sítio salva localmente com sucesso");
                Toast.makeText(this, getString(R.string.toast_site_saved), Toast.LENGTH_SHORT).show();
                String id = UUID.randomUUID().toString();
                String dataAtual = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

                FichaSitioDTO dto = new FichaSitioDTO(
                        id,
                        idProjeto,
                        nomeTexto,
                        localizacao,
                        estruturas,
                        dataAtual
                );

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/") // ou IP do seu PC real
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                SitioService api = retrofit.create(SitioService.class);

                api.enviarSitio(dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("API", "Sítio enviado com sucesso!");
                        } else {
                            Log.e("API", "Erro ao enviar sítio: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("API", "Falha na conexão: " + t.getMessage());
                    }
                });

                finish();
            }, error -> {
                Log.e("REALM", getString(R.string.toast_site_save_error), error);
            });
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSAO", "onRequestPermissionsResult chamado");
            Log.d("PERMISSAO", "grantResults[0]: " + (grantResults.length > 0 ? grantResults[0] : "N/A"));

            obterLocalizacao();
            Log.d("LOCALIZACAO", "obterLocalizacao() foi chamado");

        }else {
            Toast.makeText(this,getString(R.string.toast_location_not_found), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
