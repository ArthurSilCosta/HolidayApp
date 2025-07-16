package com.example.myapplication.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FichaArtefato extends RealmObject {

    @PrimaryKey
    private String id;

    private String idProjeto;
    private String tipoArtefato;
    private String material;
    private String dimensoes;
    private String contexto;
    private String dataDescoberta;
    private String descricaoArtefato;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(String idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getTipoArtefato() {
        return tipoArtefato;
    }

    public void setTipoArtefato(String tipoArtefato) {
        this.tipoArtefato = tipoArtefato;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDimensoes() {
        return dimensoes;
    }

    public void setDimensoes(String dimensoes) {
        this.dimensoes = dimensoes;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public String getDataDescoberta() {
        return dataDescoberta;
    }

    public void setDataDescoberta(String dataDescoberta) {
        this.dataDescoberta = dataDescoberta;
    }

    public String getDescricaoArtefato() {
        return descricaoArtefato;
    }

    public void setDescricaoArtefato(String descricaoArtefato) {
        this.descricaoArtefato = descricaoArtefato;
    }
}
