package com.example.myapplication.api;

public class FichaCroquiDTO {
    private String _id;
    private String idProjeto;
    private String titulo;
    private String anotacao;
    private String data;

    public FichaCroquiDTO(String id, String idProjeto, String titulo, String anotacao, String data) {
        this._id = id;
        this.idProjeto = idProjeto;
        this.titulo = titulo;
        this.anotacao = anotacao;
        this.data = data;
    }
}
