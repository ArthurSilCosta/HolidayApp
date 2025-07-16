package com.example.myapplication.api;

public class FichaFotoDTO {
    private String _id;
    private String idProjeto;
    private String titulo;
    private String legenda;
    private String data;

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }


    public FichaFotoDTO(String id, String idProjeto, String titulo, String legenda, String data) {
        this._id = id;
        this.idProjeto = idProjeto;
        this.titulo = titulo;
        this.legenda = legenda;
        this.data = data;
    }
}
