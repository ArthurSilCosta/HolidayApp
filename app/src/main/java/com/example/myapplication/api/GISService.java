package com.example.myapplication.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GISService {
    @POST("/api/gis")
    Call<Void> enviarGIS(@Body FichaGISDTO gis);
}
