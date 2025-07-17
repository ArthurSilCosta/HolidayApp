package com.example.myapplication.view;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.FichaAnotacao;
import com.example.myapplication.model.FichaArtefato;
import com.example.myapplication.model.FichaCroqui;
import com.example.myapplication.model.FichaGIS;
import com.example.myapplication.model.FichaImagem;
import com.example.myapplication.model.FichaSitio;
import com.example.myapplication.model.Projeto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.realm.Realm;

public class DetalhesProjetoActivity extends AppCompatActivity {

    private String idProjeto;
    private Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_projeto);

        idProjeto = getIntent().getStringExtra("idProjeto");


        Realm realm = Realm.getDefaultInstance();
        Projeto projeto = realm.where(Projeto.class).equalTo("id", idProjeto).findFirst();

        TextView txtTituloProjeto = findViewById(R.id.txt_titulo_projeto);
        if (projeto != null) {
            txtTituloProjeto.setText(getString(R.string.project_title)+ " " + projeto.getNome());
        } else {
            txtTituloProjeto.setText(getString(R.string.project_title) + " " + idProjeto); // fallback
        }
        Button btnGerarPdf = findViewById(R.id.btn_gerar_pdf);
        btnGerarPdf.setOnClickListener(v -> {


            List<FichaSitio> sitios = realm.where(FichaSitio.class).equalTo("idProjeto", idProjeto).findAll();
            List<FichaArtefato> artefatos = realm.where(FichaArtefato.class).equalTo("idProjeto", idProjeto).findAll();
            List<FichaGIS> gisList = realm.where(FichaGIS.class).equalTo("idProjeto", idProjeto).findAll();
            List<FichaAnotacao> anotacoes = realm.where(FichaAnotacao.class).equalTo("idProjeto", idProjeto).findAll();
            List<FichaImagem> imagens = realm.where(FichaImagem.class).equalTo("idProjeto", idProjeto).findAll();
            List<FichaCroqui> croquis = realm.where(FichaCroqui.class).equalTo("idProjeto", idProjeto).findAll();

            gerarPdfProjeto(projeto, sitios, artefatos, gisList, anotacoes, imagens, croquis);
        });


        Button btnSitios = findViewById(R.id.btn_sitios);
        Button btnArtefatos = findViewById(R.id.btn_artefatos);
        Button btnGIS = findViewById(R.id.btn_gis);
        Button btnAnotacoes = findViewById(R.id.btn_anotacoes);
        Button btnFotos = findViewById(R.id.btn_fotos);

        btnSitios.setOnClickListener(v -> abrirCategoria("sitios"));
        btnArtefatos.setOnClickListener(v -> abrirCategoria("artefatos"));
        btnGIS.setOnClickListener(v -> abrirCategoria("gis"));
        btnAnotacoes.setOnClickListener(v -> abrirCategoria("anotacoes"));
        btnFotos.setOnClickListener(v -> abrirCategoria("fotos"));
        Button btnMenuPrincipal = findViewById(R.id.btn_menu_principal);

        btnMenuPrincipal.setOnClickListener(v -> {
            Intent intent = new Intent(DetalhesProjetoActivity.this, MenuProjetosActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            finish();
        });
        realm.close();


    }

    private void abrirCategoria(String categoria) {
        Intent intent = new Intent(this, ListaFichasCategoriaActivity.class);
        intent.putExtra("idProjeto", idProjeto);
        intent.putExtra("categoria", categoria);
        startActivity(intent);

    }

    private void gerarPdfProjeto(Projeto projeto,
                                 List<FichaSitio> sitios,
                                 List<FichaArtefato> artefatos,
                                 List<FichaGIS> gisList,
                                 List<FichaAnotacao> anotacoes,
                                 List<FichaImagem> imagens,
                                 List<FichaCroqui> croquis) {

        PdfDocument pdf = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        titlePaint.setTextSize(20);
        titlePaint.setFakeBoldText(true);

        // Página 1 - Cabeçalho do Projeto
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        int y = 50; // Posição inicial no eixo Y

        canvas.drawText("Projeto: " + projeto.getNome(), 50, y, titlePaint);
        y += 30;
        canvas.drawText("Data de Criação: " + projeto.getDataCriacao().toString(), 50, y, paint);

        y += 50;
        canvas.drawText("=== Sítios Arqueológicos ===", 50, y, titlePaint);
        y += 30;

        for (FichaSitio sitio : sitios) {
            canvas.drawText("- " + sitio.getSitio() + " | " + sitio.getLocalizacao(), 50, y, paint);
            y += 20;
            if (y > 800) { // Nova página se exceder
                pdf.finishPage(page);
                pageInfo = new PdfDocument.PageInfo.Builder(595, 842, pdf.getPages().size() + 1).create();
                page = pdf.startPage(pageInfo);
                canvas = page.getCanvas();
                y = 50;
            }
        }

        // === Artefatos ===
        y += 20;
        canvas.drawText("=== Artefatos ===", 50, y, titlePaint);
        y += 25;
        for (FichaArtefato artefato : artefatos) {
            y = verificarNovaPagina(pdf, page, y, pageWidth, pageHeight);
            canvas = page.getCanvas();

            canvas.drawText("- Tipo: " + artefato.getTipoArtefato(), 50, y, paint);
            y += 18;
            canvas.drawText("  Descrição: " + artefato.getDescricaoArtefato(), 50, y, paint);
            y += 18;
            canvas.drawText("  Material: " + artefato.getMaterial(), 50, y, paint);
            y += 18;
            canvas.drawText("  Dimensões: " + artefato.getDimensoes(), 50, y, paint);
            y += 18;
            canvas.drawText("  Contexto: " + artefato.getContexto(), 50, y, paint);
            y += 25;
        }

        // === GIS ===
        y += 20;
        canvas.drawText("=== Informações GIS ===", 50, y, titlePaint);
        y += 25;
        for (FichaGIS gis : gisList) {
            y = verificarNovaPagina(pdf, page, y, pageWidth, pageHeight);
            canvas = page.getCanvas();

            canvas.drawText("- Tipo: " + gis.getTipoDado(), 50, y, paint);
            y += 18;
            canvas.drawText("  Coordenadas: " + gis.getCoordenadas(), 50, y, paint);
            y += 18;
            canvas.drawText("  Fonte: " + gis.getFonte(), 50, y, paint);
            y += 18;
            canvas.drawText("  Descrição: " + gis.getDescricao(), 50, y, paint);
            y += 18;
            canvas.drawText("  Data: " + gis.getData(), 50, y, paint);
            y += 25;
        }

        // === Anotações ===
        y += 20;
        canvas.drawText("=== Anotações Gerais ===", 50, y, titlePaint);
        y += 25;
        for (FichaAnotacao anotacao : anotacoes) {
            y = verificarNovaPagina(pdf, page, y, pageWidth, pageHeight);
            canvas = page.getCanvas();

            canvas.drawText("- Título: " + anotacao.getTitulo(), 50, y, paint);
            y += 18;
            canvas.drawText("  Conteúdo: " + anotacao.getConteudo(), 50, y, paint);
            y += 18;
            canvas.drawText("  Data: " + anotacao.getDataCriacao(), 50, y, paint);
            y += 25;
        }

        // === Fotos ===
        y += 20;
        canvas.drawText("=== Fotos ===", 50, y, titlePaint);
        y += 25;
        for (FichaImagem img : imagens) {
            y = verificarNovaPagina(pdf, page, y, pageWidth, pageHeight);
            canvas = page.getCanvas();

            canvas.drawText("- Legenda: " + img.getLegenda(), 50, y, paint);
            y += 18;
            canvas.drawText("  Título: " + img.getTituloCroqui(), 50, y, paint);
            y += 18;
            canvas.drawText("  Data: " + img.getData(), 50, y, paint);
            y += 25;
        }

        // === Croquis ===
        y += 20;
        canvas.drawText("=== Croquis ===", 50, y, titlePaint);
        y += 25;
        for (FichaCroqui c : croquis) {
            y = verificarNovaPagina(pdf, page, y, pageWidth, pageHeight);
            canvas = page.getCanvas();

            canvas.drawText("- Título: " + c.getTitulo(), 50, y, paint);
            y += 18;
            canvas.drawText("  Anotação: " + c.getAnotacao(), 50, y, paint);
            y += 18;
            canvas.drawText("  Data: " + c.getData(), 50, y, paint);
            y += 25;
        }
        pdf.finishPage(page);

        // Salvar PDF no armazenamento interno (Downloads)
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                    projeto.getNome().replace(" ", "_") + "_completo.pdf");
            pdf.writeTo(new FileOutputStream(file));
            Toast.makeText(this, "PDF salvo em: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erro ao salvar PDF", Toast.LENGTH_SHORT).show();
        }

        pdf.close();
    }
    private int verificarNovaPagina(PdfDocument pdf, PdfDocument.Page page,
                                    int y, int pageWidth, int pageHeight) {
        if (y > pageHeight - 50) {
            pdf.finishPage(page);
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pdf.getPages().size() + 1).create();
            PdfDocument.Page newPage = pdf.startPage(pageInfo);
            return 50; // retorna o Y inicial da nova página
        }
        return y;
    }


}
