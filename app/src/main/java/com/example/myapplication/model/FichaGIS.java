package com.example.myapplication.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FichaGIS extends RealmObject {

    @PrimaryKey
    private String id;
    private String idProjeto;
    private String tipoDado;
    private String coordenadas;
    private String fonte;
    private String descricao;
    private String data; // Formato ISO: yyyy-MM-dd


    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdProjeto() { return idProjeto; }
    public void setIdProjeto(String idProjeto) { this.idProjeto = idProjeto; }

    public String getTipoDado() { return tipoDado; }
    public void setTipoDado(String tipoDado) { this.tipoDado = tipoDado; }

    public String getCoordenadas() { return coordenadas; }
    public void setCoordenadas(String coordenadas) { this.coordenadas = coordenadas; }

    public String getFonte() { return fonte; }
    public void setFonte(String fonte) { this.fonte = fonte; }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
