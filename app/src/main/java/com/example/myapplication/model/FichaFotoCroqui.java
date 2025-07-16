package com.example.myapplication.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FichaFotoCroqui extends RealmObject {

    @PrimaryKey
    private String id;
    private String idProjeto;

    // Parte da foto
    private String legenda;
    private String caminhoFoto;

    // Parte do croqui técnico
    private String tituloCroqui;
    private String anotacaoCroqui;
    private String caminhoCroqui;

    // Getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdProjeto() { return idProjeto; }
    public void setIdProjeto(String idProjeto) { this.idProjeto = idProjeto; }

    public String getLegenda() { return legenda; }
    public void setLegenda(String legenda) { this.legenda = legenda; }

    public String getCaminhoFoto() { return caminhoFoto; }
    public void setCaminhoFoto(String caminhoFoto) { this.caminhoFoto = caminhoFoto; }

    public String getTituloCroqui() { return tituloCroqui; }
    public void setTituloCroqui(String tituloCroqui) { this.tituloCroqui = tituloCroqui; }

    public String getAnotacaoCroqui() { return anotacaoCroqui; }
    public void setAnotacaoCroqui(String anotacaoCroqui) { this.anotacaoCroqui = anotacaoCroqui; }

    public String getCaminhoCroqui() { return caminhoCroqui; }
    public void setCaminhoCroqui(String caminhoCroqui) { this.caminhoCroqui = caminhoCroqui; }
}
