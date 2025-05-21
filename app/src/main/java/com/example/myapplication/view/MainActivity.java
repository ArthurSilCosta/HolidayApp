package com.example.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnVerFichas = findViewById(R.id.btn_ver_fichas);
        btnVerFichas.setOnClickListener(v -> {
            startActivity(new Intent(this, ListaFichasActivity.class));
        });

        Button btnNovaFicha = findViewById(R.id.btn_nova_ficha);
        btnNovaFicha.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NovaFichaActivity.class);
            startActivity(intent);
        });

    }
}
