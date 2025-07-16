package com.example.myapplication.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FichaApi {
    @POST("/fichas")
    Call<Void> salvarFicha(@Body FichaDTO ficha);
}

