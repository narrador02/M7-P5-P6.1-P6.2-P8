package com.example.m07_p5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    private val foodList: MutableList<FoodItem>,
    private val onItemClick: (FoodItem) -> Unit,
    private val onItemDelete: (Int) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodName: TextView = view.findViewById(R.id.food_name)
        val foodQuantity: TextView = view.findViewById(R.id.food_quantity)
        val foodDate: TextView = view.findViewById(R.id.food_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.foodName.text = food.name
        holder.foodQuantity.text = "${food.quantity} gr"
        holder.foodDate.text = "Fecha: ${food.date}"
        holder.itemView.setOnClickListener {
            onItemClick(food)
        }

        holder.itemView.setOnLongClickListener {
            onItemDelete(position)
            true
        }
    }

    override fun getItemCount() = foodList.size
}