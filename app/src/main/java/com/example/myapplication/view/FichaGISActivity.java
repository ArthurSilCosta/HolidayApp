package com.example.myapplication.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.FichaGISDTO;
import com.example.myapplication.api.GISService;
import com.example.myapplication.model.FichaGIS;

import java.util.Calendar;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class FichaGISActivity extends AppCompatActivity {

    private String idProjeto;
    private Realm realm;
    private FusedLocationProviderClient fusedLocationClient;
    private EditText campoCoordenadas;

    private void obterLocalizacaoGIS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                String latLong = location.getLatitude() + ", " + location.getLongitude();

                if (campoCoordenadas != null) {
                    campoCoordenadas.setText(latLong);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_gis);

        idProjeto = getIntent().getStringExtra("idProjeto");
        realm = Realm.getDefaultInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            obterLocalizacaoGIS();
        }


        EditText tipo = findViewById(R.id.input_tipo_dado);
        campoCoordenadas = findViewById(R.id.input_coordenadas);

        EditText fonte = findViewById(R.id.input_fonte);
        EditText descricao = findViewById(R.id.input_descricao);
        EditText inputData = findViewById(R.id.input_data_gis);
        inputData.setFocusable(false);
        inputData.setClickable(true);
        inputData.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        String dataFormatada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        inputData.setText(dataFormatada);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });


        Button btnSalvar = findViewById(R.id.btn_salvar_gis);
        Button btnVoltar = findViewById(R.id.btn_voltar_categoria);
        btnVoltar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> {
            String t = tipo.getText().toString().trim();
            String c = campoCoordenadas.getText().toString().trim();
            String f = fonte.getText().toString().trim();
            String d = descricao.getText().toString().trim();
            String id = UUID.randomUUID().toString();


            // Tipo de dado
            if (t.isEmpty()) {
                tipo.setError("Tipo de dado é obrigatório");
                return;
            }
            if (t.length() > 100) {
                tipo.setError("Máximo de 100 caracteres");
                return;
            }

            // Coordenadas
            if (c.isEmpty()) {
                campoCoordenadas.setError("Coordenadas são obrigatórias");
                return;
            }
            if (!c.matches("^-?\\d+(\\.\\d+)?(,\\s*-?\\d+(\\.\\d+)?)?$")) {
                campoCoordenadas.setError("Formato inválido. Use: lat, long (ex: -23.5, -46.6)");
                return;
            }

            // Fonte (opcional)
            if (f.length() > 100) {
                fonte.setError("Máximo de 100 caracteres");
                return;
            }

            // Descrição (opcional)
            if (d.length() > 300) {
                descricao.setError("Máximo de 300 caracteres");
                return;
            }
            String dataTexto = inputData.getText().toString().trim();

            if (dataTexto.isEmpty()) {
                inputData.setError("Data obrigatória");
                return;
            }


            realm.executeTransactionAsync(r -> {
                FichaGIS ficha = r.createObject(FichaGIS.class, id);

                ficha.setIdProjeto(idProjeto);
                ficha.setTipoDado(t);
                ficha.setCoordenadas(c);
                ficha.setFonte(f);
                ficha.setDescricao(d);
                ficha.setData(dataTexto);

            }, () -> {
                Toast.makeText(this, "Ficha GIS salva!", Toast.LENGTH_SHORT).show();
                 String dataAtual = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());


                FichaGISDTO dto = new FichaGISDTO(
                        id,
                        idProjeto,
                        c,
                        d,
                        dataTexto
                );

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:3000/") // ou IP local se for celular real
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                GISService api = retrofit.create(GISService.class);

                api.enviarGIS(dto).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.d("API", "GIS enviado com sucesso!");
                        } else {
                            Log.e("API", "Erro ao enviar GIS: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("API", "Falha na conexão: " + t.getMessage());
                    }
                });

                finish();
            }, error -> {
                Toast.makeText(this, "Erro ao salvar ficha", Toast.LENGTH_LONG).show();
            });
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obterLocalizacaoGIS();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
