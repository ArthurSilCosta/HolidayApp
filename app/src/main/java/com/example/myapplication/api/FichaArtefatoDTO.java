package com.example.myapplication.api;

public class FichaArtefatoDTO {
    private String _id;
    private String idProjeto;
    private String tipo;
    private String descricao;
    private String data;

    public FichaArtefatoDTO(String id, String idProjeto, String tipo, String descricao, String data) {
        this._id = id;
        this.idProjeto = idProjeto;
        this.tipo = tipo;
        this.descricao = descricao;
        this.data = data;
    }
}
