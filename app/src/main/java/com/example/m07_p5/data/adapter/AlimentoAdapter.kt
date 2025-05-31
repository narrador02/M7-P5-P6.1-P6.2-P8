package com.example.m07_p5.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.m07_p5.R
import com.example.m07_p5.data.model.Alimento

class AlimentoAdapter(
    private val alimentos: MutableList<Alimento>,
    private val onEditClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit) : RecyclerView.Adapter<AlimentoAdapter.AlimentoViewHolder>() {

    class AlimentoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.food_name)
        val calorias: TextView = view.findViewById(R.id.food_calories)
        val imagen: ImageView = view.findViewById(R.id.food_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlimentoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return AlimentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlimentoViewHolder, position: Int) {
        val alimento = alimentos[position]
        holder.nombre.text = alimento.nombre
        holder.calorias.text = "${alimento.calorias} kcal"
        Glide.with(holder.itemView.context)
            .load(alimento.imagenUrl)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .into(holder.imagen)

        holder.itemView.setOnClickListener {
            onEditClick(position)
        }

        holder.itemView.setOnLongClickListener {
            onDeleteClick(position)
            true
        }
    }

    override fun getItemCount() = alimentos.size
}