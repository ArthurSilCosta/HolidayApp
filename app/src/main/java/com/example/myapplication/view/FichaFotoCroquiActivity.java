package com.example.myapplication.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.FichaImagem;

import com.example.myapplication.api.FichaFotoDTO;
import com.example.myapplication.api.FotoService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import io.realm.Realm;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class FichaFotoCroquiActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText inputData;

    private ImageView imagePreview;
    private EditText inputLegenda, inputTituloCroqui, inputAnotacaoCroqui;
    private Bitmap fotoBitmap;
    private Realm realm;
    private String idProjeto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_foto_croqui);

        realm = Realm.getDefaultInstance();
        idProjeto = getIntent().getStringExtra("idProjeto");
        Button btnVoltar = findViewById(R.id.btn_voltar_foto_croqui);
        btnVoltar.setOnClickListener(v -> finish());

        imagePreview = findViewById(R.id.image_preview);
        inputLegenda = findViewById(R.id.input_legenda);
        inputTituloCroqui = findViewById(R.id.input_titulo_croqui);
        inputAnotacaoCroqui = findViewById(R.id.input_anotacao_croqui);

        EditText inputTitulo = findViewById(R.id.input_titulo_croqui);
        EditText inputLegenda = findViewById(R.id.input_legenda);
        inputData = findViewById(R.id.input_data_foto);
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


        String titulo = inputTitulo.getText().toString().trim();
        String legenda = inputLegenda.getText().toString().trim();

        findViewById(R.id.btn_tirar_foto).setOnClickListener(v -> abrirCamera());
        findViewById(R.id.btn_salvar_foto_croqui).setOnClickListener(v -> salvarFicha());
        findViewById(R.id.btn_desenhar_croqui).setOnClickListener(v -> {
            Intent i = new Intent(this, DesenharCroquiActivity.class);
            startActivity(i);


        });
    }

    private void abrirCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            fotoBitmap = (Bitmap) data.getExtras().get("data");
            imagePreview.setImageBitmap(fotoBitmap);
        }
    }

    private void salvarFicha() {
        if (fotoBitmap == null) {
            Toast.makeText(this, getString(R.string.error_take_photo_first), Toast.LENGTH_SHORT).show();
            return;
        }

        String legenda = inputLegenda.getText().toString().trim();
        String titulo = inputTituloCroqui.getText().toString().trim();
        String anotacaoCroqui = inputAnotacaoCroqui.getText().toString().trim();
        String dataTexto = inputData.getText().toString().trim();
        if (dataTexto.isEmpty()) {
            inputData.setError("Data obrigatória");
            return;
        }
        if (titulo.isEmpty()) {
            inputTituloCroqui.setError(getString(R.string.error_photo_date_required));
            return;
        }
        if (legenda.length() > 150) {
            inputLegenda.setError(getString(R.string.error_subitles_max));
            return;
        }


        String nomeArquivo = UUID.randomUUID().toString() + ".jpg";
        File file = new File(getFilesDir(), nomeArquivo);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        } catch (Exception e) {
            Log.e("FOTO", "Erro ao salvar foto", e);
            Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = UUID.randomUUID().toString();
        String dataAtual = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

        // 1. Salva localmente (Realm)
        realm.executeTransactionAsync(bgRealm -> {
            FichaImagem ficha = bgRealm.createObject(FichaImagem.class, id);
            ficha.setIdProjeto(idProjeto);
            ficha.setLegenda(legenda);
            ficha.setCaminhoImagem(file.getAbsolutePath());
            ficha.setTituloCroqui(titulo);
            ficha.setAnotacaoCroqui(anotacaoCroqui);
            ficha.setData(dataTexto);

        }, () -> {
            Toast.makeText(this,getString(R.string.toast_photo_saved), Toast.LENGTH_SHORT).show();

            // 2. Envia para o MongoDB
            Log.d("DEBUG", "ID Projeto: " + idProjeto);
            Log.d("DEBUG", "Título: " + titulo);
            Log.d("DEBUG", "Legenda: " + legenda);
            Log.d("DEBUG", "Data: " + dataAtual);
            Log.d("DEBUG", "Data: " + dataTexto);
            FichaFotoDTO dto = new FichaFotoDTO(id, idProjeto, titulo, legenda, dataTexto);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:3000/") // use IP real se for celular físico
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            FotoService api = retrofit.create(FotoService.class);
            api.enviarFoto(dto).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("API", "Foto enviada com sucesso!");
                    } else {
                        Log.e("API", "Erro ao enviar foto: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("API", "Falha na conexão: " + t.getMessage());
                }
            });

            finish();

        }, error -> {
            Log.e("REALM", "Erro ao salvar ficha de imagem", error);
            Toast.makeText(this, "Erro ao salvar ficha", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
