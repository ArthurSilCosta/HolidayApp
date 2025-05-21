package com.example.myapplication.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.myapplication.R;
import com.example.myapplication.model.Ficha;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;

import io.realm.Realm;
import io.realm.RealmList;

public class NovaFichaActivity extends AppCompatActivity {

    private EditText inputSitio, inputLocalizacao, inputQuadra, inputProfundidade, inputPesquisador, inputData;
    private EditText inputCorSolo, inputTextura, inputEstruturas, inputMaterialArqueologico, inputRestosOrganicos, inputObservacoes;
    private Button btnSalvarFicha;
    private Date dataSelecionada;
    private String fichaId = null;
    private CheckBox checkboxEstruturaPresente;
    private EditText inputEstruturaDescricao;
    private CheckBox checkCeramica, checkVidro, checkFianca, checkFerro, checkOutros;
    private RadioGroup radioGroupTextura, radioGroupColoracao;

    private ImageView imgCroqui;
    private String caminhoCroqui = null;
    private static final int REQUEST_FOTO_CROQUI = 101;
    private static final int REQUEST_DESENHO_CROQUI = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_ficha);

        checkboxEstruturaPresente = findViewById(R.id.checkbox_estrutura_presente);
        inputEstruturaDescricao = findViewById(R.id.input_estrutura_descricao);

        checkCeramica = findViewById(R.id.check_ceramica);
        checkVidro = findViewById(R.id.check_vidro);
        checkFianca = findViewById(R.id.check_faiança);
        checkFerro = findViewById(R.id.check_ferro);
        checkOutros = findViewById(R.id.check_outros);

        radioGroupTextura = findViewById(R.id.radio_group_textura);
        radioGroupColoracao = findViewById(R.id.radio_group_coloracao);

        // Inicializar campos
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
        btnSalvarFicha = findViewById(R.id.btn_salvar_ficha);
        Button btnVoltar = findViewById(R.id.btn_voltar_menu);
        btnVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(NovaFichaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        imgCroqui = findViewById(R.id.img_croqui);

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
        btnDesenharCroqui.setOnClickListener(v -> {
            Intent intent = new Intent(this, DesenhoCroquiActivity.class);
            startActivityForResult(intent, REQUEST_DESENHO_CROQUI);
        });

        fichaId = getIntent().getStringExtra("fichaId");
        final Ficha[] ficha = new Ficha[1];


        if (fichaId != null) {
            Realm realm = Realm.getDefaultInstance();
            ficha[0] = realm.where(Ficha.class).equalTo("id", fichaId).findFirst();

        }

        Button btnExcluir = findViewById(R.id.btn_excluir_ficha);
        btnExcluir.setOnClickListener(v -> {
            if (ficha[0] != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Confirmar exclusão")
                        .setMessage("Tem certeza que deseja excluir esta ficha?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            Realm realm1 = Realm.getDefaultInstance();
                            realm1.executeTransaction(r -> {
                                Ficha fichaToDelete = r.where(Ficha.class).equalTo("id", fichaId).findFirst();
                                if (fichaToDelete != null) {
                                    fichaToDelete.deleteFromRealm();
                                }
                            });
                            Toast.makeText(this, "Ficha excluída com sucesso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, ListaFichasActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });


        // Modo edição: carregar dados
        Intent intent = getIntent();
        boolean modoEdicao = intent.getBooleanExtra("modoEdicao", false);

        if (modoEdicao) {
            fichaId = intent.getStringExtra("fichaId");
            Realm realm = Realm.getDefaultInstance();
            Ficha fichaEdicao = realm.where(Ficha.class).equalTo("id", fichaId).findFirst();

            if (fichaEdicao != null) {
                // Preencher campos com a ficha encontrada
                inputSitio.setText(fichaEdicao.getSitio());
                inputLocalizacao.setText(fichaEdicao.getLocalizacao());
                inputQuadra.setText(fichaEdicao.getQuadra());
                inputProfundidade.setText(fichaEdicao.getProfundidade());
                inputPesquisador.setText(fichaEdicao.getPesquisador());
                inputData.setText(fichaEdicao.getDataTexto());
                inputCorSolo.setText(fichaEdicao.getCorSolo());
                inputTextura.setText(fichaEdicao.getTextura());
                inputEstruturas.setText(fichaEdicao.getEstruturas());
                inputMaterialArqueologico.setText(fichaEdicao.getMaterialArqueologico());
                inputRestosOrganicos.setText(fichaEdicao.getRestosOrganicos());
                inputObservacoes.setText(fichaEdicao.getObservacoes());
            }
        }


        // Seletor de data
        inputData.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        dataSelecionada = calendar.getTime();
                        inputData.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    }, year, month, day);

            datePickerDialog.show();
        });

        // Botão salvar ficha
        btnSalvarFicha.setOnClickListener(v -> {
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
                Ficha fichaAlvo;

                if (fichaId != null) {
                    fichaAlvo = r.where(Ficha.class).equalTo("id", fichaId).findFirst();
                } else {
                    fichaAlvo = r.createObject(Ficha.class, java.util.UUID.randomUUID().toString());
                }

                if (fichaAlvo != null) {
                    fichaAlvo.setSitio(sitio);
                    fichaAlvo.setLocalizacao(localizacao);
                    fichaAlvo.setQuadra(quadra);
                    fichaAlvo.setProfundidade(profundidade);
                    fichaAlvo.setPesquisador(pesquisador);
                    fichaAlvo.setDataTexto(dataTexto);
                    fichaAlvo.setCorSolo(corSolo);
                    fichaAlvo.setTextura(texturaLivre);
                    fichaAlvo.setEstruturas(estruturas);
                    fichaAlvo.setMaterialArqueologico(material);
                    fichaAlvo.setRestosOrganicos(restos);
                    fichaAlvo.setObservacoes(observacoes);
                    fichaAlvo.setCaminhoCroqui(caminhoCroqui);

                    // Estruturas
                    fichaAlvo.setEstruturaPresente(checkboxEstruturaPresente.isChecked());
                    fichaAlvo.setEstruturaDescricao(inputEstruturaDescricao.getText().toString().trim());

                    // Artefatos múltiplos
                    List<String> tiposArtefato = new ArrayList<>();
                    if (checkCeramica.isChecked()) tiposArtefato.add("Cerâmica");
                    if (checkVidro.isChecked()) tiposArtefato.add("Vidro");
                    if (checkFianca.isChecked()) tiposArtefato.add("Faiança");
                    if (checkFerro.isChecked()) tiposArtefato.add("Ferro");
                    if (checkOutros.isChecked()) tiposArtefato.add("Outros");

                    RealmList<String> listaRealm = new RealmList<>();
                    listaRealm.addAll(tiposArtefato);
                    fichaAlvo.setTiposArtefato(listaRealm);

                    // Textura e coloração (radio)
                    int texturaId = radioGroupTextura.getCheckedRadioButtonId();
                    if (texturaId != -1) {
                        String texturaSelecionada = ((RadioButton) findViewById(texturaId)).getText().toString();
                        fichaAlvo.setTexturaSolo(texturaSelecionada);
                    }

                    int coloracaoId = radioGroupColoracao.getCheckedRadioButtonId();
                    if (coloracaoId != -1) {
                        String coloracaoSelecionada = ((RadioButton) findViewById(coloracaoId)).getText().toString();
                        fichaAlvo.setColoracaoSolo(coloracaoSelecionada);
                    }
                }
            }, () -> {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Ficha salva com sucesso!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, ListaFichasActivity.class));
                    finish();
                });
            }, error -> {
                runOnUiThread(() -> Toast.makeText(this, "Erro ao salvar ficha.", Toast.LENGTH_LONG).show());
            });
        });

        // Voltar ao menu com botão físico
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(NovaFichaActivity.this, MainActivity.class));
                finish();
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FOTO_CROQUI) {
                imgCroqui.setImageURI(Uri.fromFile(new File(caminhoCroqui)));
            } else if (requestCode == REQUEST_DESENHO_CROQUI && data != null) {
                caminhoCroqui = data.getStringExtra("caminhoCroqui");
                imgCroqui.setImageURI(Uri.fromFile(new File(caminhoCroqui)));
            }
        }
    }

}
