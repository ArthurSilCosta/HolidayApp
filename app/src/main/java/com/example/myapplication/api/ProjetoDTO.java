package com.example.myapplication.api;

public class ProjetoDTO {
    private String _id;
    private String nome;
    private String dataCriacao;

    public ProjetoDTO(String id, String nome, String dataCriacao) {
        this._id = id;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
    }
}
