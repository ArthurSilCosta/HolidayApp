package com.example.myapplication.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FichaImagem extends RealmObject {
    @PrimaryKey
    private String id;
    private String idProjeto;
    private String legenda;
    private String caminhoImagem;
    private String tituloCroqui;
    private String anotacaoCroqui;
    private String data;

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }


    // Getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdProjeto() { return idProjeto; }
    public void setIdProjeto(String idProjeto) { this.idProjeto = idProjeto; }

    public String getLegenda() { return legenda; }
    public void setLegenda(String legenda) { this.legenda = legenda; }

    public String getCaminhoImagem() { return caminhoImagem; }
    public void setCaminhoImagem(String caminhoImagem) { this.caminhoImagem = caminhoImagem; }

    public String getTituloCroqui() { return tituloCroqui; }
    public void setTituloCroqui(String tituloCroqui) { this.tituloCroqui = tituloCroqui; }

    public String getAnotacaoCroqui() { return anotacaoCroqui; }
    public void setAnotacaoCroqui(String anotacaoCroqui) { this.anotacaoCroqui = anotacaoCroqui; }
}
