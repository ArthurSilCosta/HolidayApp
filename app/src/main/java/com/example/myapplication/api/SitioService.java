package com.example.myapplication.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SitioService {
    @POST("/api/sitios")
    Call<Void> enviarSitio(@Body FichaSitioDTO sitio);
}
