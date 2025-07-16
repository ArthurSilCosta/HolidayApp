package com.example.myapplication.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FichaAnotacao extends RealmObject {
    @PrimaryKey
    private String id;
    private String idProjeto;
    private String titulo;
    private String conteudo;
    private String dataCriacao;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdProjeto() { return idProjeto; }
    public void setIdProjeto(String idProjeto) { this.idProjeto = idProjeto; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public String getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }
}
