package com.example.m07_p5.data.network

import com.example.m07_p5.data.model.Alimento
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @GET("alimentos")
    suspend fun getAlimentos(): List<Alimento>

    @POST("alimentos")
    suspend fun addAlimento(@Body alimento: Alimento): Alimento

    @DELETE("alimentos")
    suspend fun deleteAlimentoByName(@Query("nombre") nombre: String)

    @PUT("alimentos/{id}")
    suspend fun updateAlimento(@Path("id") id: Int, @Body alimento: Alimento): Alimento
}