
package com.example.myapplication.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.FichaCroqui;
import com.example.myapplication.view.custom.CroquiDrawingView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.realm.Realm;

public class FichaCroquiActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private Uri photoUri;
    private String currentPhotoPath;
    private ImageView imagePreview;

    private Realm realm;

    private String idProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desenhar_croqui);

        idProjeto = getIntent().getStringExtra("idProjeto");
        realm = Realm.getDefaultInstance();

        EditText inputTitulo = findViewById(R.id.input_titulo_croqui);
        EditText inputAnotacao = findViewById(R.id.input_anotacao_croqui);
        CroquiDrawingView desenhoView = findViewById(R.id.desenho_croqui);
        Button btnSalvar = findViewById(R.id.btn_salvar_croqui);

        btnSalvar.setOnClickListener(v -> {
            String titulo = inputTitulo.getText().toString();
            String anotacao = inputAnotacao.getText().toString();

            // salvar imagem
            String filename = "croqui_" + UUID.randomUUID().toString() + ".png";
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);

            Bitmap bitmap = Bitmap.createBitmap(desenhoView.getWidth(), desenhoView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            desenhoView.draw(canvas);

            try (FileOutputStream out = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                Log.d("CROQUI", "📸 Croqui salvo em: " + file.getAbsolutePath());

                // salva no Realm
                realm.executeTransactionAsync(bgRealm -> {
                    FichaCroqui ficha = bgRealm.createObject(FichaCroqui.class, UUID.randomUUID().toString());
                    ficha.setIdProjeto(idProjeto);
                    ficha.setTitulo(titulo);
                    ficha.setAnotacao(anotacao);
                    ficha.setCaminhoImagem(file.getAbsolutePath());
                }, () -> {
                    Toast.makeText(this, "Croqui salvo!", Toast.LENGTH_SHORT).show();
                    finish();
                }, error -> {
                    Log.e("REALM", "❌ Erro ao salvar croqui", error);
                    Toast.makeText(this, "Erro ao salvar croqui", Toast.LENGTH_SHORT).show();
                });

            } catch (IOException e) {
                Log.e("CROQUI", "Erro ao salvar imagem", e);
                Toast.makeText(this, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
