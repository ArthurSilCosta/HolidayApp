package com.example.myapplication.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.myapplication.R;

import java.io.File;
import java.io.FileOutputStream;

public class DesenhoCroquiActivity extends Activity {

    private CustomDrawingView drawingView;
    private String caminhoImagemSalva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desenho_croqui);

        FrameLayout frame = findViewById(R.id.frame_desenho);
        drawingView = new CustomDrawingView(this);
        frame.addView(drawingView, 0); // adiciona como fundo da tela

        Button btnLimpar = findViewById(R.id.btn_limpar_desenho);
        Button btnSalvar = findViewById(R.id.btn_salvar_desenho);

        btnLimpar.setOnClickListener(v -> drawingView.clear());

        btnSalvar.setOnClickListener(v -> {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawingView.getWidth(),
                    drawingView.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawingView.draw(canvas);

            try {
                File file = new File(getExternalFilesDir(null), "croqui_desenhado_" + System.currentTimeMillis() + ".jpg");
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

                caminhoImagemSalva = file.getAbsolutePath();
                Intent intent = new Intent();
                intent.putExtra("caminhoCroqui", caminhoImagemSalva);
                setResult(RESULT_OK, intent);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Erro ao salvar o desenho", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
