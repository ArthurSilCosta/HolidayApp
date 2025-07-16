package com.example.myapplication.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import java.util.UUID;

public class FichaSitio extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    private String idProjeto;
    private String sitio;
    private String localizacao;
    private String quadra;
    private String profundidade;
    private String pesquisador;
    private String dataTexto;
    private String corSolo;
    private String textura;
    private String estruturas;
    private String materialArqueologico;
    private String restosOrganicos;
    private String observacoes;
    private double latitude;
    private double longitude;

    // Getters e Setters
    public String getId() { return id; }

    public String getIdProjeto() { return idProjeto; }
    public void setIdProjeto(String idProjeto) { this.idProjeto = idProjeto; }

    public String getSitio() { return sitio; }
    public void setSitio(String sitio) { this.sitio = sitio; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getQuadra() { return quadra; }
    public void setQuadra(String quadra) { this.quadra = quadra; }

    public String getProfundidade() { return profundidade; }
    public void setProfundidade(String profundidade) { this.profundidade = profundidade; }

    public String getPesquisador() { return pesquisador; }
    public void setPesquisador(String pesquisador) { this.pesquisador = pesquisador; }

    public String getDataTexto() { return dataTexto; }
    public void setDataTexto(String dataTexto) { this.dataTexto = dataTexto; }

    public String getCorSolo() { return corSolo; }
    public void setCorSolo(String corSolo) { this.corSolo = corSolo; }

    public String getTextura() { return textura; }
    public void setTextura(String textura) { this.textura = textura; }

    public String getEstruturas() { return estruturas; }
    public void setEstruturas(String estruturas) { this.estruturas = estruturas; }

    public String getMaterialArqueologico() { return materialArqueologico; }
    public void setMaterialArqueologico(String materialArqueologico) { this.materialArqueologico = materialArqueologico; }

    public String getRestosOrganicos() { return restosOrganicos; }
    public void setRestosOrganicos(String restosOrganicos) { this.restosOrganicos = restosOrganicos; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}
