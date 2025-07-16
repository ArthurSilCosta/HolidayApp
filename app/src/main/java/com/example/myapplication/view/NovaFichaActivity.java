// NovaFichaActivity.java reescrito sem erros de sintaxe
// Inclui salvamento de localização, data da foto e tratamento de exceções

package com.example.myapplication.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
import android.widget.*;
import android.Manifest;


import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.FichaApi;

import com.example.myapplication.model.Ficha;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;



import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.myapplication.api.FichaDTO;

public class NovaFichaActivity extends AppCompatActivity {

    private EditText inputSitio, inputLocalizacao, inputQuadra, inputProfundidade, inputPesquisador, inputData;
    private EditText inputCorSolo, inputTextura, inputEstruturas, inputMaterialArqueologico, inputRestosOrganicos, inputObservacoes, inputEstruturaDescricao;
    private Button btnSalvarFicha;
    private CheckBox checkboxEstruturaPresente, checkCeramica, checkVidro, checkFianca, checkFerro, checkOutros;
    private RadioGroup radioGroupTextura, radioGroupColoracao;
    private ImageView imgCroqui;
    private String caminhoCroqui = null, dataFotoCroqui = "";
    private static final int REQUEST_FOTO_CROQUI = 101, REQUEST_DESENHO_CROQUI = 102;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0.0, longitude = 0.0;
    private String fichaId = null;
    private Date dataSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_ficha);
        fichaId = getIntent().getStringExtra("fichaId");

        inputSitio = findViewById(R.id.input_sitio);
        inputLocalizacao = findViewById(R.id.input_localizacao);
        inputQuadra = findViewById(R.id.input_quadra);
        inputProfundidade = findViewById(R.id.input_profundidade);
        inputPesquisador = findViewById(R.id.input_pesquisador);
        inputData = findViewById(R.id.input_data);
        inputCorSolo = findViewById(R.id.input_cor_solo);
        inputTextura = findViewById(R.id.input_textura);
        inputEstruturas = findViewById(R.id.input_estruturas);
        inputMaterialArqueologico = findViewById(R.id.input_material_arqueologico);
        inputRestosOrganicos = findViewById(R.id.input_restos_organicos);
        inputObservacoes = findViewById(R.id.input_observacoes);
        inputEstruturaDescricao = findViewById(R.id.input_estrutura_descricao);

        checkboxEstruturaPresente = findViewById(R.id.checkbox_estrutura_presente);
        checkCeramica = findViewById(R.id.check_ceramica);
        checkVidro = findViewById(R.id.check_vidro);
        checkFianca = findViewById(R.id.check_faiança);
        checkFerro = findViewById(R.id.check_ferro);
        checkOutros = findViewById(R.id.check_outros);

        radioGroupTextura = findViewById(R.id.radio_group_textura);
        radioGroupColoracao = findViewById(R.id.radio_group_coloracao);
        btnSalvarFicha = findViewById(R.id.btn_salvar_ficha);
        imgCroqui = findViewById(R.id.img_croqui);


        // Inicialização
        if (fichaId != null) {
            Realm realm = Realm.getDefaultInstance();
            Ficha ficha = realm.where(Ficha.class).equalTo("id", fichaId).findFirst();
            if (ficha != null) {
                inputSitio.setText(ficha.getSitio());
                inputLocalizacao.setText(ficha.getLocalizacao());
                inputQuadra.setText(ficha.getQuadra());
                inputProfundidade.setText(ficha.getProfundidade());
                inputPesquisador.setText(ficha.getPesquisador());
                inputData.setText(ficha.getDataTexto());
                inputCorSolo.setText(ficha.getCorSolo());
                inputTextura.setText(ficha.getTextura());
                inputEstruturas.setText(ficha.getEstruturas());
                inputMaterialArqueologico.setText(ficha.getMaterialArqueologico());
                inputRestosOrganicos.setText(ficha.getRestosOrganicos());
                inputObservacoes.setText(ficha.getObservacoes());
                inputEstruturaDescricao.setText(ficha.getEstruturaDescricao());

                checkboxEstruturaPresente.setChecked(ficha.isEstruturaPresente());

                // Preenche os checkboxes de artefato
                if (ficha.getTiposArtefato() != null) {
                    checkCeramica.setChecked(ficha.getTiposArtefato().contains("Cerâmica"));
                    checkVidro.setChecked(ficha.getTiposArtefato().contains("Vidro"));
                    checkFianca.setChecked(ficha.getTiposArtefato().contains("Faiança"));
                    checkFerro.setChecked(ficha.getTiposArtefato().contains("Ferro"));
                    checkOutros.setChecked(ficha.getTiposArtefato().contains("Outros"));
                }

                // Preenche os radio buttons
                if (ficha.getTexturaSolo() != null) {
                    for (int i = 0; i < radioGroupTextura.getChildCount(); i++) {
                        RadioButton radio = (RadioButton) radioGroupTextura.getChildAt(i);
                        if (radio.getText().toString().equals(ficha.getTexturaSolo())) {
                            radio.setChecked(true);
                            break;
                        }
                    }
                }

                if (ficha.getColoracaoSolo() != null) {
                    for (int i = 0; i < radioGroupColoracao.getChildCount(); i++) {
                        RadioButton radio = (RadioButton) radioGroupColoracao.getChildAt(i);
                        if (radio.getText().toString().equals(ficha.getColoracaoSolo())) {
                            radio.setChecked(true);
                            break;
                        }
                    }
                }

                // Se já tiver croqui, carrega imagem
                if (ficha.getCaminhoCroqui() != null) {
                    caminhoCroqui = ficha.getCaminhoCroqui();
                    imgCroqui.setImageURI(Uri.fromFile(new File(caminhoCroqui)));
                }

                // Preenche data da foto se quiser exibir (opcional)
                dataFotoCroqui = ficha.getDataFotoCroqui();
            }
        }



        inputData.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        dataSelecionada = calendar.getTime();
                        String dataFormatada = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        inputData.setText(dataFormatada);
                    }, year, month, day);

            datePickerDialog.show();
        });


        Button btnVoltar = findViewById(R.id.btn_voltar_menu);
        btnVoltar.setOnClickListener(v -> {
            startActivity(new Intent(NovaFichaActivity.this, MainActivity.class));
            finish();
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        solicitarPermissoes();
        getLastLocation();

        Button btnFotoCroqui = findViewById(R.id.btn_foto_croqui);
        btnFotoCroqui.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File foto = new File(getExternalFilesDir(null), "croqui_" + System.currentTimeMillis() + ".jpg");
            caminhoCroqui = foto.getAbsolutePath();
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", foto);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_FOTO_CROQUI);
        });

        Button btnDesenharCroqui = findViewById(R.id.btn_desenhar_croqui);
        btnDesenharCroqui.setOnClickListener(v -> startActivityForResult(new Intent(this, DesenhoCroquiActivity.class), REQUEST_DESENHO_CROQUI));

        btnSalvarFicha.setOnClickListener(v -> {
            getLastLocation(); // tenta capturar de novo

            new Handler().postDelayed(() -> {
                if (latitude == 0.0 && longitude == 0.0) {
                    Toast.makeText(this, "Localização não disponível. Salvando mesmo assim.", Toast.LENGTH_SHORT).show();
                }

                try {
                    salvarFicha();
                } catch (Exception e) {
                    Log.e("SALVAR_DEBUG", "Erro ao salvar ficha: ", e);
                    Toast.makeText(this, "Erro inesperado ao salvar. Veja o Logcat.", Toast.LENGTH_LONG).show();
                }
            }, 1500);
            // aguarda 1,5 segundo antes de verificar
        });



        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(NovaFichaActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void solicitarPermissoes() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, 200);
        }
    }

    private void salvarFicha() {
        String sitio = inputSitio.getText().toString().trim();
        String localizacao = inputLocalizacao.getText().toString().trim();
        String quadra = inputQuadra.getText().toString().trim();
        String profundidade = inputProfundidade.getText().toString().trim();
        String pesquisador = inputPesquisador.getText().toString().trim();
        String dataTexto = inputData.getText().toString().trim();
        String corSolo = inputCorSolo.getText().toString().trim();
        String texturaLivre = inputTextura.getText().toString().trim();
        String estruturas = inputEstruturas.getText().toString().trim();
        String material = inputMaterialArqueologico.getText().toString().trim();
        String restos = inputRestosOrganicos.getText().toString().trim();
        String observacoes = inputObservacoes.getText().toString().trim();

        if (sitio.isEmpty() || localizacao.isEmpty() || quadra.isEmpty() || profundidade.isEmpty() || pesquisador.isEmpty() || dataTexto.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(r -> {
            Ficha ficha;
            if (fichaId != null) {
                ficha = r.where(Ficha.class).equalTo("id", fichaId).findFirst();
            } else {
                ficha = r.createObject(Ficha.class, UUID.randomUUID().toString());
            }
            if (ficha != null) {
                ficha.setSitio(sitio);
                ficha.setLocalizacao(localizacao);
                ficha.setQuadra(quadra);
                ficha.setProfundidade(profundidade);
                ficha.setPesquisador(pesquisador);
                ficha.setDataTexto(dataTexto);
                ficha.setCorSolo(corSolo);
                ficha.setTextura(texturaLivre);
                ficha.setEstruturas(estruturas);
                ficha.setMaterialArqueologico(material);
                ficha.setRestosOrganicos(restos);
                ficha.setObservacoes(observacoes);
                ficha.setCaminhoCroqui(caminhoCroqui);
                if (latitude == 0.0 && longitude == 0.0) {
                    // Fallback para marcar como não disponível
                    ficha.setLatitude(-999.999);
                    ficha.setLongitude(-999.999);
                } else {
                    ficha.setLatitude(latitude);
                    ficha.setLongitude(longitude);
                }

                ficha.setLatitude(latitude);
                ficha.setLongitude(longitude);
                ficha.setDataFotoCroqui(dataFotoCroqui);
                ficha.setEstruturaPresente(checkboxEstruturaPresente.isChecked());
                ficha.setEstruturaDescricao(inputEstruturaDescricao.getText().toString().trim());

                List<String> tiposArtefato = new ArrayList<>();
                if (checkCeramica.isChecked()) tiposArtefato.add("Cerâmica");
                if (checkVidro.isChecked()) tiposArtefato.add("Vidro");
                if (checkFianca.isChecked()) tiposArtefato.add("Faiança");
                if (checkFerro.isChecked()) tiposArtefato.add("Ferro");
                if (checkOutros.isChecked()) tiposArtefato.add("Outros");
                RealmList<String> listaRealm = new RealmList<>();
                listaRealm.addAll(tiposArtefato);
                ficha.setTiposArtefato(listaRealm);

                int texturaId = radioGroupTextura.getCheckedRadioButtonId();
                if (texturaId != -1)
                    ficha.setTexturaSolo(((RadioButton) findViewById(texturaId)).getText().toString());

                int coloracaoId = radioGroupColoracao.getCheckedRadioButtonId();
                if (coloracaoId != -1)
                    ficha.setColoracaoSolo(((RadioButton) findViewById(coloracaoId)).getText().toString());
            }
        }, () -> runOnUiThread(() -> {
            Log.d("FLUXO", "⚙️ Iniciando pós-salvamento da ficha Realm");
            Toast.makeText(this, "Ficha salva com sucesso!", Toast.LENGTH_LONG).show();
            Log.d("FLUXO", "✅ Ficha salva localmente (Realm)");

            // 🔹 Criar objeto DTO e enviar para API online:
            FichaDTO fichaDTO = new FichaDTO();
            fichaDTO.sitio = sitio;
            fichaDTO.localizacao = localizacao;
            fichaDTO.quadra = quadra;
            fichaDTO.profundidade = profundidade;
            fichaDTO.pesquisador = pesquisador;
            fichaDTO.dataTexto = dataTexto;
            fichaDTO.latitude = latitude;
            fichaDTO.longitude = longitude;
            Log.d("FLUXO", "📦 Objeto FichaDTO criado com dados:");
            Log.d("FLUXO", "Sitio: " + sitio + ", Pesquisador: " + pesquisador + ", Lat: " + latitude + ", Long: " + longitude);


            FichaApi api = ApiClient.getClient().create(FichaApi.class);
            Log.d("API_TESTE", "Iniciando envio para a API...");

            api.salvarFicha(fichaDTO).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("API_TESTE", "✅ Ficha enviada com sucesso ao servidor (HTTP " + response.code() + ")");
                    if (response.isSuccessful()) {
                        Log.d("API", "Ficha enviada com sucesso ao servidor.");
                        runOnUiThread(() -> {
                            Toast.makeText(NovaFichaActivity.this, "Ficha salva com sucesso!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(NovaFichaActivity.this, ListaFichasActivity.class));
                            finish();
                        });
                    } else {
                        Log.e("API", "Erro na resposta da API: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("API", "Erro ao enviar ficha para servidor: " + t.getMessage());
                }
            });
            Log.d("FLUXO", "📤 Chamada enqueue realizada com Retrofit");

        }), error -> runOnUiThread(() -> {
            Toast.makeText(this, "Erro ao salvar ficha. Verifique os campos.", Toast.LENGTH_LONG).show();
            Log.e("REALM_ERROR", "Erro: ", error);
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FOTO_CROQUI) {
                imgCroqui.setImageURI(Uri.fromFile(new File(caminhoCroqui)));
                dataFotoCroqui = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            } else if (requestCode == REQUEST_DESENHO_CROQUI && data != null) {
                caminhoCroqui = data.getStringExtra("caminhoCroqui");
                imgCroqui.setImageURI(Uri.fromFile(new File(caminhoCroqui)));
            }
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("GPS_DEBUG", "Permissão negada");
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.d("GPS_DEBUG", "Localização obtida: Lat " + latitude + ", Lon " + longitude);
            } else {
                Log.w("GPS_DEBUG", "Localização ainda é null (emulador pode demorar)");
            }
        }).addOnFailureListener(e -> {
            Log.e("GPS_DEBUG", "Erro ao tentar pegar localização: " + e.getMessage());
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        } else {
            Toast.makeText(this, "Permissão negada.", Toast.LENGTH_SHORT).show();
        }
    }
}
