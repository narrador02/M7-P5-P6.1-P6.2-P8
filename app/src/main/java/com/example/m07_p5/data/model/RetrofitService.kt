package com.example.m07_p5.data.model

import retrofit2.http.*

interface RetrofitService {

    @GET("/alimentos")
    suspend fun listAlimentos(): List<Alimento>

    @POST("/alimentos")
    suspend fun addAlimento(@Body alimento: Alimento)

    @PUT("/alimentos/{id}")
    suspend fun updateAlimento(
        @Path("id") id: Int,
        @Body alimento: Alimento
    )

    @DELETE("/alimentos/{id}")
    suspend fun deleteAlimento(
        @Path("id") id: Int
    )

    @GET("/alimentos/{id}")
    suspend fun readAlimento(
        @Path("id") id: Int
    ): Alimento
}
