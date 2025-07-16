package com.example.myapplication.api;

public class FichaGISDTO {
    private String _id;
    private String idProjeto;
    private String coordenadas;
    private String observacoes;

    private String data;  // formato ISO

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public FichaGISDTO(String id, String idProjeto, String coordenadas, String observacoes, String data) {
        this._id = id;
        this.idProjeto = idProjeto;
        this.coordenadas = coordenadas;
        this.observacoes = observacoes;
        this.data = data;
    }
}
