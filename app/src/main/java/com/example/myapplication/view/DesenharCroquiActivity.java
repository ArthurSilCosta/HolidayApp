// DesenharCroquiActivity.java
package com.example.myapplication.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.CroquiService;
import com.example.myapplication.model.FichaCroqui;
import com.example.myapplication.view.custom.CroquiDrawingView;
import com.example.myapplication.api.FichaCroquiDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
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

public class DesenharCroquiActivity extends AppCompatActivity {

    private CroquiDrawingView drawingView;
    private EditText inputTitulo, inputAnotacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desenhar_croqui);

        drawingView = findViewById(R.id.desenho_croqui);
        inputTitulo = findViewById(R.id.input_titulo_croqui);
        inputAnotacao = findViewById(R.id.input_anotacao_croqui);
        Button btnSalvar = findViewById(R.id.btn_salvar_croqui);
        Button btnVoltar = findViewById(R.id.btn_voltar_desenhar);
        btnVoltar.setOnClickListener(v -> finish());

        btnSalvar.setOnClickListener(v -> salvarCroqui());
    }

    private void salvarCroqui() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/") // ← para emulador Android, ou use o IP do seu PC real se for dispositivo físico
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CroquiService api = retrofit.create(CroquiService.class);

        Bitmap bitmap = drawingView.getBitmap();
        if (bitmap == null) {
            Toast.makeText(this, "Nenhum desenho encontrado.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File arquivo = new File(getFilesDir(), UUID.randomUUID().toString() + "_croqui.png");
            FileOutputStream fos = new FileOutputStream(arquivo);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            String titulo = inputTitulo.getText().toString().trim();
            String anotacao = inputAnotacao.getText().toString().trim();
            String caminhoImagem = arquivo.getAbsolutePath();
            String idProjeto = getIntent().getStringExtra("idProjeto");

            Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(r -> {
                FichaCroqui croqui = r.createObject(FichaCroqui.class, UUID.randomUUID().toString());
                croqui.setTitulo(titulo);
                croqui.setAnotacao(anotacao);
                croqui.setCaminhoImagem(caminhoImagem);
                croqui.setIdProjeto(idProjeto);
            });

            Toast.makeText(this, "Croqui salvo com sucesso!", Toast.LENGTH_SHORT).show();
            String id = UUID.randomUUID().toString(); // mesmo que usou no Realm
            String dataAtual = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).format(new Date());

            FichaCroquiDTO dto = new FichaCroquiDTO(
                    id,
                    idProjeto,
                    titulo,
                    anotacao,
                    dataAtual
            );

            api.enviarCroqui(dto).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("API", "Croqui enviado com sucesso!");
                    } else {
                        Log.e("API", "Erro ao enviar croqui: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("API", "Falha na conexão: " + t.getMessage());
                }
            });

            finish();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao salvar croqui.", Toast.LENGTH_SHORT).show();
        }
    }

}
