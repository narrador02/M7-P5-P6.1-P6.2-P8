package com.example.m07_p5.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.m07_p5.data.model.Alimento
import com.example.m07_p5.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlimentoViewModel : ViewModel() {
    val alimentos = mutableListOf<Alimento>()

    fun fetchAlimentos(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.getAlimentos()
                }
                alimentos.clear()
                alimentos.addAll(response)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    fun addAlimento(alimento: Alimento, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val newAlimento = withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.addAlimento(alimento)
                }
                alimentos.add(newAlimento)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }

    fun deleteAlimento(position: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val alimento = alimentos[position]
                withContext(Dispatchers.IO) {
                    RetrofitClient.apiService.deleteAlimentoByName(alimento.nombre)
                }
                alimentos.removeAt(position)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }
}