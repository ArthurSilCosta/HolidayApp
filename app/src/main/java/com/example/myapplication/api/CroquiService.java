package com.example.myapplication.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CroquiService {
    @POST("/api/croquis")
    Call<Void> enviarCroqui(@Body FichaCroquiDTO croqui);
}
