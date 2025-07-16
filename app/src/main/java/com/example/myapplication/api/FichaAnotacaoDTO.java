package com.example.myapplication.api;

public class FichaAnotacaoDTO {
    private String _id;
    private String idProjeto;
    private String titulo;
    private String texto;
    private String data;

    public FichaAnotacaoDTO(String id, String idProjeto, String titulo, String texto, String data) {
        this._id = id;
        this.idProjeto = idProjeto;
        this.titulo = titulo;
        this.texto = texto;
        this.data = data;
    }
}
