package com.example.m07_p5.data.model

data class Alimento(
    val nombre: String,
    val calorias: Int,
    val proteinas: Float?,
    val grasas: Float?,
    val carbohidratos: Float?,
    val imagenUrl: String?,
    val categoria: String
)
