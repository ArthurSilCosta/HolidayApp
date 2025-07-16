package com.example.myapplication.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FotoService {
    @POST("/api/fotos")
    Call<Void> enviarFoto(@Body FichaFotoDTO foto);
}
