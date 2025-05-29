package com.example.m07_p5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister: Button = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener {
            // Simula el registro exitoso
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

            // Navega de vuelta a LoginActivity
            Log.d("RegisterActivity", "Registro exitoso, volviendo a LoginActivity")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Cierra RegisterActivity para que no quede en la pila de actividades
            finish()
        }
    }
}