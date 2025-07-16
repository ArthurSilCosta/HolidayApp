package com.example.myapplication.api;

import android.widget.EditText;

public class FichaSitioDTO {
    private String _id;
    private String idProjeto;
    private String nome;
    private String localizacao;
    private String descricao;
    private String data;

    public FichaSitioDTO(String id, String idProjeto, String nome, String localizacao, String descricao, String data) {
        this._id = id;
        this.idProjeto = idProjeto;
        this.nome = nome;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.data = data;
    }


}
